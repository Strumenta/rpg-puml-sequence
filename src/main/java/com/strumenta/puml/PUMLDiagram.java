package com.strumenta.puml;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a PUML diagram.
 * This class is a container for PUML statements, modeling the structure
 * of a PlantUML diagram. It extends PUMLNode, allowing it to be part of
 * a larger PUML structure.
 */
public class PUMLDiagram extends PUMLNode {
    private final List<PUMLEntity> entities = new ArrayList<>();
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

    public PUMLEntity add(PUMLEntity entity) {
        entity.setParent(this);
        entities.add(entity);
        return entity;
    }
    /**
     * Retrieves the list of PUML statements that make up the diagram.
     *
     * @return A list of PUMLStatement objects representing the components of the diagram.
     */
    public List<PUMLStatement> getStatements() {
        return statements;
    }

    public List<PUMLEntity> getEntities() {
        return entities;
    }

    public void ensureHasEntity(String entityName, PUMLEntity.EntityType entityType, Color color) {
        if (entities.stream().noneMatch(e -> e.getName().equals(entityName))) {
            PUMLEntity entity = new PUMLEntity(entityName, entityType);
            entity.setColor(color);
            add(entity);
        }
    }
    public void ensureHasEntity(String entityName, PUMLEntity.EntityType entityType) {
        ensureHasEntity(entityName, entityType, null);
    }
}
