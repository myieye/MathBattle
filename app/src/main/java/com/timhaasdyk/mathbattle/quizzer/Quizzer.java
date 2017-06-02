package com.timhaasdyk.mathbattle.quizzer;

import com.timhaasdyk.mathbattle.exceptions.QuizAlreadyFinishedException;
import com.timhaasdyk.mathbattle.io.QuestionAnsweredListener;
import com.timhaasdyk.mathbattle.io.QuestionAnswerer;
import com.timhaasdyk.mathbattle.io.QuestionAskedListener;
import com.timhaasdyk.mathbattle.io.QuestionAsker;
import com.timhaasdyk.mathbattle.io.sounds.SoundPlayer;
import com.timhaasdyk.mathbattle.io.tts.HelpLevel;
import com.timhaasdyk.mathbattle.io.tts.HelpType;
import com.timhaasdyk.mathbattle.models.Question;
import com.timhaasdyk.mathbattle.models.QuestionStatus;
import com.timhaasdyk.mathbattle.models.Quiz;

import static com.timhaasdyk.mathbattle.ui.util.DelayUtil.delay;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class Quizzer implements QuestionAskedListener, QuestionAnsweredListener {

    private enum QuizzerState { UNSTARTED, FINISHED, CANCELED,
        WAITING_FOR_QUESTION_ASKED, WAITING_FOR_QUESTION_ANSWERED }

    private Quiz quiz;
    private final QuizEventsListener quizEventsListener;
    private final QuestionAsker questionAsker;
    private final QuestionAnswerer questionAnswerer;
    private final SoundPlayer soundPlayer;
    private QuizzerState state = QuizzerState.UNSTARTED;

    private HelpLevel helpLevelTime;
    private HelpLevel helpLevelWrong;

    public Quizzer(QuizEventsListener quizEventsListener,
                   QuestionAsker questionAsker,
                   QuestionAnswerer questionAnswerer,
                   SoundPlayer soundPlayer) {
        this.quizEventsListener = quizEventsListener;
        this.questionAsker = questionAsker;
        this.questionAnswerer = questionAnswerer;
        this.soundPlayer = soundPlayer;

        questionAsker.setQuestionAskedListener(this);
        questionAnswerer.setQuestionAnsweredListener(this);
    }

    public void doQuiz(Quiz quiz) {
        this.quiz = quiz;
        state = QuizzerState.UNSTARTED;
        if (quizIsFinished()) {
            throw new QuizAlreadyFinishedException("Null or finished Quiz passed to doQuiz");
        }
        askNextQuestion();
    }

    private boolean quizIsFinished() {
        boolean finished = quiz == null || quiz.finished();
        if (finished) {
            state = QuizzerState.FINISHED;
            quizEventsListener.onQuizFinished(quiz);
            soundPlayer.playQuizFinished();
        }
        return finished;
    }

    private void askNextQuestion() {
        helpLevelTime = HelpLevel.ONE;
        questionAsker.askQuestion(quiz.nextQuestion());
        state = QuizzerState.WAITING_FOR_QUESTION_ASKED;
    }

    private void skipToNextQuestion() {
        if (!quizIsFinished()) {
            questionAsker.skipQuestion();
            askNextQuestion();
        }
    }

    @Override
    public void onFinishedAskingQuestion(Question question) {
        if (!state.equals(QuizzerState.CANCELED)) {
            questionAnswerer.answerQuestion(question);
            state = QuizzerState.WAITING_FOR_QUESTION_ANSWERED;
        }
    }

    @Override
    public void onFinishedOfferingHelp() {
        questionAnswerer.resume();
    }

    @Override
    public void onFinishedAnsweringQuestion(Question question) {
        questionAsker.cancel();
        questionAnswerer.pause();

        if (question.getStatus().equals(QuestionStatus.ANSWERED)) {
            quizEventsListener.onPlayerAnsweredQuestion(question.getAnsweredBy());
            soundPlayer.playCorrectAnswer();
        }
        if (!(quizIsFinished() || state.equals(QuizzerState.CANCELED))) {
            delay(new Runnable(){ public void run() { askNextQuestion(); } }, 1000);
        }
    }

    @Override
    public void onNeedsHelp(HelpType helpType) {
        questionAnswerer.pause();

        switch(helpType) {
            case TIME:
                if (helpLevelTime.isMax()) {
                    skipToNextQuestion();
                    break;
                }
                questionAsker.offerHelp(helpLevelTime);
                helpLevelTime = helpLevelTime.getNextLevel();
                break;
            case WRONG:
                if (helpLevelWrong.isMax()) {
                    skipToNextQuestion();
                    break;
                }
                questionAsker.offerHelp(helpLevelWrong);
                helpLevelWrong = helpLevelWrong.getNextLevel();
                break;
        }
    }

    public void pause() {
        questionAsker.pause();
        questionAnswerer.pause();
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

    public void cancel() {
        questionAsker.cancel();
        questionAnswerer.cancel();
        state = QuizzerState.CANCELED;
    }

    public void destroy() {
        questionAsker.destroy();
        questionAnswerer.destroy();
    }
}