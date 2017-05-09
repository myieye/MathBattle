package com.timhaasdyk.mathbattle.io.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import com.timhaasdyk.mathbattle.io.QuestionAskedListener;
import com.timhaasdyk.mathbattle.io.QuestionAsker;
import com.timhaasdyk.mathbattle.io.tts.qtt.QuestionToText;
import com.timhaasdyk.mathbattle.models.Question;

import java.util.HashMap;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public class TTSQuestionAsker
        extends UtteranceProgressListener
        implements QuestionAsker, TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private QuestionToText qtt;
    private QuestionAskedListener questionAskedListener;

    private Question currQ;
    private boolean ttsInitialized = false;
    private boolean interruptedOnPause = false;

    public TTSQuestionAsker(Context context, QuestionToText qtt) {
        this(context, qtt, null);
    }

    public TTSQuestionAsker(Context context,
                            QuestionToText qtt,
                            QuestionAskedListener questionAskedListener) {
        this.questionAskedListener = questionAskedListener;
        this.qtt = qtt;
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void askQuestion(Question question) {
        this.currQ = question;

        if (ttsInitialized) {
            sayCurrentQuestion();
        }
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
        bundle.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, question.getText());
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
    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    // UtteranceProgressListener -----------------------------------

    @Override
    public void onDone(String utteranceId) {
        questionAskedListener.onFinishedAskingQuestion(currQ);
    }

    @Override
    public void onStart(String s) { }
    @Override
    public void onError(String s) { }
}
