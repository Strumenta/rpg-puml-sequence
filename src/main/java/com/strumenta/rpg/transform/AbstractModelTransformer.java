package com.strumenta.rpg.transform;

import com.strumenta.kolasu.model.Node;

import java.io.File;

/**
 * An abstract base class for model transformers in RPG processing.
 * This class provides a foundational structure for implementing specific
 * transformations, handling input and output file management which are
 * common across various types of model transformations.
 */
public abstract class AbstractModelTransformer implements ModelTransformer {

    protected final File inputFile;
    protected final File outputFile;

    /**
     * Constructs an AbstractModelTransformer with specified input and output files.
     *
     * @param inputFile The file to read input from for the transformation.
     * @param outputFile The file to write the output of the transformation to.
     */
    public AbstractModelTransformer(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    /**
     * Transforms the given model based on specific implementation.
     * This abstract method must be implemented by subclasses to define
     * the specific transformation logic.
     *
     * @param model The model node to be transformed.
     * @return The transformed model node.
     * @throws Exception if the transformation process encounters any issues.
     */
    @Override
    public abstract Node transform(Node model) throws Exception;
}
