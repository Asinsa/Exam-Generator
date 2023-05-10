package com.example.testgenerator.views;

import com.example.testgenerator.Q;
import com.example.testgenerator.Question;
import com.example.testgenerator.QuestionService;
import com.example.testgenerator.Utils;
import com.example.testgenerator.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
     *
     * @param questionManager   The question service that contains all information about the questions.
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

            if (!fileExists(filename)) {
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

    /**
     * Method to check if a file by the file name specified by user exists already.
     *
     * @param filename  Filename entered by user.
     * @return true if filename already present, false if not.
     */
    private boolean fileExists(String filename) {
        File[] files = new File("src/papers").listFiles();

        int copies = 0;
        for (File file : files) {
            if (file.isFile()) {
                if ((filename + ".txt").equals(file.getName())) {
                    copies++;
                }
            }
        }

        if (copies == 0) {
            return false;
        }
        return true;
    }

    /**
     * Method to add the chosen questions to the question service and the chosen subquestions to the questions.
     */
    private void addChosen() {
        Question newQuestion = null;
        for (Q question : chosenQuestions) {
            if (question.getParent() == null) { // is a question

                if (newQuestion != null) {
                    questionList.add(newQuestion);
                }
                newQuestion = questionManager.findQuestion(question.getName());

                if (question.getNumQ() > 0) {
                    newQuestion.setNumRand(question.getNumQ());
                }
            } else if (newQuestion != null) {
                String[] parts = question.getName().split(" - ");
                newQuestion.addChosenSubquestion(parts[1]);
            }
        }
        questionList.add(newQuestion);
    }

    /**
     * Method to generate the quiz files.
     *
     * @param filename  Name of the quiz and the filename of the quiz.
     */
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

                for (Question question : questionList) {
                    question.generateSubquestionBlock(outFile, 1);
                }

                System.out.println("Build done");
                outFile.close();
                returnSuccess("New quiz/quizzes " + filename + " created!");
                UI.getCurrent().navigate(MainView.class);

            } catch (IOException ee) {
                System.out.println("File error");
                System.exit(-1);
            } catch (Exception e) {
                QuizGenerationView.returnError("Quiz unable to be created due to question/subquestion file error! Please check the contents of your file!");
            }
        }
    }

    /**
     * Method to return an success notification.
     *
     * @param successDescription  The success message that will be displayed to the user.
     */
    public static void returnSuccess(String successDescription) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        Div text = new Div(new Text(successDescription));

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> {
            notification.close();
        });

        HorizontalLayout layout = new HorizontalLayout(text, closeButton);

        notification.add(layout);
        notification.open();
    }
}
