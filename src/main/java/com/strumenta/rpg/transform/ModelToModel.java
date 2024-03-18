package com.strumenta.rpg.transform;

import com.strumenta.kolasu.model.Node;
import com.strumenta.puml.*;
import com.strumenta.rpgparser.model.*;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Transforms a RPG model into a PUML (PlantUML) diagram.
 * This class extends from AbstractModelTransformer and provides
 * an implementation for transforming various RPG statements
 * into their corresponding PUML representations.
 */
public class ModelToModel extends AbstractModelTransformer {

    private final Stack<String> stack = new Stack<>();

    /**
     * Constructs a new ModelToModel transformer with specified input and output files.
     *
     * @param inputFile  the file to be transformed.
     * @param outputFile the file where the transformed output will be written.
     */
    public ModelToModel(File inputFile, File outputFile) {
        super(inputFile, outputFile);
    }

    /**
     * Transforms a given RPG model into a PUML diagram.
     *
     * @param model the RPG model to be transformed.
     * @return the AST representing the PUML diagram.
     * @throws Exception if the transformation fails.
     */
    @Override
    public Node transform(Node model) throws Exception {
        PUMLDiagram target = new PUMLDiagram();
        String file = inputFile.getName();
        if (model instanceof CompilationUnit) {
            CompilationUnit cu = (CompilationUnit) model;
            // RPG code contains an initialization routine it is executed first
            for (Subroutine s : cu.getSubroutines()) {
                if (s.isInitializationSubroutine()) {
                    target.add(
                            new PUMInvoke(file, file, "inzsr", List.of()
                            ));
                }
            }
            // Process the main statements
            for (Statement s : cu.getMainStatements()) {
                target.add(transformStatement(cu, s));
            }
            return target;
        }
        throw new Exception(String.format("Invalid input Model: %s", model.getClass().getName()));

    }

    /**
     * Transforms an RPG statement into a PUML statement.
     *
     * @param cu        the CompilationUnit associated with the statement.
     * @param statement the RPG statement to be transformed.
     * @return the transformed PUML statement.
     */
    private PUMLStatement transformStatement(CompilationUnit cu, Statement statement) {
        String file = inputFile.getName();
        if (!stack.isEmpty()) {
            file = stack.peek();
        }
        if (statement instanceof InvokeSubroutineStatement stmt) {
            PUMInvoke subroutine = new PUMInvoke(file, stmt.getSubroutine().getName(), stmt.getSubroutine().getName(), List.of());

            for (Subroutine sub : cu.getSubroutines()) {
                if (stmt.getSubroutine().getName().equalsIgnoreCase(sub.getName())) {
                    stack.push(stmt.getSubroutine().getName());
                    for (Statement s : sub.getStatements()) {
                        subroutine.getBody().add(transformStatement(cu, s));
                    }
                    stack.pop();
                }
            }
            return subroutine;
        }

        if (statement instanceof ConditionIfStatement stmt) {
            String condition = transformExpression(stmt.getCondition());
            PUMLIf ifThen = new PUMLIf(condition);
            for (Statement body : stmt.getThenBody()) {
                ifThen.getBody().add(transformStatement(cu, body));
            }
            return ifThen;
        }

        if (statement instanceof ConditionDoUntilStatement stmt) {
            String condition = transformExpression(stmt.getCondition());
            PUMLoop loop = new PUMLoop("UNTIL", condition);
            for (Statement body : stmt.getBody()) {
                loop.getBody().add(transformStatement(cu, body));
            }
            return loop;
        }

        if (statement instanceof SetLowerLimitStatement stmt) {
            String searchArg = transformExpression(stmt.getSearchArgument());
            String reference = transformExpression(stmt.getName());
            return new PUMInvoke(file, reference, "SETLL", List.of(searchArg, reference));
        }

        if (statement instanceof ReadRecordStatement stmt) {
            String reference = transformExpression(stmt.getName());
            return new PUMInvoke(file, reference, "READ", List.of(reference));
        }

        if (statement instanceof WriteRecordStatement stmt) {
            String reference = transformExpression(stmt.getName());
            return new PUMInvoke(file, reference, "WRITE", List.of(reference));
        }
        if (statement instanceof DeleteRecordStatement stmt) {
            String reference = transformExpression(stmt.getName());
            return new PUMInvoke(file, reference, "DELETE", List.of(reference));
        }
        if (statement instanceof UpdateRecordStatement stmt) {
            String reference = transformExpression(stmt.getName());
            return new PUMInvoke(file, reference, "UPDATE", List.of(reference));
        }
        return new PUMLEmpty();
    }

    /**
     * Transforms an RPG expression into its string representation.
     *
     * @param expression the RPG expression to be transformed.
     * @return the string representation of the expression.
     */
    private String transformExpression(Expression expression) {
        if (expression instanceof IntLiteral expr) {
            return expr.getValue();
        }
        if (expression instanceof DecLiteral expr) {
            return expr.getValue();
        }
        if (expression instanceof StringLiteral expr) {
            return expr.getValue();
        }
        if (expression instanceof FigurativeConst expr) {
            return expr.getText().toUpperCase();
        }
        if (expression instanceof ReferenceExpr expr) {
            return expr.getDataDefinition().getName().toUpperCase();
        }

        if (expression instanceof ComparisonExpr expr) {
            Map<ComparisonType, String> operators = Map.of(
                    ComparisonType.Equality, "=",
                    ComparisonType.Inequality, "!=",
                    ComparisonType.LessThan, "<",
                    ComparisonType.LessEq, "<=",
                    ComparisonType.MoreThan, ">",
                    ComparisonType.MoreEq, ">="
            );

            return String.format("%s %s %s", transformExpression(expr.getLeft()), operators.get(expr.getComparisonType()), transformExpression(expr.getRight()));
        }

        if (expression instanceof LogicalNegationExpr expr) {
            return String.format("NOT %s", transformExpression(expr.getBase()));
        }

        if (expression instanceof BuiltinFunctionCall expr) {
            List<String> args = expr.getParams().stream()
                    .map(this::transformExpression)
                    .toList();
            return String.format("%s(%s)", expr.getFunctionName().toUpperCase(), String.join(",", args));
        }

        return "???";
    }
}
