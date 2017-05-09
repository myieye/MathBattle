package com.timhaasdyk.mathbattle.models;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public abstract class Question {

    private String text;
    private String answer;
    private int level;
    private Player answeredBy = null;
    private QuestionStatus status = QuestionStatus.UNASKED;

    public Question(String text, String answer, int level) {
        this.text = text;
        this.answer = answer;
        this.level = level;
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

    public String getText() {
        return text;
    }

    public String getAnswer() {
        return answer;
    }

    public int getLevel() {
        return level;
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
}
