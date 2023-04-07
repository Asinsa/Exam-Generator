package com.example.testgenerator.views.main;

import com.example.testgenerator.Question;
import com.example.testgenerator.views.MainLayout;
import com.example.testgenerator.views.QuizGenerationView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
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
import java.util.ArrayList;
import java.util.List;

@PageTitle("Main")
@Route(value = "main", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class MainView extends HorizontalLayout {

    private Button uploadButton;
    private Button makeQuiz;

    public MainView() {

        VerticalLayout mainLayout = new VerticalLayout();
        add(mainLayout);
        mainLayout.setAlignItems(Alignment.CENTER);

        HorizontalLayout layout = new HorizontalLayout();
        setMargin(true);
        layout.setWidthFull();
        mainLayout.add(layout);

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


        // View All Question Names
        List<Question> questions = new ArrayList<>();

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

        // Layouts
        layout.add(upload);
        layout.add(grid);

        // Make Quiz Button
        makeQuiz = new Button("Make Test");
        makeQuiz.addClickListener( e -> UI.getCurrent().navigate(QuizGenerationView.class));
        mainLayout.add(makeQuiz);
    }
}
