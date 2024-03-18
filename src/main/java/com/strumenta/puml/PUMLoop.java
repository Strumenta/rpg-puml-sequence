package com.strumenta.puml;

import java.util.ArrayList;

/**
 * Represents a loop statement in a PlantUML diagram.
 * This class models the concept of a loop, which includes a type, a condition,
 * and a body containing a list of PUMLStatements that are executed as part of the loop.
 */
public class PUMLoop extends PUMLStatement {

    private final String type;
    private final String condition;
    private final ArrayList<PUMLStatement> body = new ArrayList<>();

    /**
     * Constructs a new PUMLoop with the specified type and condition.
     *
     * @param type      the type of loop (e.g., "FOR", "WHILE", "UNTIL")
     * @param condition the condition on which the loop is based
     */
    public PUMLoop(String type, String condition) {
        this.type = type;
        this.condition = condition;
    }

    /**
     * Retrieves the type of the loop.
     *
     * @return the type of the loop
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieves the condition of the loop.
     *
     * @return the condition of the loop
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Retrieves the body of the loop, which is a list of PUMLStatements.
     *
     * @return the body of the loop as an ArrayList of PUMLStatements
     */
    public ArrayList<PUMLStatement> getBody() {
        return body;
    }
}