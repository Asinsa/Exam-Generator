package com.example.testgenerator.questions;

import com.example.testgenerator.Question;

public class TestFileB extends Question {

    public TestFileB() {
        SubquestionTypes.add("TEST-B1");
        SubquestionTypes.add("TEST-B2");
        SubquestionTypes.add("TEST-B3");
    }

    @Override
    public String getName() {
        return "Test Question B";
    }

    @Override
    public String getHeader() {
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