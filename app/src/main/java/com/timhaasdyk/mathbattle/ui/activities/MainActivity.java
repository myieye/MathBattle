package com.timhaasdyk.mathbattle.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.constants.PlayerStore;
import com.timhaasdyk.mathbattle.generators.math.MathQuestionGenerator;
import com.timhaasdyk.mathbattle.io.*;
import com.timhaasdyk.mathbattle.io.input.MultiInputQuestionAnswerer;
import com.timhaasdyk.mathbattle.io.sounds.SoundPlayer;
import com.timhaasdyk.mathbattle.io.speech_recognition.PocketSphinxQuizzerRecognizer;
import com.timhaasdyk.mathbattle.io.speech_recognition.norm.MathSpeechNormalizer;
import com.timhaasdyk.mathbattle.io.tts.TTSQuestionAsker;
import com.timhaasdyk.mathbattle.io.tts.qtt.MathQuestionToTextConverter;
import com.timhaasdyk.mathbattle.logic.QuizProcessor;
import com.timhaasdyk.mathbattle.logic.impl.QuizProcessorImpl;
import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.models.PlayerResult;
import com.timhaasdyk.mathbattle.models.PlayerTag;
import com.timhaasdyk.mathbattle.models.Quiz;
import com.timhaasdyk.mathbattle.quizzer.PlayerAdmin;
import com.timhaasdyk.mathbattle.quizzer.PlayerPosition;
import com.timhaasdyk.mathbattle.quizzer.QuizEventsListener;
import com.timhaasdyk.mathbattle.quizzer.Quizzer;
import com.timhaasdyk.mathbattle.ui.adapters.PlayerSelectionAdapter;
import com.timhaasdyk.mathbattle.ui.adapters.ResultListAdapter;
import com.timhaasdyk.mathbattle.ui.util.PlayerButtonUtil;
import com.timhaasdyk.mathbattle.util.ResourceUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.timhaasdyk.mathbattle.ui.util.DelayUtil.delay;
import static com.timhaasdyk.mathbattle.util.ResourceUtil.str;

