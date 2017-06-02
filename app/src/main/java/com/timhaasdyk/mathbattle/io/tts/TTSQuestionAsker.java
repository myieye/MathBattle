package com.timhaasdyk.mathbattle.io.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.io.QuestionAskedListener;
import com.timhaasdyk.mathbattle.io.QuestionAsker;
import com.timhaasdyk.mathbattle.io.tts.qtt.QuestionToTextConverter;
import com.timhaasdyk.mathbattle.models.Question;

import java.util.HashMap;
import java.util.Random;

import static com.timhaasdyk.mathbattle.util.ResourceUtil.str;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public class TTSQuestionAsker
        extends UtteranceProgressListener
        implements QuestionAsker, TextToSpeech.OnInitListener {

    public enum QuestionAskerState { IDLE, ASKING_QUESTION, OFFERING_HELP, SKIPPING }

    private Random rnd = new Random();
    private String[][] help;

    private TextToSpeech tts;
    private QuestionToTextConverter qtt;
    private QuestionAskedListener questionAskedListener;
    private QuestionAskerState state = QuestionAskerState.IDLE;
    private Question currQ;
    private boolean ttsInitialized = false;
    private boolean interruptedOnPause = false;

    public TTSQuestionAsker(Context context, QuestionToTextConverter qtt) {
        this(context, qtt, null);
    }

    public TTSQuestionAsker(Context context,
                            QuestionToTextConverter qtt,
                            QuestionAskedListener questionAskedListener) {
        this.questionAskedListener = questionAskedListener;
        this.qtt = qtt;
        tts = new TextToSpeech(context, this);
        help = new String[][]{
                {str(R.string.help_1_1), str(R.string.help_1_2)},
                {str(R.string.help_2_1), str(R.string.help_2_2)},
                {str(R.string.help_3_1), str(R.string.help_3_2)}
        };
    }

    @Override
    public void askQuestion(Question question) {
        state = QuestionAskerState.ASKING_QUESTION;
        this.currQ = question;

        if (ttsInitialized) {
            sayCurrentQuestion();
        }
    }

    @Override
    public void offerHelp(HelpLevel helpLevel) {
        state = QuestionAskerState.OFFERING_HELP;
        tts.speak(
                String.format("%s...", help[helpLevel.I][rnd.nextInt(help[helpLevel.I].length)]),
                TextToSpeech.QUEUE_FLUSH, null);
        sayCurrentQuestion();
    }

    @Override
    public void skipQuestion() {
        tts.speak(str(R.string.skipQuestion), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void setQuestionAskedListener(QuestionAskedListener questionAskedListener) {
        this.questionAskedListener = questionAskedListener;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            ttsInitialized = true;
            tts.setSpeechRate(0.8f);
            tts.setOnUtteranceProgressListener(this);
            sayCurrentQuestion();
        }
    }

    private void sayCurrentQuestion() {
        if (currQ != null) {
            tts.speak(qtt.getSpeechForQuestion(currQ), TextToSpeech.QUEUE_ADD, utteranceIdHashMapForQuestion(currQ));
        }
    }

    private HashMap<String, String> utteranceIdHashMapForQuestion(Question question) {
        HashMap<String, String> bundle = new HashMap<>();
        bundle.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, question.getQText());
        return bundle;
    }

    @Override
    public void pause() {
        interruptedOnPause = tts.isSpeaking();
        tts.stop();
    }

    @Override
    public void resume() {
        if (interruptedOnPause) {
            sayCurrentQuestion();
        }
        interruptedOnPause = false;
    }

    @Override
    public void cancel() {
        tts.stop();
        interruptedOnPause = false;
        currQ = null;
    }

    @Override
    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    // UtteranceProgressListener -----------------------------------

    @Override
    public void onDone(String utteranceId) {
        switch (state) {
            case ASKING_QUESTION:
                questionAskedListener.onFinishedAskingQuestion(currQ);
                break;
            case OFFERING_HELP:
                questionAskedListener.onFinishedOfferingHelp();
                break;
        }
        state = QuestionAskerState.IDLE;
    }

    @Override
    public void onStart(String s) { }
    @Override
    public void onError(String s) { }
}