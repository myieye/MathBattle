package com.timhaasdyk.mathbattle.io;

import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public interface QuestionAsker {

    void askQuestion(Question question);

    void setQuestionAskedListener(QuestionAskedListener questionAskedListener);

    void pause();

    void resume();

    void destroy();
}