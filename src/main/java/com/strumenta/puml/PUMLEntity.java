package com.strumenta.puml;

public class PUMLEntity extends PUMLNode {
    public enum EntityType {
        ACTOR,
        ENTITY,
        DATABASE
    }

    private String name;
    private EntityType type;

    public PUMLEntity(String name) {
        this(name, EntityType.ENTITY);
    }

    public PUMLEntity(String name, EntityType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }
}