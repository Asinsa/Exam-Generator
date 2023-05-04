package com.example.testgenerator;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Class to manage the questions availible in the application
 */
@Service
public class QuestionService {

    private static HashMap<String, Question> allQuestions = new HashMap<String, Question>();
    private static List<Q> QUESTION_LIST;
    private static List<Q> CHOSEN_QUESTION_LIST = new ArrayList<>();

    /**
     * Method that updates the QUESTION_LIST with questions and subquestions of type Q
     */
    public void update() {
        QUESTION_LIST = createQuestionList();
    }

    /**
     * Method that creates and returns QUESTION_LIST using the questions availible
     *
     * @return the list of questions and subquestions as Q objects that can go into a TreeGrid
     */
    private static List<Q> createQuestionList() {
        List<Q> questionList = new ArrayList<>();

        int id = 0;
        int count = 0;
        for (Question question : allQuestions.values()) {
            questionList.add(new Q(id, question.getName(), null));
            count = questionList.size() - 1;
            id++;
            for (String subquestion : question.getAllSubquestionTypes()) {
                questionList.add(new Q(id, question.getName() + " - " + subquestion, questionList.get(count)));
                id++;
            }
        }

        return questionList;
    }

    /**
     * Method to get the list of all questions and subquestions as Q objects
     *
     * @return the list of questions and subquestions as Q objects that can go into a TreeGrid.
     */
    public List<Q> getQuestions() {
        return QUESTION_LIST;
    }

    /**
     * Method to get the questions as Q objects.
     *
     * @return the list of questions as Q objects that can go into a TreeGrid.
     */
    public List<Q> getRootQuestions() {
        return QUESTION_LIST
                .stream()
                .filter(department -> department.getParent() == null)
                .collect(Collectors.toList());
    }

    /**
     * Method to get the subquestions of a specific question as Q objects.
     *
     * @param parent    the question to get the subquestions from.
     * @return the list of subquestions as Q objects that can go into a TreeGrid.
     */
    public List<Q> getChildQuestions(Q parent) {
        return QUESTION_LIST
                .stream()
                .filter(department -> Objects.equals(department.getParent(), parent))
                .collect(Collectors.toList());
    }

    /**
     * Method to set the chosen questions and subquestions when the user has picked them.
     *
     * @param allChosenQuestions    The questions and subquestions chosen by the user.
     */
    public void setChosenQuestions(List<Q> allChosenQuestions) {
        this.CHOSEN_QUESTION_LIST = allChosenQuestions;
    }

    /**
     * Method to get the chosen questions.
     *
     * @return the questions and subquestions chosen by the user.
     */
    public List<Q> getChosenQuestions() {
        return CHOSEN_QUESTION_LIST;
    }

    /**
     * Method to add question objects to the allQuestions list.
     *
     * @param name          The name of the question.
     * @param newQuestion   The question itself.
     */
    public void addQuestion(String name, Question newQuestion) {
        // If there are no questions or question exists under a different name remove it and replace with new name
        if ((!allQuestions.isEmpty()) && (allQuestions.containsValue(newQuestion))) {
            for (Map.Entry<String, Question> entry : allQuestions.entrySet()) {
                if (Objects.equals(newQuestion, entry.getValue())) {
                    String existingKey = entry.getKey();
                    allQuestions.remove(existingKey);
                }
            }
        }
        allQuestions.put(name, newQuestion);
    }

    /**
     * Method to remove a question object from the allQuestions list.
     *
     * @param name  The name of the question object to be removed.
     */
    public void removeQuestion(String name) {
        allQuestions.remove(name);
    }

    /**
     * Method to find a specific question object by name.
     *
     * @param name  The name of the question object to be found.
     * @return the question object.
     */
    public Question findQuestion(String name) {
        return allQuestions.get(name);
    }
}
