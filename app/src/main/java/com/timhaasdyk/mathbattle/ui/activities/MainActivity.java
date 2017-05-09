package com.timhaasdyk.mathbattle.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.timhaasdyk.mathbattle.generators.math.MathQuestionGenerator;
import com.timhaasdyk.mathbattle.io.PlayerRecognizer;
import com.timhaasdyk.mathbattle.io.QuestionAnswerer;
import com.timhaasdyk.mathbattle.io.QuestionAsker;
import com.timhaasdyk.mathbattle.io.input.MultiInputQuestionAnswerer;
import com.timhaasdyk.mathbattle.io.tts.TTSQuestionAsker;
import com.timhaasdyk.mathbattle.io.tts.qtt.MathQuestionToText;
import com.timhaasdyk.mathbattle.models.Quiz;
import com.timhaasdyk.mathbattle.quizzer.QuizFinishedListener;
import com.timhaasdyk.mathbattle.quizzer.Quizzer;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements PlayerRecognizer, QuizFinishedListener {

    private Quizzer quizzer;
    private Quiz currQuiz;

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        QuestionAsker questionAsker = new TTSQuestionAsker(this, new MathQuestionToText(getResources()));
        QuestionAnswerer questionAnswerer = new MultiInputQuestionAnswerer();
        quizzer = new Quizzer(this, questionAsker, questionAnswerer);

        /*
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.caption_text))
                .setText("Preparing the recognizer");
                */

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        currQuiz = Quiz.generateQuiz(new MathQuestionGenerator(1),  "Test Quiz", 5);
        quizzer.doQuiz(currQuiz);
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
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quizzer.destory();
    }


    // PlayerRecognizer -----------------------------------------

    @Override
    public void startRecognizing() {
        shortToast("MainActivity.startRecognizing()");
    }

    @Override
    public void stopRecognizing() {
        shortToast("MainActivity.stopRecognizing()");
    }

    // QuizFinishedListener -----------------------------------------

    @Override
    public void onQuizFinished(Quiz quiz) {
        longToast(String.format("Quiz \"%s\" finished.", quiz.getName()));
    }

    private void longToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void shortToast(String msg) {

    }
}