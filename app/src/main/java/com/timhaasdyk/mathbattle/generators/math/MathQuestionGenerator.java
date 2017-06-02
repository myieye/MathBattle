package com.timhaasdyk.mathbattle.generators.math;

import com.timhaasdyk.mathbattle.generators.QuestionGenerator;
import com.timhaasdyk.mathbattle.io.speech_recognition.norm.MathSpeechNormalizer;
import com.timhaasdyk.mathbattle.models.Question;
import com.timhaasdyk.mathbattle.models.math.MathQuestion;
import com.timhaasdyk.mathbattle.util.NumberConvertor;
import com.udojava.evalex.Expression;

import java.util.Random;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class MathQuestionGenerator extends QuestionGenerator {

    private Random rand;
    private String operators = "-+*";

    private int ops;
    private int maxA;
    private int maxB;

    public MathQuestionGenerator(int level) {
        this(level, new Random());
    }

    public MathQuestionGenerator(int level, Random rand) {
        super(level, new MathSpeechNormalizer());
        this.rand = rand;

        switch(level) {
            case 1:
                ops = 2;
                maxA = 7;
                maxB = 3;
                break;
            case 2:
                ops = 2;
                maxA = 15;
                maxB = 7;
                break;
            case 3:
                ops = 3;
                maxA = 15;
                maxB = 15;
                break;
        }
    }

    @Override
    public Question generateQuestion() {
        int a = rand.nextInt(maxA) + 1;
        int b = rand.nextInt(maxB) + 1;
        int operator = rand.nextInt(ops);
        String text = "" + a + operators.charAt(operator) + b;

        Number answer = new Expression(text).eval().doubleValue();
        String speechAnswer = speechNormalizer.normalize(NumberConvertor.getTextForNumber(answer));

        return new MathQuestion(getLevel(), text, answer, speechAnswer);
    }
}