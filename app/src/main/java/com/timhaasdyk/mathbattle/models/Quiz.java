package com.timhaasdyk.mathbattle.models;

import com.timhaasdyk.mathbattle.generators.QuestionGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class Quiz {

    private String name;
    private List<Question> questions;
    private int nextQ;

    private Quiz(String name) {
        this.name = name;
        questions = new ArrayList<>();
        nextQ = 0;
    }

    public static Quiz generateQuiz(QuestionGenerator generator, String name, int numQuestions)  {
        Quiz quiz = new Quiz(name);

        for (int i = 0; i < numQuestions; i++) {
            quiz.questions.add(generator.generateQuestion());
        }

        return quiz;
    }

    public Question nextQuestion() {
        if (!finished()) {
            return questions.get(nextQ++);
        }
        return null;
    }

    public boolean finished() {
        return questions == null || nextQ >= questions.size();
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}