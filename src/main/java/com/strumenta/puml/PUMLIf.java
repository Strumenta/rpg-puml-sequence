package com.strumenta.puml;

import java.util.ArrayList;

/**
 * Represents an if statement in a PlantUML diagram.
 * This class encapsulates the concept of an if statement,
 * which includes a condition and a body (then part) that contains
 * a list of PUMLStatements to be executed if the condition is true.
 */
public class PUMLIf extends PUMLStatement {

    private final String condition;
    private final ArrayList<PUMLStatement> then = new ArrayList<>();

    /**
     * Constructs a new PUMLIf with the specified condition.
     *
     * @param condition the condition on which the if statement is based
     */
    public PUMLIf(String condition) {
        this.condition = condition;
    }

    /**
     * Retrieves the condition of the if statement.
     *
     * @return the condition of the if statement
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Retrieves the 'then' part of the if statement, which is a list of PUMLStatements.
     *
     * @return the 'then' part of the if statement as an ArrayList of PUMLStatements
     */
    public ArrayList<PUMLStatement> getBody() {
        return then;
    }

    public boolean hasEmptyBody() {
        return this.then.stream().allMatch(s -> s instanceof PUMLEmpty);
    }
}