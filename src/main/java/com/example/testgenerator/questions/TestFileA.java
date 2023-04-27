package com.example.testgenerator.questions;

import com.example.testgenerator.Question;

public class TestFileA extends Question {

    public TestFileA() {
        SubquestionTypes.add("TEST-A1");
        SubquestionTypes.add("TEST-A2");
        SubquestionTypes.add("TEST-A3");
    }

    @Override
    public String getName() {
        return "Test Question A";
    }

    @Override
    public String getHeader() {
        return null;
    }

    @Override
    public String getSubQuestions(int num) {
        return null;
    }

    @Override
    public String getQuestionVariable() {
        return null;
    }

    @Override
    public String getCorrectAnswer(Object input) {
        return null;
    }

    @Override
    public String getWrongAnswer(String answer) {
        return null;
    }

    @Override
    public String getReverseWrongAnswer(String answer) {
        return null;
    }

    @Override
    public String getSpecificSubQuestion(String type, int num) throws IllegalArgumentException {
        return null;
    }
}