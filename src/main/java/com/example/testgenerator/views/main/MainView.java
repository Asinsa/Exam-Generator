package com.example.testgenerator.views.main;

import com.example.testgenerator.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

    public MainView() {

        // Upload questions
        File uploadFolder = new File("src/main/questions");
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
        List<ArrayList<String>> questions = new ArrayList<>();

        File[] files = new File("src/main/questions").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                String filename = file.getName();
                if (FilenameUtils.getExtension(filename).equals("java")) {
                    ArrayList<String> question = new ArrayList<>();

                    question.add(filename.substring(0, filename.lastIndexOf('.')));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    question.add(sdf.format(file.lastModified()));

                    questions.add(question);
                }
            }
        }

        Grid<ArrayList<String>> grid = new Grid<>();
        grid.setItems(questions);
        grid.addColumn(a -> a.get(0)).setHeader("Question Name").setSortable(true);
        grid.addColumn(a -> a.get(1)).setHeader("Upload Date").setSortable(true);

        // Layouts
        setMargin(true);
        add(upload);
        add(grid);
    }


}
