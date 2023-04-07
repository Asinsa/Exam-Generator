package com.example.testgenerator.views;

import com.example.testgenerator.views.about.AboutView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
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

    @Id("allQuestions")
    private VerticalLayout allQuestions;
    @Id("chosenQuestions")
    private VerticalLayout chosenQuestions;

    /**
     * Creates a new QuizGenerationView.
     */
    public QuizGenerationView() {
        Button a = new Button("AAAAAAAAAAAAAAAAAAA");
        a.addClickListener( e -> UI.getCurrent().navigate(AboutView.class));
        allQuestions.add(a);

        Button b = new Button("BBBBBBBBBBBBBBBBBBBBB");
        b.addClickListener( e -> UI.getCurrent().navigate(AboutView.class));
        chosenQuestions.add(b);


        /*
        Grid<Person> grid1 = setupGrid();
        Grid<Person> grid2 = setupGrid();

        GridListDataView<Person> dataView1 = grid1.setItems(people1);
        GridListDataView<Person> dataView2 = grid2.setItems(people2);

        grid1.setDropMode(GridDropMode.ON_GRID);
        grid1.setRowsDraggable(true);
        grid1.addDragStartListener(this::handleDragStart);
        grid1.addDropListener(e -> {
            dataView2.removeItem(draggedItem);
            dataView1.addItem(draggedItem);
        });
        grid1.addDragEndListener(this::handleDragEnd);

        grid2.setDropMode(GridDropMode.ON_GRID);
        grid2.setRowsDraggable(true);
        grid2.addDragStartListener(this::handleDragStart);
        grid2.addDropListener(e -> {
            dataView1.removeItem(draggedItem);
            dataView2.addItem(draggedItem);
        });
        grid2.addDragEndListener(this::handleDragEnd);

         */
    }

}
