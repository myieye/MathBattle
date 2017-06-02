package com.timhaasdyk.mathbattle.io;

import com.timhaasdyk.mathbattle.io.tts.HelpLevel;
import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public interface QuestionAsker {

    void askQuestion(Question question);

    void offerHelp(HelpLevel helpLevel);

    void setQuestionAskedListener(QuestionAskedListener questionAskedListener);

    void pause();

    void resume();

    void cancel();

    void destroy();

    void skipQuestion();
}