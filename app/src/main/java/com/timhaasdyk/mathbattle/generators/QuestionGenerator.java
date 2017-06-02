package com.timhaasdyk.mathbattle.generators;

import com.timhaasdyk.mathbattle.io.speech_recognition.norm.SpeechNormalizer;
import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public abstract class QuestionGenerator {

    private int level;
    protected SpeechNormalizer speechNormalizer;

    public QuestionGenerator(int level, SpeechNormalizer speechNormalizer) {
        this.level = level;
        this.speechNormalizer = speechNormalizer;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public abstract Question generateQuestion();
}