public class MainActivity extends AppCompatActivity
        implements PlayerAnswerRecognizer, QuizEventsListener, OnRecognizerInitListener,
        ActivePlayerListener, SoundPool.OnLoadCompleteListener, OnStartListener {

    private Quizzer quizzer;
    private PlayerAdmin playerAdmin;
    private QuizProcessor quizProcessor;
    private PocketSphinxQuizzerRecognizer sphinxRecognizer;

    private boolean recognizerReady = false;
    private boolean soundPoolReady = false;
    private List<Player> currPlayers;
    private boolean inQuiz = false;

    private int level = 1;
    private int numQuestions = 10;

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        ResourceUtil.init(getResources());
        setContentView(R.layout.activity_main);

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    protected void onStart() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onStart();
        PlayerButtonUtil.init(this);

        SoundPlayer soundPlayer = SoundPlayer.defaultSoundPlayer(this);
        soundPlayer.getSoundPool().setOnLoadCompleteListener(this);

        currPlayers = PlayerStore.getInstance().getDefaultPlayers();
        playerAdmin = new PlayerAdmin((ViewGroup)findViewById(R.id.buttonGroup));
        quizProcessor = new QuizProcessorImpl();

        QuestionAsker questionAsker = new TTSQuestionAsker(this, new MathQuestionToTextConverter(getResources()));
        sphinxRecognizer = new PocketSphinxQuizzerRecognizer(this, this, playerAdmin, new MathSpeechNormalizer());
        sphinxRecognizer.setOnStartRecognizedListener(this);
        List<PlayerAnswerRecognizer> recognizers = Arrays.asList(this, sphinxRecognizer);
        QuestionAnswerer questionAnswerer = new MultiInputQuestionAnswerer(recognizers, this, soundPlayer);
        quizzer = new Quizzer(this, questionAsker, questionAnswerer, soundPlayer);

        PlayerButtonUtil.setTestPlayerButtons(playerAdmin, currPlayers);
    }

    public void onPlayerButtonClick(final View v) {
        if (recognizingPlayers) {
            onPlayerAnswerInputListener.onPlayer(
                    playerAdmin.getPlayerByTag((PlayerTag)v.getTag()), System.currentTimeMillis());
        } else if(!inQuiz) {
            GridView gridView = new GridView(this);

            final ArrayAdapter<Player> arrayAdapter = new PlayerSelectionAdapter(this, R.layout.player_selection_grid_item);
            arrayAdapter.addAll(currPlayers);
            gridView.setAdapter(arrayAdapter);
            gridView.setNumColumns(2);

            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(gridView)
                    .setTitle(str(R.string.choosePlayerTitle))
                    .create();
            dialog.show();

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int which, long id) {
                    PlayerPosition pos = (PlayerPosition) v.getTag(R.id.playerPosTagKey);
                    playerAdmin.setPlayer(currPlayers.get(which), pos);
                    dialog.dismiss();
                }
            });
        }
    }

    public void onStartClick(View btnStart) {
        if (!recognizerReady) {
            toast("Recognizer is not yet initialized", Toast.LENGTH_LONG);
        } else if (!soundPoolReady) {
            toast("Sound Pool is not yet initialized", Toast.LENGTH_LONG);
        } else {
            startQuiz();
        }
    }

    private void startQuiz() {
        sphinxRecognizer.stopRecognizing();
        inQuiz = true;
        toast("Starting Quiz", Toast.LENGTH_LONG);
        findViewById(R.id.startButton).setVisibility(View.INVISIBLE);
        PlayerButtonUtil.hideUnusedButtons();
        long wait = PlayerButtonUtil.shakePlayerButtons();
        final Quiz currQuiz = Quiz.generateQuiz(new MathQuestionGenerator(level), "Test Quiz", numQuestions);
        delay(new Runnable(){ public void run() {
            quizzer.doQuiz(currQuiz);
            runOnUiThread(new Runnable() { public void run() {
                findViewById(R.id.stopButton).setVisibility(View.VISIBLE); } });
            sphinxRecognizer.setSearch(PocketSphinxQuizzerRecognizer.Search.NUMBER);
        }}, wait);
    }

    private void stopQuiz() {
        inQuiz = false;
        resetUI();
    }

    public void onStopClick(View view) {
        quizzer.cancel();
        stopQuiz();
        sphinxRecognizer.setSearch(PocketSphinxQuizzerRecognizer.Search.START);
        sphinxRecognizer.startRecognizing();
    }

    private void resetUI() {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            PlayerButtonUtil.showAllButtons();
            findViewById(R.id.startButton).setVisibility(View.VISIBLE);
            findViewById(R.id.stopButton).setVisibility(View.INVISIBLE);
        } else {
            runOnUiThread(new Runnable() { public void run() { resetUI(); } });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        quizzer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        quizzer.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quizzer.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                finish();
            }
        }
    }


    // PlayerRecognizer -----------------------------------------

    private OnPlayerAnswerInputListener onPlayerAnswerInputListener;
    private boolean recognizingPlayers = false;

    @Override
    public void startRecognizing() {
        recognizingPlayers = true;
    }

    @Override
    public void stopRecognizing() {
        recognizingPlayers = false;
    }

    @Override
    public boolean isReceiving() {
        return false;
    }

    @Override
    public void destroy() { }

    @Override
    public void setOnPlayerAnswerRecognizedListener(OnPlayerAnswerInputListener onPlayerAnswerInputListener) {
        this.onPlayerAnswerInputListener = onPlayerAnswerInputListener;
    }


    // QuizEventsListener -----------------------------------------

    @Override
    public void onQuizFinished(Quiz quiz) {
        stopQuiz();
        showQuizResults(quiz);
    }

    private void showQuizResults(Quiz quiz) {
        final ArrayAdapter<PlayerResult> arrayAdapter = new ResultListAdapter(this, R.layout.player_result_list_item);
        arrayAdapter.addAll(quizProcessor.getQuizResults(quiz, playerAdmin.getPlayers()));

        final ContextThemeWrapper ctw = new ContextThemeWrapper(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = new AlertDialog.Builder(ctw)
                        .setTitle(str(R.string.resultsTitle))
                        .setPositiveButton("OK", null)
                        .setAdapter(arrayAdapter, null)
                        .create();

                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        sphinxRecognizer.setSearch(PocketSphinxQuizzerRecognizer.Search.START);
                        sphinxRecognizer.startRecognizing();
                    }
                });
            }
        });
    }

    @Override
    public void onPlayerAnsweredQuestion(Player player) {
        PlayerButtonUtil.squeezePlayerButton(player);
    }


    // RecognizerInitListener -----------------------------------

    @Override
    public void onRecognizerInit() {
        recognizerReady = true;
        toast("Recognizer ready!", Toast.LENGTH_LONG);
        sphinxRecognizer.setSearch(PocketSphinxQuizzerRecognizer.Search.START);
        sphinxRecognizer.startRecognizing();
    }

    @Override
    public void onRecognizerInitFailed() {
        toast("Recognizer init failed!", Toast.LENGTH_LONG);
    }


    // ActivePlayerListener -----------------------------------------

    @Override
    public void onActivatePlayer(Player player) {
        PlayerButtonUtil.activateButtonForPlayer(player);
    }

    @Override
    public void onDeactivatePlayer(Player player) {
        PlayerButtonUtil.deactivateButtonForPlayer(player);
    }


    // SoundPool.OnLoadCompleteListener

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
        soundPoolReady = true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(menu.findItem(R.id.btnChooseLevel)).findViewById(R.id.spnLevels);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                level = i + 1;
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        return true;
    }

    private void toast(final String text, final int toastLength) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            Toast.makeText(this, text, toastLength).show();
        } else {
            runOnUiThread(new Runnable() { public void run() { toast(text, toastLength); } });
        }
    }

    @Override
    public void onStartRecognized() {
        if (!inQuiz) startQuiz();
    }
}