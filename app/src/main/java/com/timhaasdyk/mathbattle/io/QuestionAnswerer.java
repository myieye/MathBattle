package com.timhaasdyk.mathbattle.io;

import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public interface QuestionAnswerer {

    void answerQuestion(Question question);

    void setQuestionAnsweredListener(QuestionAnsweredListener questionAnsweredListener);

    void pause();

    void resume();

    void destroy();

    void cancel();
}