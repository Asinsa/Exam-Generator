package com.example.testgenerator;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private static HashMap<String, Question> allQuestions = new HashMap<String, Question>();;

    private static List<Q> QUESTION_LIST;

    public void update() {
        QUESTION_LIST = createQuestionList();
    }

    private static List<Q> createQuestionList() {
        List<Q> questionList = new ArrayList<>();

        int qID = 1;
        int count = 0;
        for (Question question : allQuestions.values()) {
            questionList.add(new Q(qID, question.getName(), null));
            count = questionList.size()-1;
            int subqID = 1;
            for (String subquestion : question.getAllSubquestionTypes()) {
                questionList.add(new Q((qID*10)+subqID, question.getName()+" - "+subquestion, questionList.get(count)));
                subqID++;
            }
            qID++;
        }

        return questionList;
    }

    public List<Q> getQuestions() {
        return QUESTION_LIST;
    }

    public List<Q> getRootQuestions() {
        return QUESTION_LIST
                .stream()
                .filter(department -> department.getParent() == null)
                .collect(Collectors.toList());
    }

    public List<Q> getChildQuestions(Q parent) {
        return QUESTION_LIST
                .stream()
                .filter(department -> Objects.equals(department.getParent(), parent))
                .collect(Collectors.toList());
    }


    public void addQuestion(String name, Question newQuestion) {
        // If there are no questions or question exists under a different name remove it and replace with new name
        if ((!allQuestions.isEmpty()) && (allQuestions.containsValue(newQuestion))){
            for (Map.Entry<String, Question> entry : allQuestions.entrySet()) {
                if (Objects.equals(newQuestion, entry.getValue())) {
                    String existingKey = entry.getKey();
                    allQuestions.remove(existingKey);
                }
            }
        }
        allQuestions.put(name, newQuestion);
    }

    public void removeQuestion(String name) {
        allQuestions.remove(name);
    }

    public Question findQuestion(String name) {
        return allQuestions.get(name);
    }

    public Collection<Question> getAllQuestions() {
        if (allQuestions.isEmpty()) {
            return null;
        }
        return allQuestions.values();
    }

    public Collection<Question> getEmpty() {
        HashMap<String, Question> q = new HashMap<String, Question>();
        return q.values();
    }
}
