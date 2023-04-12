package com.example.testgenerator.views;

import com.example.testgenerator.Question;
import com.example.testgenerator.QuestionService;
import com.example.testgenerator.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A Designer generated component for the quiz-generation-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("quiz-generation-view")
@Route(value = "generate", layout = MainLayout.class)
@PreserveOnRefresh
public class QuizGenerationView extends VerticalLayout {

    private QuestionService questionManager;
    private Question draggedItem;

    /**
     * Creates a new QuizGenerationView.
     */
    public QuizGenerationView(QuestionService questionManager) {
        try {
            this.questionManager = questionManager;

            Collection<Question> questions = questionManager.getAllQuestions();
            Collection<Question> dum = questionManager.getDummyQuestions();
            ArrayList<Question> questions1 = new ArrayList<>(questions);
            ArrayList<Question> dum2 = new ArrayList<>(dum);

            Grid<Question> grid1 = setupGrid("Questions");
            Grid<Question> grid2 = setupGrid("Chosen Test Questions");

            GridListDataView<Question> dataView1 = grid1.setItems(questions1);
            GridListDataView<Question> dataView2 = grid2.setItems(dum2);

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

            Div container = new Div(grid1, grid2);
            setContainerStyles(container);

            add(container);
        } catch (NullPointerException e) {
            UI.getCurrent().navigate(MainView.class);
        }
    }

    private static Grid<Question> setupGrid(String name) {
        Grid<Question> grid = new Grid<>(Question.class, false);
        grid.addColumn(Question::getName).setHeader(name);
        setGridStyles(grid);

        return grid;
    }

    private void handleDragStart(GridDragStartEvent<Question> e) {
        draggedItem = e.getDraggedItems().get(0);
    }

    private void handleDragEnd(GridDragEndEvent<Question> e) {
        draggedItem = null;
    }

    private static void setGridStyles(Grid<Question> grid) {
        grid.getStyle().set("width", "300px").set("height", "300px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }

    private static void setContainerStyles(Div container) {
        container.getStyle().set("display", "flex").set("flex-direction", "row")
                .set("flex-wrap", "wrap");
    }
}
