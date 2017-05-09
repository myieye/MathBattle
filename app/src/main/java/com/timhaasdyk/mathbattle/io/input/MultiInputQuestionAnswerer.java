package com.timhaasdyk.mathbattle.io.input;

import com.timhaasdyk.mathbattle.io.*;
import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.models.Question;

import java.util.List;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public class MultiInputQuestionAnswerer implements QuestionAnswerer, OnAnswerInputListener, OnPlayerInputListener {

    private QuestionAnsweredListener questionAnsweredListener;
    private Player answeringPlayer;
    private Question currQ;
    private List<PlayerRecognizer> playerRecognizers;
    private List<AnswerRecognizer> answerRecognizers;

    public MultiInputQuestionAnswerer() {
        this(null);
    }

    public MultiInputQuestionAnswerer(QuestionAnsweredListener questionAnsweredListener) {
        this.questionAnsweredListener = questionAnsweredListener;
    }

    @Override
    public void answerQuestion(Question question) {
        currQ = question;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        questionAnsweredListener.onFinishedAnsweringQuestion(question);
    }

    @Override
    public void setQuestionAnsweredListener(QuestionAnsweredListener questionAnsweredListener) {
        this.questionAnsweredListener = questionAnsweredListener;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onAnswer(String answer) {
        if (answeringPlayer != null && currQ.attemptAnswer(answeringPlayer, answer)) {
            questionAnsweredListener.onFinishedAnsweringQuestion(currQ);
        }
    }

    @Override
    public void onPlayer(Player player) {
        this.answeringPlayer = player;
    }
}
