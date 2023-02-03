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
import elemental.json.Json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@PageTitle("Main")
@Route(value = "main", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class MainView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public MainView() {
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

        Button uploadButton = new Button("Upload Question");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        upload.setUploadButton(uploadButton);

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
