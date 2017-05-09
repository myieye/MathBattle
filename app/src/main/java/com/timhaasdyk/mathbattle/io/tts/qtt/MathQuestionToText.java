package com.timhaasdyk.mathbattle.io.tts.qtt;

import android.content.res.Resources;
import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public class MathQuestionToText extends QuestionToText {

    private Resources resources;

    public MathQuestionToText(Resources resources) {
        this.resources = resources;
    }

    public String getSpeechForQuestion(Question question) {
        return convertMathSymbolsToWords(question.getText());
    }

    private String convertMathSymbolsToWords(String text) {
        return text.replace("+", resources.getString(R.string.plus))
                .replace("-", resources.getString(R.string.minus))
                .replace("*", resources.getString(R.string.times))
                .replace("/", resources.getString(R.string.divide));
    }
}
