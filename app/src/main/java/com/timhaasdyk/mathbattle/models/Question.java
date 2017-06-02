package com.timhaasdyk.mathbattle.models;

import java.util.Locale;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public abstract class Question {

    private int level;
    private String qText;
    private String answer;
    private Player answeredBy = null;
    private QuestionStatus status = QuestionStatus.UNASKED;

    public Question(int level, String qText, String answer) {
        this.level = level;
        this.qText = qText;
        this.answer = answer;
    }

    public boolean attemptAnswer(Player player, String attempt) {
        if (isCorrectAnswer(attempt)) {
            answeredBy = player;
            status = QuestionStatus.ANSWERED;
            return true;
        }
        return false;
    }

    protected abstract boolean isCorrectAnswer(String attempt);

    public int getLevel() {
        return level;
    }

    public String getQText() {
        return qText;
    }

    public String getAnswer() {
        return answer;
    }

    public Player getAnsweredBy() {
        return answeredBy;
    }

    public void setAnsweredBy(Player answeredBy) {
        this.answeredBy = answeredBy;
    }

    public QuestionStatus getStatus() {
        return status;
    }

    public void setStatus(QuestionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "[%d]{%s}:{%s}", level, qText, answer);
    }
}
