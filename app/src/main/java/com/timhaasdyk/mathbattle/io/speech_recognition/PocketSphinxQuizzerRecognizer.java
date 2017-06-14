package com.timhaasdyk.mathbattle.io.speech_recognition;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.io.*;
import com.timhaasdyk.mathbattle.io.speech_recognition.norm.SpeechNormalizer;
import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.logic.impl.PlayerAdmin;
import edu.cmu.pocketsphinx.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

import static com.timhaasdyk.mathbattle.util.ResourceUtil.str;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class PocketSphinxQuizzerRecognizer
        implements RecognitionListener, PlayerAnswerRecognizer, StartRecognizer {

    public enum Search { NUMBER, START }

    private Search currSearch = Search.START;

    private SpeechRecognizer recognizer;
    private final Context context;
    private final PlayerAdmin playerAdmin;
    private final SpeechNormalizer speechNormalizer;
    private long startSpeechTime = 0;

    private String startKeyphrase;

    public PocketSphinxQuizzerRecognizer(Context context,
                                         OnRecognizerInitListener onRecognizerInitListener,
                                         PlayerAdmin playerAdmin,
                                         SpeechNormalizer speechNormalizer) {
        this.context = context;
        this.playerAdmin = playerAdmin;
        this.speechNormalizer = speechNormalizer;
        this.startKeyphrase = str(R.string.start_keyphrase).toLowerCase();
        init(onRecognizerInitListener);
    }

    private void init(final OnRecognizerInitListener onRecognizerInitListener) {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(context);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(new File(assetDir, context.getString(R.string.locale)));
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    onRecognizerInitListener.onRecognizerInitFailed();
                } else {
                    onRecognizerInitListener.onRecognizerInit();
                }
            }
        }.execute();
    }

    public void setSearch(Search search) {
        recognizer.stop();
        currSearch = search;
    }

    private void setupRecognizer(File assetDir) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetDir, context.getString(R.string.acoustic_model_folder)))
                .setDictionary(new File(assetDir, context.getString(R.string.dictionary_file)))
                .setKeywordThreshold(Double.valueOf(str(R.string.start_threshold)).floatValue())
                .getRecognizer();
        recognizer.addListener(this);

        File numberGrammar = new File(assetDir, context.getString(R.string.number_grammar_file));
        recognizer.addGrammarSearch(Search.NUMBER.name(), numberGrammar);

        Log.d("RECOG", "Start Keyphrase: " + startKeyphrase);
        recognizer.addKeyphraseSearch(Search.START.name(), startKeyphrase);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("RECOG", "onBeginningOfSpeech");
        startSpeechTime = System.currentTimeMillis();
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("RECOG", "onEndOfSpeech");
        startSpeechTime = 0;
        reset();
    }

    private void reset() {
        recognizer.stop();
        startRecognizing();
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        processHypothesis(hypothesis, false);
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        processHypothesis(hypothesis, true);
    }

    private void processHypothesis(Hypothesis hypothesis, boolean finalResult) {
        if (hypothesis == null || StringUtils.isBlank(hypothesis.getHypstr()))
            return;

        String input = hypothesis.getHypstr();
        Log.d("RECOG", "Hypothesis: " + input);

        if (input.equalsIgnoreCase(startKeyphrase)) {
            if (startListener != null) startListener.onStartRecognized();
            return;
        }

        String firstWord = StringUtils.substringBefore(input, " ");

        if (playerAdmin.playerExists(firstWord)) {
            input = StringUtils.substringAfter(input, " ");
            Player player = playerAdmin.getPlayerByTag(firstWord);
            if (StringUtils.isNotBlank(input)) {
                Log.d("RECOG", "onPlayerAnswer");
                playerAnswerListener.onPlayerAnswer(player, speechNormalizer.normalize(input), startSpeechTime, finalResult);
            } else {
                playerAnswerListener.onPlayer(player, startSpeechTime);
            }
        } else {
            Log.d("RECOG", "onAnswer");
            playerAnswerListener.onAnswer(speechNormalizer.normalize(input), finalResult);
        }
    }

    @Override
    public void onError(Exception error) { }

    @Override
    public void onTimeout() { }

    @Override
    public void destroy() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }


    // OnInputListener ------------------------------

    private OnPlayerAnswerInputListener playerAnswerListener;
    private OnStartListener startListener;

    @Override
    public void startRecognizing() {
        Log.d("RECOG", "Starting Sphinx Recognizer");
        recognizer.startListening(currSearch.name());
    }

    @Override
    public void stopRecognizing() {
        recognizer.cancel();
    }

    @Override
    public boolean isReceiving() {
        return recognizer != null && recognizer.getDecoder().getInSpeech();
    }

    @Override
    public void setOnPlayerAnswerRecognizedListener(OnPlayerAnswerInputListener onPlayerAnswerInputListener) {
        this.playerAnswerListener = onPlayerAnswerInputListener;
    }

    @Override
    public void setOnStartRecognizedListener(OnStartListener onStartListener) {
        this.startListener = onStartListener;
    }
}
