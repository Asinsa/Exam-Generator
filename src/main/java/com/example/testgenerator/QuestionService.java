package com.example.testgenerator;

import com.example.testgenerator.questions.CheckSum;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class QuestionService {

    private static HashMap<String, Question> allQuestions = new HashMap<String, Question>();;

    public QuestionService() {}

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

    public Collection<Question> getDummyQuestions() {
        HashMap<String, Question> q = new HashMap<String, Question>();
        Question qq = new CheckSum();
        q.put("hello", qq);
        q.put("hgjjjyj", qq);
        q.put("tssesre", qq);
        return q.values();
    }
}
