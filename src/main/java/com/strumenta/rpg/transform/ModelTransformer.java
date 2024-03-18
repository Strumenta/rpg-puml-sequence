package com.strumenta.rpg.transform;

import com.strumenta.kolasu.model.Node;

/**
 * Interface for transforming one model node into another.
 * This interface is used in the context of processing RPG code,
 * allowing for the transformation of models at various stages
 * of the parsing and generation process.
 */
public interface ModelTransformer {

    /**
     * Transforms a given model node into another model node.
     * This method is intended to be implemented to perform specific
     * transformations, depending on the desired outcome of the transformation process.
     *
     * @param model The model node to be transformed.
     * @return The transformed model node.
     * @throws Exception if the transformation cannot be completed successfully.
     */
    Node transform(Node model) throws Exception;
}
