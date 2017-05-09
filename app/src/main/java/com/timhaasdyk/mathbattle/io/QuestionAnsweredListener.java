package com.timhaasdyk.mathbattle.io;

import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public interface QuestionAnsweredListener {
    void onFinishedAnsweringQuestion(Question question);
}
