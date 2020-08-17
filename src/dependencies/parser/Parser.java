package dependencies.parser;

import dependencies.executor.Executor;
import dependencies.dukeexceptions.*;

/**
 * Parser class which parses the given input. Checks if any command is given
 * and command is valid.
 */
public class Parser {
    /** Object for executing the commands. */
    private static final Executor executor = Executor.initExecutor();

    /**
     * Private constructor for a Parser object.
     */
    private Parser() {}

    /**
     * Initializer for Parser.
     *
     * @return the Parser
     */
    public static Parser initParser() {
        return new Parser();
    }

    /**
     * Parses given command and determines if it is a valid command,
     * and calls an executor to execute a valid command.
     *
     * @param command
     * @return reply: what was done
     */
    public String parseAndExec(String command) {
        Checker checker = new Checker(command);
        String reply;
        if (checker.containsTask()) {
            reply = executor.receiveAndExec(checker.getCommand(), checker.getTask());
        } else {
            reply = executor.receiveAndExec(command);
        }
        return reply;
    }


}
