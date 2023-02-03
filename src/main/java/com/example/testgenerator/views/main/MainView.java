package com.example.testgenerator.views.main;

import com.example.testgenerator.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.MultiFileReceiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@PageTitle("Main")
@Route(value = "main", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class MainView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;
    private Button addQuestion;

    public MainView() {
        File uploadFolder = new File("src/main/questions");
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        Upload upload = new Upload((MultiFileReceiver) (filename, mimeType) -> {
            File file = new File(uploadFolder, filename);
            try {
                return new FileOutputStream(file);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return null;
            }
        });

        upload.setAcceptedFileTypes("text/x-java-source,java", ".java");

        Button uploadButton = new Button("Upload Question");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        upload.setUploadButton(uploadButton);

        // Disable the upload button after the file is selected
        // Re-enable the upload button after the file is cleared
        upload.getElement().addEventListener("max-files-reached-changed", event -> {
            boolean maxFilesReached = event.getEventData()
                    .getBoolean("event.detail.value");
            uploadButton.setEnabled(!maxFilesReached);
        }).addEventData("event.detail.value");

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            Notification.show("Uploading file " + fileName);

            // Do something with the file data
            // processFile(inputStream, fileName);
        });

        add(upload);

        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);
    }


}
