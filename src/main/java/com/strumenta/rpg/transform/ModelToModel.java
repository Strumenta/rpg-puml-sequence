package com.strumenta.rpg.transform;

import com.strumenta.kolasu.model.Node;
import com.strumenta.puml.*;
import com.strumenta.rpgparser.model.*;


import java.awt.Color;
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

        // REFINEMENT
        target.getEntities().add(new PUMLEntity("client", PUMLEntity.EntityType.ACTOR));
        target.getEntities().add(new PUMLEntity(file, PUMLEntity.EntityType.ENTITY));

        if (model instanceof CompilationUnit) {
            CompilationUnit cu = (CompilationUnit) model;
            // RPG code contains an initialization routine it is executed first
            for (Subroutine s : cu.getSubroutines()) {
                if (s.isInitializationSubroutine()) {
                    PUMInvoke pumInvoke = new PUMInvoke(file, file, "inzsr", List.of());
                    // REFINEMENT
                    target.ensureHasEntity(pumInvoke.getMethod(), PUMLEntity.EntityType.ENTITY);
                    target.add(pumInvoke);
                }
            }
            // Process the main statements
            for (Statement s : cu.getMainStatements()) {
                target.add(transformStatement(cu, target, s));
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
    private PUMLStatement transformStatement(CompilationUnit cu, PUMLDiagram target, Statement statement) {
        String file = inputFile.getName();
        if (!stack.isEmpty()) {
            file = stack.peek();
        }
        if (statement instanceof InvokeSubroutineStatement stmt) {
            PUMInvoke subroutine = new PUMInvoke(file, stmt.getSubroutine().getName(), stmt.getSubroutine().getName(), List.of());
            // REFINEMENT
            target.ensureHasEntity(subroutine.getMethod(), PUMLEntity.EntityType.ENTITY);

            for (Subroutine sub : cu.getSubroutines()) {
                if (stmt.getSubroutine().getName().equalsIgnoreCase(sub.getName())) {
                    stack.push(stmt.getSubroutine().getName());
                    for (Statement s : sub.getStatements()) {
                        subroutine.getBody().add(transformStatement(cu, target, s));
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
                ifThen.getBody().add(transformStatement(cu, target, body));
            }
            // REFINEMENT
            if (ifThen.hasEmptyBody()) {
                return new PUMLEmpty();
            }
            return ifThen;
        }

        if (statement instanceof ConditionDoUntilStatement stmt) {
            String condition = transformExpression(stmt.getCondition());
            PUMLoop loop = new PUMLoop("UNTIL", condition);
            for (Statement body : stmt.getBody()) {
                loop.getBody().add(transformStatement(cu, target, body));
            }
            return loop;
        }

        if (statement instanceof SetLowerLimitStatement stmt) {
            String searchArg = transformExpression(stmt.getSearchArgument());
            String reference = transformExpression(stmt.getName());
            //  REFINEMENT
            PUMInvoke pumInvoke = new PUMInvoke(file, reference, "Initialize cursor", List.of(searchArg, reference));
            Color entityColor = randomColor(reference);
            target.ensureHasEntity(reference, PUMLEntity.EntityType.DATABASE, entityColor);
            pumInvoke.setColor(darkerShade(entityColor));
            return pumInvoke;
        }

        if (statement instanceof ReadRecordStatement stmt) {
            String reference = transformExpression(stmt.getName());
            PUMInvoke pumInvoke = new PUMInvoke(file, reference, "READ", List.of(reference));
            Color entityColor = randomColor(reference);
            pumInvoke.setColor(darkerShade(entityColor));
            return pumInvoke;
        }

        if (statement instanceof WriteRecordStatement stmt) {
            String reference = transformExpression(stmt.getName());
            PUMInvoke pumInvoke = new PUMInvoke(file, reference, "WRITE", List.of(reference));
            Color entityColor = randomColor(reference);
            pumInvoke.setColor(darkerShade(entityColor));
            return pumInvoke;
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

    private Color randomColor(String name) {
        long code = name.hashCode();
        int red = sanitizeColorComponent(code * 11 + 73);
        int green = sanitizeColorComponent(code * 97 + 113);
        int blue = sanitizeColorComponent(code * 71 + 373);
        return new Color(red, green, blue);
    }

    private Color darkerShade(Color originalColor) {
        int red = sanitizeColorComponent(originalColor.getRed() / 2);
        int green = sanitizeColorComponent(originalColor.getGreen() / 2);
        int blue = sanitizeColorComponent(originalColor.getBlue() / 2);
        return new Color(red, green, blue);
    }

    private int sanitizeColorComponent(long value) {
        int v = (int)value;
        if (v < 0) {
            v = v * -1;
        }
        v = v % 255;
        return v;
    }
}
