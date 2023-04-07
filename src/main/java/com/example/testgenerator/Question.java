package com.example.testgenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public abstract class Question {

    protected HashSet<String> SubquestionTypes = new HashSet<String>();
    protected Utils utils = new Utils();
    protected String date;

    public Question() {
        SubquestionTypes.add("BASE");
        SubquestionTypes.add("REVERSE");
        SubquestionTypes.add("PAIRS");
    }

    /**
     * Name of the question
     *
     * @return  Question name
     */
    public abstract String getName();

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    /**
     * Defines HTML question header
     *
     * @return  Question header as string
     */
    public abstract String getHeader();

    /**
     * Gets the names of all the subquestions
     *
     * @return  The subquestion types
     */
    public HashSet<String> getAllSubquestionTypes() {
        return SubquestionTypes;
    }

    /**
     * Returns a block of multiple subquestions (by the amount specified)
     *
     * @param num   The number of subquestions to generate
     * @return      A block of subquestions
     */
    public abstract String getSubQuestions(int num);

    /**
     * Returns the question variable (the thing that makes the questions unique)
     *
     * @return  The variable as a string
     */
    public abstract String getQuestionVariable();

    /**
     * Returns correct input from string or int answer
     *
     * Implement below structure for string or int as appropriate:
     * if (obj instanceof String) {
     *       String str = (String) obj;
     *       // Implementation
     *     } else {
     *       throw new IllegalArgumentException("Expected String argument, but got: " + obj.getClass().getName());
     *     }
     * }
     *
     * @param  input    An Object parameter means that it can accept any type of object as its argument.
     *                  This includes String and int values, as both String and int are subclasses of the Object class.
     * @return          This is the correct reverse answer as a string
     */
    public abstract String getCorrectAnswer(Object input);

    /**
     * Generates incorrect answer
     *
     * @param answer    The answer to the question as a String
     * @return          Returns a random wrong answer
     */
    public abstract String getWrongAnswer(String answer);

    /**
     * Generates incorrect answer for reverse question
     *
     * @param answer    The answer to the question as a String
     * @return          Returns a random wrong answer
     */
    public abstract String getReverseWrongAnswer(String answer);

    /**
     * Generates specific subquestions
     *
     * @param type      The type of subquestion
     * @param num       The number of questions of that type to generate
     * @return          Returns the subquestion/s specified
     */
    public abstract String getSpecificSubQuestion(String type, int num) throws IllegalArgumentException;

    /**
     * Generates the base subquestion
     *
     * @param question      The question variable
     * @param qNum          The question number
     * @param marks         The number of marks the question is worth
     * @param questionText  The wording of the question
     * @param type          The type of subquestion
     * @return              Returns the base subquestion
     */
    public String setupDefaultSubquestion(String question, int qNum, int marks, String questionText, String type) throws IllegalArgumentException {
        if (!SubquestionTypes.contains(type)) {
            throw new IllegalArgumentException("Invalid Subquestion");
        }

        String answer = getCorrectAnswer(question);
        String questionWording = qNum + ". " + this.getName() + ": " + questionText;
        if (type == "REVERSE") {
            answer = question;
            question = getCorrectAnswer(question);
        }

        int ansLoc = utils.genRandomRange(0, 4); //which multiple choice the answer is in
        String[] qSet = generateMultipleChoices(question, answer, ansLoc, type);
        if (type != "PAIRS") {
            questionWording += question;
        }
        questionWording += "?";

        if (utils.testNotUnique(qSet)) {
            System.out.println("***ERROR Qset Checksum simple numbers Not Unique");
        }

        return generateSubquestion(qNum, marks, questionWording, qSet, ansLoc);
    }

    /**
     * Method that generates subquestion text
     *
     * @param qNum          The question number
     * @param marks         The number of marks the question is worth
     * @param question      The wording of the full question
     * @param qSet          The multiple choice options
     * @param ansLoc        The location of the answer
     * @return              The subquestion text
     */
    public String generateSubquestion(int qNum, int marks, String question, String[] qSet, int ansLoc) {
        String qText = "Title: Question " + qNum + "\n";
        qText += "Points: " + marks + "\n";
        qText += question;
        for (int i = 0; i < qSet.length; i++) {
            qText += "\n";
            if (i == ansLoc) {
                qText += "*";
                //Sanity Check
                //utils.checkEquals(reducedStr, qSet[i]);
            } else {
                //Sanity Check
                //utils.checkNotEquals(reducedStr, qSet[i]);
            }
            char id = (char) (i + 97);
            qText += id + ") " + qSet[i];
        }
        return qText;
    }

    /**
     * Generates the multiple choice options
     *
     * @param question  The question variable as a string
     * @param answer    The answer as a string
     * @param ansLoc    The location of the answer in the multiple choice options
     * @param type      The type of subquestion
     * @return          The multiple choice options
     */
    public String[] generateMultipleChoices(String question, String answer, int ansLoc, String type) throws IllegalArgumentException {
        if (!SubquestionTypes.contains(type)) {
            throw new IllegalArgumentException("Invalid Subquestion");
        }

        String[] qSet = new String[5];

        do {
            for (int i = 1; i < qSet.length; i++) {
                switch(type) {
                    case "BASE":
                        qSet[0] = answer;
                        qSet[i] = getWrongAnswer(answer);
                        break;
                    case "REVERSE":
                        qSet[0] = answer;
                        qSet[i] = getReverseWrongAnswer(answer);
                        break;
                    case "PAIRS":
                        qSet[0] = question + " " + answer;
                        String mergeString = question + " " + answer;
                        qSet[i] = getReverseWrongAnswer(question) +  " " + getWrongAnswer(answer);
                        utils.checkNotEquals(mergeString, qSet[i]);
                }
            }
        } while (utils.testNotUnique(qSet));

        qSet = utils.reorderQSet(qSet, ansLoc, 1);

        return qSet;
    }

    // Generates the header in the file
    public void genHeader(FileWriter outFile) throws IOException {
        outFile.write(getHeader());
    }

    // Generates the subquestions in the file
    public void genSubQuestions(FileWriter outFile, int num) throws IOException {
        outFile.write(getSubQuestions(num));
    }
}
