package com.strumenta.rpg;

import com.strumenta.kolasu.model.Node;
import com.strumenta.rpg.transform.ModelTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a pipeline of model transformations.
 * This class is responsible for managing and executing a series of ModelTransformer
 * instances in a defined sequence. Each transformer takes a model, transforms it,
 * and passes it to the next transformer in the pipeline.
 */
public class Pipeline {
    private final List<ModelTransformer> transformers;

    /**
     * Constructs a new Pipeline with the given transformers.
     * The transformers are applied in the order they are provided.
     *
     * @param args A variable number of ModelTransformer objects.
     */
    public Pipeline(ModelTransformer... args) {
        transformers = new ArrayList<>(Arrays.asList(args));
    }

    /**
     * Executes the pipeline of transformations on a model.
     * Each transformer in the pipeline receives the output of the previous transformer
     * as its input. The first transformer receives a null model.
     *
     * @throws Exception If any transformation in the pipeline fails.
     */
    public void run() throws Exception {
        Node model = null;
        for (ModelTransformer t : transformers) {
            model = t.transform(model);
        }
    }
}
