package com.example.testgenerator;

/**
 * Class to define the questions and subquestions in order to go in the TreeGrid.
 */
public class Q {
    private int id;
    private String name;
    private Q parent;
    private int numQ = 0;

    /**
     * Constructor
     *
     * @param id        The unique identifier of the question/subquestion.
     * @param name      The name of the question/subquestion.
     * @param parent    The question the subquestion belongs to.
     */
    public Q(int id, String name, Q parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    /**
     * Getter method for getting the id of the question/subquestion.
     *
     * @return the id of the question/subquestion.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter method for setting the id of the question/subquestion.
     *
     * @param id    The id of the question/subquestion.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for getting the name of the question/subquestion.
     *
     * @return the name of the question/subquestion.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for setting the name of the question/subquestion.
     *
     * @param name  The name of the question/subquestion.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for getting the question the subquestion belongs to.
     *
     * @return the name of the subquestion's question if it is a subquestion otherwise returns null.
     */
    public Q getParent() {
        return parent;
    }

    /**
     * Setter method for setting the parent of the subquestion.
     *
     * @param parent  The question the subquestion belongs to.
     */
    public void setParent(Q parent) {
        this.parent = parent;
    }

    /**
     * Method to add a duplicate question.
     */
    public void addQ() {
        numQ++;
    }

    /**
     * Method to remove all duplicate questions.
     */
    public void removeQ() {
        numQ = 0;
    }

    /**
     * Method to get the number of duplicate questions.
     *
     * @return the number of duplicate questions.
     */
    public int getNumQ() {
        return numQ;
    }
}
