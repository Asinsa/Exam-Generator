package com.example.testgenerator.views.main;

import com.example.testgenerator.Question;
import com.example.testgenerator.QuestionService;
import com.example.testgenerator.views.MainLayout;
import com.example.testgenerator.views.QuizGenerationView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.MultiFileReceiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import elemental.json.Json;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashSet;

/**
 * Main View Class is the first view users will see and contains a question upload section and a list of
 * all previously uploaded questions.
 */
@PageTitle("Main")
@RouteAlias("home")
@Route(value = "main", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class MainView extends HorizontalLayout {

    private Button uploadButton;
    private Button makeQuiz;
    private QuestionService questionManager;

    /**
     * Creates a new MainView.
     *
     * @param questionManager   The question service that contains all information about the questions.
     */
    public MainView(QuestionService questionManager) {
        this.questionManager = questionManager;

        // Layouts
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setAlignItems(Alignment.CENTER);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();

        setMargin(true);

        layout.add(getUploadSection(), getFileList());

        // Make Quiz Button
        makeQuiz = new Button("Make Test");
        makeQuiz.addClickListener( e -> UI.getCurrent().navigate(QuizGenerationView.class));

        add(mainLayout);

        mainLayout.add(layout, makeQuiz);
    }

    /**
     * Method to create the upload section consisting of an upload button and a file uploader.
     *
     * @return the component containg the upload section.
     */
    private Component getUploadSection() {
        // Upload questions
        File uploadFolder = new File("src/main/java/com/example/testgenerator/questions");
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        Upload upload = new Upload((MultiFileReceiver) (filename, mimeType) -> {
            File file = new File(uploadFolder, filename);
            try {
                Notification.show("Uploaded file - '" + filename.substring(0, filename.lastIndexOf('.')) + "'");
                return new FileOutputStream(file);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return null;
            }
        });

        upload.addSucceededListener(event -> {
            upload.getElement().setPropertyJson("files", Json.createArray());
        });

        upload.setAcceptedFileTypes("text/x-java-source,java", ".java");

        uploadButton = new Button("Upload Question");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        uploadButton.addClickShortcut(Key.ENTER);

        upload.setUploadButton(uploadButton);
        return upload;
    }

    /**
     * Method to create the list of questions and their upload date.
     *
     * @return the Grid component containg the question file list.
     */
    private Component getFileList() {
        // View All Question Names
        HashSet<Question> questions = new HashSet<>();

        File[] files = new File("src/main/java/com/example/testgenerator/questions").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                String filename = file.getName();
                if (FilenameUtils.getExtension(filename).equals("java")) {
                    String name = filename.substring(0, filename.lastIndexOf('.'));

                    try {
                        Class<?> questionClass = Class.forName("com.example.testgenerator.questions." + name);
                        Question question = (Question) questionClass.newInstance();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        question.setDate(sdf.format(file.lastModified()));

                        questions.add(question);
                        questionManager.addQuestion(name, question);
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                        System.out.println(name + " is not a valid question file!");
                    }
                }
            }
        }

        Grid<Question> grid = new Grid<>(Question.class, false);
        grid.addColumn(Question::getName).setHeader("Question Name").setSortable(true);
        grid.addColumn(Question::getDate).setHeader("Upload Date").setSortable(true);
        grid.setItems(questions);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        return grid;
    }
}
