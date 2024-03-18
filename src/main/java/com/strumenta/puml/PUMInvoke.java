package com.strumenta.puml;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an invocation statement in a PlantUML diagram.
 * This class models the concept of a method or procedure call, including details
 * about the caller, receiver, method name, parameters, and the body of the invocation.
 */
public class PUMInvoke extends PUMLStatement {
    private final String caller;
    private final String receiver;
    private final String method;
    private final List<String> params;
    private final List<PUMLStatement> body;

    /**
     * Constructs a PUMInvoke statement with specified caller, receiver, method, and parameters.
     *
     * @param caller   The entity that initiates the call.
     * @param receiver The entity that receives the call.
     * @param method   The name of the method being invoked.
     * @param params   The list of parameters passed to the method.
     */
    public PUMInvoke(String caller, String receiver, String method, List<String> params) {
        this.caller = caller;
        this.receiver = receiver;
        this.method = method;
        this.params = new ArrayList<>(params);
        this.body = new ArrayList<>();
    }

    /**
     * Retrieves the caller of the invocation.
     *
     * @return The name of the caller.
     */
    public String getCaller() {
        return caller;
    }

    /**
     * Retrieves the receiver of the invocation.
     *
     * @return The name of the receiver.
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Retrieves the method name of the invocation.
     *
     * @return The method name.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Retrieves the parameters of the invocation.
     *
     * @return A list of parameters as strings.
     */
    public List<String> getParams() {
        return params;
    }

    /**
     * Retrieves the body of the invocation, which may contain additional PUML statements.
     *
     * @return A list of PUMLStatement objects representing the body of the invocation.
     */
    public List<PUMLStatement> getBody() {
        return body;
    }
}
