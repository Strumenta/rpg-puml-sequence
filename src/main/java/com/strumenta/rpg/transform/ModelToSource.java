package com.strumenta.rpg.transform;

import com.strumenta.kolasu.model.Node;
import com.strumenta.puml.PUMLCodeGenerator;
import com.strumenta.puml.PUMLDiagram;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

/**
 * Transformer for converting a PUML model into PlantUML source code.
 * This class extends AbstractModelTransformer and is responsible for
 * generating PlantUML code from a given PUML diagram model and writing
 * it to the specified output file.
 */
public class ModelToSource extends AbstractModelTransformer {

/**
 * Constructs a ModelToSource transformer with specified input and output files.
 *
 * @param inputFile The file containing the PUML model to be transformed.
 * @param outputFile The file where the generated PlantUML code will be written.
 */
    public ModelToSource(File inputFile, File outputFile) {
        super(inputFile, outputFile);
    }

    /**
     * Performs the transformation from a PUML model to PlantUML source code.
     * This method generates PlantUML code using the PUMLCodeGenerator and writes
     * the output to the specified file. The method expects the model to be an
     * instance of PUMLDiagram.
     *
     * @param model The PUML model to be transformed into PlantUML code.
     * @return The original model after the transformation process.
     * @throws Exception If the model is not a PUMLDiagram, or if there are issues
     *                   with file writing.
     */
    @Override
    public Node transform(Node model) throws Exception {
        PUMLCodeGenerator generator = new PUMLCodeGenerator();

        if (model instanceof PUMLDiagram) {
            String filePath = null;
            HashMap<String, String> values = new HashMap<>();
            values.put("client", "client");
            if(outputFile.isFile()) {
                filePath = outputFile.getAbsolutePath();
                values.put("module", outputFile.getName());
            } else {
                filePath = String.format("%s.puml",outputFile.getPath() + File.separator + inputFile.getName());
                values.put("module", inputFile.getName());
            }

            PUMLDiagram diagram = (PUMLDiagram) model;
            String code = generator.generateToString(diagram, values);
            try {
                Files.write(Paths.get(filePath), Collections.singleton(code), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new Exception(e.getMessage());
            }
            return model;
        }


        throw new Exception(String.format("Invalid input Model: %s",model.getClass().getName()));
    }
}
