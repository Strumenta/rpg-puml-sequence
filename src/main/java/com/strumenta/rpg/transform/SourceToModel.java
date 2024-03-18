package com.strumenta.rpg.transform;

import com.strumenta.kolasu.model.Node;
import com.strumenta.kolasu.parsing.ParsingResult;
import com.strumenta.rpgparser.RPGKolasuParser;

import java.io.File;

/**
 * Transforms RPG source code into an Abstract Syntax Tree (AST).
 * This class extends AbstractModelTransformer to convert RPG code from
 * a source file into a structured model that can be further processed or analyzed.
 */
public class SourceToModel extends AbstractModelTransformer {

    /**
     * Constructs a SourceToModel transformer with specified input and output files.
     *
     * @param inputFile  The file containing the RPG source code to be transformed.
     * @param outputFile The file where the transformed output will be written (not used in this transformer).
     */
    public SourceToModel(File inputFile, File outputFile) {
        super(inputFile, outputFile);
    }

    /**
     * Performs the transformation of RPG source code into an AST.
     * Reads the source code from the input file, parses it using RPGKolasuParser,
     * and returns the resulting AST.
     *
     * @param model Not used in this implementation as the transformation is from source code to AST.
     * @return The root node of the generated AST representing the parsed RPG code.
     * @throws Exception if the input file is invalid or the parsing fails.
     */
    @Override
    public Node transform(Node model) throws Exception {
        if (inputFile.isFile() && inputFile.exists()) {
            RPGKolasuParser rpgParser = RPGKolasuParser.parserFromExtension(inputFile);
            ParsingResult result = rpgParser.parse(inputFile);
            if(result.getCorrect()) {
                return result.getRoot();
            }
        }
        throw new Exception(String.format("Invalid input file '%s'", inputFile.toPath()));
    }
}
