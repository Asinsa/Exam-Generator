package com.example.testgenerator.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.router.Route;

/**
 * A Designer generated component for the quiz-generation-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("quiz-generation-view")
@JsModule("./quiz-generation-view.ts")
@Route(value = "generate", layout = MainLayout.class)
public class QuizGenerationView extends LitTemplate {

    /**
     * Creates a new QuizGenerationView.
     */
    public QuizGenerationView() {
        // You can initialise any data required for the connected UI components here.
    }

}
