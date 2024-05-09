package com.strumenta.rpg;

import com.strumenta.rpg.transform.ModelToModel;
import com.strumenta.rpg.transform.ModelToSource;
import com.strumenta.rpg.transform.SourceToModel;
import org.apache.commons.cli.*;

import java.io.File;

/**
 * Main class for the RPG to PlantUML conversion tool.
 * This command-line application takes RPG source code as input
 * and generates a PlantUML diagram as output. The conversion
 * process involves multiple stages of transformation.
 */
public class RPGtoPUML {

    /**
     * The main method for the RPGtoPUML command-line application.
     * It parses command-line arguments for input and output file paths
     * and orchestrates the conversion process.
     *
     * @param args Command-line arguments, expecting input and output file paths.
     */
    public static void main(String[] args) {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);
        Option output = new Option("o", "output", true, "output file path");
        output.setRequired(true);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            File inputFile = new File(cmd.getOptionValue("input"));
            File outputFile = new File(cmd.getOptionValue("output"));

            try {
                Pipeline pipeline = new Pipeline(
                        new SourceToModel(inputFile,outputFile),    // Transform the RPG source code to the model (AST)
                        new ModelToModel(inputFile,outputFile),            // Transform the RPG Model (AST) to the PlantUML model (AST)
                        new ModelToSource(inputFile,outputFile)            // Transform the PlantUML Model (AST) to the PlantUML code
                );
                pipeline.run();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Exception: " + e.getMessage());
                System.exit(1);
            }

            // Process the input file path
        } catch (ParseException e) {
            System.err.println("Invalid command.");
            formatter.printHelp("RPGtoPUML", options);
            System.exit(1);
        }
    }
}