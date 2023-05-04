package com.example.testgenerator.views;

import com.example.testgenerator.Q;
import com.example.testgenerator.QuestionService;
import com.example.testgenerator.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

/**
 * A Designer generated component for the quiz-generation-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("quiz-generation-view")
@Route(value = "generate", layout = MainLayout.class)
@PreserveOnRefresh
public class QuizGenerationView extends HorizontalLayout {

    private QuestionService questionManager;
    private Q draggedItem;

    /**
     * Creates a new QuizGenerationView.
     */
    public QuizGenerationView(QuestionService questionManager) {
        questionManager.update();

        // Layouts
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setHeightFull();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setHeightFull();

        setMargin(true);


        try {
            this.questionManager = questionManager;

            // Make grids
            TreeData<Q> questionData = new TreeData<>();
            TreeGrid<Q> questionsGrid = setupGrid("Questions", questionData);
            questionData.addItems(questionManager.getRootQuestions(), questionManager::getChildQuestions);
            questionsGrid.setMaxWidth("25%");
            questionsGrid.setHeightFull();

            TreeData<Q> chosenQData = new TreeData<>();
            TreeGrid<Q> chosenQGrid = setupGrid("Chosen Test Questions", chosenQData);
            chosenQGrid.setHeightFull();
            chosenQGrid.addColumn(Q::getNumQ).setHeader("Number Of Questions");//.setFooter("Total Questions = ");
            FooterRow footer = chosenQGrid.prependFooterRow();
            footer.getCell(chosenQGrid.getColumns().get(1)).setText("Total Questions = 0");

            questionsGrid.setDropMode(GridDropMode.ON_GRID);
            questionsGrid.setRowsDraggable(true);
            questionsGrid.addDragStartListener(this::handleDragStart);
            questionsGrid.addDropListener(e -> {
                System.out.println(draggedItem.getName());
                if (draggedItem.getParent() == null) { // if question then remove all corresponding subquestions first
                    for (Q subquestion : chosenQData.getChildren(draggedItem)) {
                        subquestion.removeQ();
                    }

                    if (chosenQData.getChildren(draggedItem.getParent()).stream().count() == 0) {
                        chosenQData.removeItem(draggedItem.getParent());
                    }
                }
                // just remove the dragged one
                draggedItem.removeQ();
                chosenQData.removeItem(draggedItem);

                int count = 0;
                for (Q question : chosenQData.getRootItems()) {
                    for (Q subquestion : chosenQData.getChildren(question)) {
                        count += subquestion.getNumQ();
                    }
                    count += question.getNumQ();
                }
                footer.getCell(chosenQGrid.getColumns().get(1)).setText("Total Questions = " + count);

                chosenQGrid.getDataProvider().refreshAll();
            });
            questionsGrid.addDragEndListener(this::handleDragEnd);

            chosenQGrid.setDropMode(GridDropMode.ON_GRID);
            chosenQGrid.setDropMode(GridDropMode.BETWEEN);
            chosenQGrid.setRowsDraggable(true);
            chosenQGrid.addDragStartListener(this::handleDragStart);
            chosenQGrid.addDropListener(e -> {
                if (draggedItem.getParent() != null) { // if it is a subquestion
                    if (!chosenQData.contains(draggedItem.getParent())) { // if it's question isn't in list add it
                        chosenQData.addItem(null, draggedItem.getParent());
                    }
                } else {
                    draggedItem.addQ();
                }

                if (!chosenQData.contains(draggedItem)) {
                    chosenQData.addItem(draggedItem.getParent(), draggedItem);
                    if (draggedItem.getParent() != null) {
                        draggedItem.addQ();
                    }
                } else {
                    if (draggedItem.getParent() != null) {
                        draggedItem.addQ();
                    }
                }

                int count = 0;
                for (Q question : chosenQData.getRootItems()) {
                    for (Q subquestion : chosenQData.getChildren(question)) {
                        count += subquestion.getNumQ();
                    }
                    count += question.getNumQ();
                }
                footer.getCell(chosenQGrid.getColumns().get(1)).setText("Total Questions = " + count);

                chosenQGrid.getDataProvider().refreshAll();
            });
            chosenQGrid.addDragEndListener(this::handleDragEnd);

            layout.add(questionsGrid, chosenQGrid);
            layout.setHeightFull();

            // Make Quiz Button
            Button makeQuiz = new Button("Next");
            makeQuiz.addClickListener( e -> UI.getCurrent().navigate(QuizGenerationView.class));

            add(mainLayout);
            mainLayout.add(layout, makeQuiz);

        } catch (NullPointerException e) {
            UI.getCurrent().navigate(MainView.class);
        }
    }

    private static TreeGrid<Q> setupGrid(String name, TreeData<Q> data) {
        TreeGrid<Q> grid = new TreeGrid<>(new TreeDataProvider<>(data));
        grid.addHierarchyColumn(Q::getName).setHeader(name);

        setGridStyles(grid);

        return grid;
    }

    private void handleDragStart(GridDragStartEvent<Q> e) {
        draggedItem = e.getDraggedItems().get(0);
    }

    private void handleDragEnd(GridDragEndEvent<Q> e) {
        draggedItem = null;
    }

    private static void setGridStyles(Grid<Q> grid) {
        grid.getStyle().set("width", "300px").set("height", "300px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }

    private static void setContainerStyles(Div container) {
        container.getStyle().set("display", "flex").set("flex-direction", "row")
                .set("flex-wrap", "wrap");
    }

}
