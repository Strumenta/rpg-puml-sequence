package com.strumenta.puml;

/**
 * Functional interface for printing PUML nodes.
 * This interface is used for implementing custom printers that
 * can convert PUMLStatement objects into their string representations.
 */
@FunctionalInterface
public interface PUMLNodePrinter {

    /**
     * Converts a given PUMLStatement into a string representation.
     * Implementations of this method should provide a specific format
     * for the given PUMLStatement suitable for use in PlantUML diagrams.
     *
     * @param statement The PUMLStatement to be printed.
     * @return A String representation of the PUMLStatement.
     */
    String print(PUMLStatement statement);
}