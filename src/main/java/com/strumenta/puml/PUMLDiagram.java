package com.strumenta.puml;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a PUML diagram.
 * This class is a container for PUML statements, modeling the structure
 * of a PlantUML diagram. It extends PUMLNode, allowing it to be part of
 * a larger PUML structure.
 */
public class PUMLDiagram extends PUMLNode {
    private final List<PUMLStatement> statements = new ArrayList<>();
    /**
     * Adds a PUML statement to the diagram.
     *
     * @param statement The PUMLStatement to be added to the diagram.
     * @return The added PUMLStatement.
     */
    public PUMLStatement add(PUMLStatement statement) {
        statement.setParent(this);
        statements.add(statement);
        return statement;
    }
    /**
     * Retrieves the list of PUML statements that make up the diagram.
     *
     * @return A list of PUMLStatement objects representing the components of the diagram.
     */
    public List<PUMLStatement> getStatements() {
        return statements;
    }
}
