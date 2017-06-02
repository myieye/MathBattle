package com.timhaasdyk.mathbattle.io.speech_recognition.norm;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * @author Tim Haasdyk on 16-May-17.
 */
public class MathSpeechNormalizer extends SpeechNormalizer {

    @Override
    public String normalize(String input) {
        Locale locale = Locale.getDefault();
        if (englishLocale(locale)) {
            input = StringUtils.replaceIgnoreCase(input, "negative", "minus");
            input = StringUtils.replaceIgnoreCase(input, " comma", " point");
            input = StringUtils.replaceIgnoreCase(input, "-", " ");
            return StringUtils.replaceIgnoreCase(input, " and ", " ");
        }
        if (germanLocale(locale)) {
            input = StringUtils.replaceIgnoreCase(input, "-", "");
            input = StringUtils.replaceIgnoreCase(input, " ", "");
            return StringUtils.removeAll(input, " ");
        } else {
            return input;
        }
    }
}