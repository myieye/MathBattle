package com.timhaasdyk.mathbattle.io.tts.qtt;

import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public class QuestionToTextConverter {

    public String getSpeechForQuestion(Question question) {
        return question.getQText();
    }

}