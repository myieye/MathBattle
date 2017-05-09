package com.timhaasdyk.mathbattle.quizzer;

import com.timhaasdyk.mathbattle.exceptions.QuizAlreadyFinishedException;
import com.timhaasdyk.mathbattle.io.QuestionAnsweredListener;
import com.timhaasdyk.mathbattle.io.QuestionAnswerer;
import com.timhaasdyk.mathbattle.io.QuestionAskedListener;
import com.timhaasdyk.mathbattle.io.QuestionAsker;
import com.timhaasdyk.mathbattle.models.Question;
import com.timhaasdyk.mathbattle.models.Quiz;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class Quizzer implements QuestionAskedListener, QuestionAnsweredListener {

    private enum QuizzerState { UNSTARTED, FINISHED, WAITING_FOR_QUESTION_ASKED, WAITING_FOR_QUESTION_ANSWERED }

    private Quiz quiz;
    private final QuizFinishedListener quizFinishedListener;
    private final QuestionAsker questionAsker;
    private final QuestionAnswerer questionAnswerer;
    private QuizzerState state = QuizzerState.UNSTARTED;


    public Quizzer(QuizFinishedListener quizFinishedListener,
                   QuestionAsker questionAsker,
                   QuestionAnswerer questionAnswerer) {
        this.quizFinishedListener = quizFinishedListener;
        this.questionAsker = questionAsker;
        this.questionAnswerer = questionAnswerer;

        questionAsker.setQuestionAskedListener(this);
        questionAnswerer.setQuestionAnsweredListener(this);
    }

    public void doQuiz(Quiz quiz) {
        this.quiz = quiz;
        if (checkQuizIsFinished()) {
            throw new QuizAlreadyFinishedException("Null or finished Quiz passed to doQuiz");
        }
        askNextQuestion();
    }

    private boolean checkQuizIsFinished() {
        boolean finished = quiz == null || quiz.finished();
        if (finished) {
            state = QuizzerState.FINISHED;
            quizFinishedListener.onQuizFinished(quiz);
        }
        return finished;
    }

    private void askNextQuestion() {
        questionAsker.askQuestion(quiz.nextQuestion());
        state = QuizzerState.WAITING_FOR_QUESTION_ASKED;
    }

    @Override
    public void onFinishedAskingQuestion(Question question) {
        questionAnswerer.answerQuestion(question);
        state = QuizzerState.WAITING_FOR_QUESTION_ANSWERED;
    }

    @Override
    public void onFinishedAnsweringQuestion(Question question) {
        if (!checkQuizIsFinished()) {
            askNextQuestion();
        }
    }

    public void pause() {
        switch (state) {
            case WAITING_FOR_QUESTION_ASKED:
                questionAsker.pause();
                break;
            case WAITING_FOR_QUESTION_ANSWERED:
                questionAnswerer.pause();
                break;
        }
    }

    public void resume() {
        switch (state) {
            case WAITING_FOR_QUESTION_ASKED:
                questionAsker.resume();
                break;
            case WAITING_FOR_QUESTION_ANSWERED:
                questionAnswerer.resume();
                break;
        }
    }

    public void destory() {
        questionAsker.destroy();
        questionAnswerer.destroy();
    }
}
