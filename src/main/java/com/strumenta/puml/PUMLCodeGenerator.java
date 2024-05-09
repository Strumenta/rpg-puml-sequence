package com.strumenta.puml;

import org.apache.commons.text.StringSubstitutor;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates PlantUML (PUML) code from a given PUML diagram model.
 * This class translates PUML diagram components into a textual representation
 * that can be rendered by PlantUML tools.
 */
public class PUMLCodeGenerator {
    private final String template = """
@startuml
'https://plantuml.com/sequence-diagram

!pragma teoz true

hide footbox

${entities}

skinparam sequence {
    ArrowColor Black
    LifeLineBorderColor #000000
    LifeLineBackgroundColor #FFFFFF

    ParticipantBorderColor #000000
    ParticipantBackgroundColor #FFFFFF

    ParticipantFontColor #000000
}
${client} -> ${module} :
${sequence}
@enduml
            """;


    private static final HashMap<Class<?>,PUMLNodePrinter> nodePrinters = new HashMap<>();
    static {
        nodePrinters.put(PUMInvoke.class, (statement) -> {
            PUMInvoke s = (PUMInvoke)statement;
            String args = String.join(" ", s.getParams());
            ArrayList<String> sequence = new ArrayList<>();
            sequence.add(String.format("%s -> %s : %s %s",s.getCaller(),s.getReceiver(),s.getMethod(),args));
            for(PUMLStatement ps : s.getBody()) {
                sequence.add(print(ps));
            }
            return String.join("\n",sequence);
        });

        nodePrinters.put(PUMLoop.class, (statement) -> {
            PUMLoop s = (PUMLoop)statement;
            ArrayList<String> sequence = new ArrayList<>();
            sequence.add(String.format("loop %s %s",s.getType(),s.getCondition()) );
            for(PUMLStatement ps : s.getBody()) {
                sequence.add(print(ps));
            }
            sequence.add("end");
            return String.join("\n",sequence);
        });
        nodePrinters.put(PUMLIf.class, (statement) -> {
            PUMLIf s = (PUMLIf) statement;
            ArrayList<String> sequence = new ArrayList<>();
            sequence.add(String.format("group IF %s",s.getCondition()) );
            for(PUMLStatement ps : s.getBody()) {
                sequence.add(print(ps));
            }
            sequence.add("end");
            return String.join("\n",sequence);
        });

        // Add other class-function pairs here
    }

    /**
     * Generates a PlantUML sequence diagram as a String based on the provided PUMLDiagram and values.
     *
     * @param diagram The PUMLDiagram to generate the sequence diagram from.
     * @param values A map of values to replace placeholders in the PlantUML template.
     * @return A String representation of the PlantUML sequence diagram.
     */
    public String generateToString(PUMLDiagram diagram, HashMap<String, String> values) {

        ArrayList<String> sequence = new ArrayList<>();
        for(PUMLStatement s : diagram.getStatements()) {
            String step = print(s);
            if(!step.isEmpty()) {
                sequence.add(step);
            }
        }
        values.put("sequence",String.join("\n",sequence));
        StringSubstitutor sub = new StringSubstitutor(values);
        String resolvedString = sub.replace(template);

        return resolvedString;
    }
    /**
     * Converts a PUMLStatement into its string representation.
     *
     * @param statement The PUMLStatement to be converted.
     * @return A String representing the given PUMLStatement.
     */
    private static String print(PUMLStatement statement) {
        PUMLNodePrinter printer = nodePrinters.get(statement.getClass());
        if(printer != null) {
            return printer.print(statement);
        }
        return "";

    }

}
