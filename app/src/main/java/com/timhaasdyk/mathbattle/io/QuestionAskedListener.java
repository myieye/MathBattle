package com.timhaasdyk.mathbattle.io;

import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public interface QuestionAskedListener {
    void onFinishedAskingQuestion(Question question);

    void onFinishedOfferingHelp();
}
