package com.example.testgenerator.views;

import com.example.testgenerator.Q;
import com.example.testgenerator.Question;
import com.example.testgenerator.QuestionService;
import com.example.testgenerator.Utils;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Designer generated component for the generate-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("generate-view")
@Route(value = "make-quiz", layout = MainLayout.class)
@JsModule("./generate-view.ts")
public class GenerateView extends HorizontalLayout {

    private QuestionService questionManager;
    private Utils utils = new Utils();
    private List<Q> chosenQuestions;

    private List<Question> questionList = new ArrayList<>();

    /**
     * Creates a new GenerateView.
     */
    public GenerateView(QuestionService questionManager) {
        questionManager.update();
        this.questionManager = questionManager;
        this.chosenQuestions = questionManager.getChosenQuestions();

        addChosen();

        // Layouts
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setHeightFull();

        setMargin(true);

        // Quiz name
        TextField title = new TextField();
        title.setLabel("Title Of Quiz:");
        title.setValue("Example");
        title.setClearButtonVisible(true);
        title.setPrefixComponent(VaadinIcon.PENCIL.create());
        title.setWidth("20%");

        // Number of quizzes
        IntegerField numQuizzes = new IntegerField();
        numQuizzes.setLabel("Number of quizzes to generate:");
        numQuizzes.setValue(1);
        numQuizzes.setStepButtonsVisible(true);
        numQuizzes.setMin(1);
        numQuizzes.setWidth("20%");
        numQuizzes.getStyle().set("font-size",
                "var(--lumo-font-size-s)");

        // Make Quiz Button
        Button makeQuiz = new Button("Generate");
        makeQuiz.addClickListener( e -> {
            String filename = title.getValue();

            if (fileExists(filename)) {
                if (numQuizzes.getValue() == 1) {
                    System.out.println(filename);
                    generateQuiz(filename);
                }
                else {
                    int filevar = 0;
                    for (int i = 0; i < numQuizzes.getValue(); i++) {
                        System.out.println(filename + filevar);
                        generateQuiz(filename + filevar);
                        filevar++;
                    }
                }
            } else {
                QuizGenerationView.returnError("Quiz called " + filename + " already exists, please choose another name!");
                title.setValue("");
            }
        });

        add(mainLayout);
        mainLayout.add(title, numQuizzes, makeQuiz);

    }

    private boolean fileExists(String filename) {
        File[] files = new File("src/papers").listFiles();

        int copies = 0;
        for (File file : files) {
            if (file.isFile()) {
                if (filename.equals(file.getName())) {
                    copies++;
                }
            }
        }

        if (copies == 0) {
            return false;
        }
        return true;
    }

    private void addChosen() {
        Question newQuestion = null;
        ArrayList<String> subquestions = new ArrayList<>();
        for (Q question : chosenQuestions) {
            if (question.getParent() == null) {
                if (newQuestion != null && !subquestions.isEmpty()) {
                    newQuestion.setChosenSubquestions(subquestions);
                    subquestions.clear();
                }
                newQuestion = questionManager.findQuestion(question.getName());
                questionList.add(newQuestion);
                if (question.getNumQ() > 0) {
                    newQuestion.setNumRand(question.getNumQ());
                }
            } else {
                String[] parts = question.getName().split(" - ");
                subquestions.add(parts[1]);
            }
        }
    }

    private void generateQuiz(String filename) {
        if (!filename.isEmpty()) {
            FileWriter outFile;
            try {
                outFile = new FileWriter("src/papers/" + filename + ".txt");

                Utils utils = new Utils();
                utils.generateHeader(outFile, filename);

                for (Question question : questionList) {
                    question.generateHeader(outFile);
                }

                int count = 1;
                for (Question question : questionList) {
                    ArrayList<String> subquestions = question.getChosenSubquestions();
                    question.generateSubquestionBlock(outFile, count);
                    count += subquestions.size();
                }

                System.out.println("Build done");
                outFile.close();

            } catch (IOException ee) {
                System.out.println("File error");
                System.exit(-1);
            }
        }
    }

}
