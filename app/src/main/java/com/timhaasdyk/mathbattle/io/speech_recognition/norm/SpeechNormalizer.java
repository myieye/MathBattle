package com.timhaasdyk.mathbattle.io.speech_recognition.norm;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author Tim Haasdyk on 16-May-17.
 */
public abstract class SpeechNormalizer {

    private static List<Locale> germanLocales = Arrays.asList(Locale.GERMAN, Locale.GERMANY);
    private static List<Locale> englishLocales = Arrays.asList(Locale.US, Locale.UK, Locale.ENGLISH, Locale.CANADA);

    public abstract String normalize(String input);

    protected boolean germanLocale(Locale locale) {
        return germanLocales.contains(locale);
    }

    protected boolean englishLocale(Locale locale) {
        return englishLocales.contains(locale);
    }
}
