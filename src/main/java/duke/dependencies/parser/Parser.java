package duke.dependencies.parser;

import duke.dependencies.dukeexceptions.DukeException;
import duke.dependencies.dukeexceptions.EmptyTaskException;
import duke.dependencies.dukeexceptions.InvalidDateException;
import duke.dependencies.dukeexceptions.UnknownCommandException;
import duke.dependencies.executable.Command;
import duke.dependencies.executable.Executable;
import duke.dependencies.task.Task;
import duke.dependencies.task.TaskDate;

import java.util.Locale;
import java.util.regex.Pattern;

class Parser {
    private final Executable command;

    /**
     * Constructor for the checker object. Validates the command given/if and creates an Executable object
     * that can be passed to the executor.
     *
     * @param e String to be parsed into an Executable for Executor.
     */
    private Parser(Executable e) {
        this.command = e;
    }

    /**
     * Returns the Executable object.
     * Executable object holds the Task object inside.
     *
     * @return The Executable object.
     */
    public Executable getExecutable() {
        return this.command;
    }

    /**
     * Static factory method for constructing the checker object.
     * The Checker should have an Executable containing the task.
     *
     * @param s command
     * @return Checker object
     */
    public static Parser parseAndCheck(String s) throws DukeException {
        try {
            return parseExplicitCommand(s);
        } catch (DukeException e) {
            throw e;
        }
    }


    /* -------------------------------------- END OF PUBLIC METHODS ----------------------------------------------- */


    /**
     * Checks for command that is passed in explicit format.
     * eg.
     * 1) "event (task) /at (date)"
     * 2) "todo (task)"
     * 3) "deadline (task) /by (date)"
     *
     * @param s input
     * @return Checker object
     */
    private static Parser parseExplicitCommand(String s) throws DukeException {
        Executable e;

        /* LIST COMMAND */
        if (s.contains("list") || s.contains("List")) {
            e = Command.createListCommand(null);
        }
        /* DONE COMMAND */
        else if (checkForWord(s, "done")) {
            String task = cutOutTheWord(s, "done");
            if (task.isBlank() || task.isEmpty()) {
                throw new EmptyTaskException("Error: Done task cannot be empty");
            }
            Task t = Task.createMiscTask(task);
            e = Command.createDoneCommand(t);

        }
        /* TODO_COMMAND */
        else if (checkForWord(s, "todo")) {
            String task = cutOutTheWord(s, "todo");
            Task t = Task.createTodo(task);
            if (task.isEmpty() || task.isBlank()) {
                throw new EmptyTaskException("Error: Todo task cannot be empty");
            }
            e = Command.createAddCommand(t);

        }
        /* EVENT COMMAND */
        else if (checkForWord(s, "event")) {
            String task = cutOutTheWord(s, "event");
            if (task.isBlank() || task.isEmpty()) {
                throw new EmptyTaskException("Error: Event task cannot be empty");
            }
            String[] arr = task.split("/at");
            if (!TaskDate.isValidFormat(arr[1].trim())) {
                throw new InvalidDateException("Error: Date format not accepted.");
            }
            Task t = Task.createEvent(arr[0].trim(), arr[1].trim());
            e = Command.createAddCommand(t);

        }
        /* DEADLINE COMMAND */
        else  if (checkForWord(s, "deadline")) {
            String task = cutOutTheWord(s, "deadline");
            if (task.isEmpty() || task.isBlank()) {
                throw new EmptyTaskException("Error: Deadline tasks cannot be empty");
            }
            String[] arr = task.split("/by");
            if (!TaskDate.isValidFormat(arr[1].trim())) {
                throw new InvalidDateException("Error: Date format not accepted.");
            }
            Task t = Task.createDeadline(arr[0].trim(), arr[1].trim());
            e = Command.createAddCommand(t);

        }
        /* DELETE COMMAND */
        else if (checkForWord(s, "delete")) {
            String task = cutOutTheWord(s, "delete");
            if (task.isEmpty() || task.isBlank()) {
                throw new EmptyTaskException("Error: Task to be deleted cannot be empty");
            }
            Task t = Task.createMiscTask(task);
            e = Command.createDeleteCommand(t);

        }
        /* FIND COMMAND */
        else if (s.contains("find")) {
            String task = cutOutTheWord(s,"find");
            if (task.isEmpty() || task.isBlank()) {
                throw new EmptyTaskException("Error: Empty field for find: keyword");
            }
            Task t = Task.createMiscTask(task);
            e = Command.createFindCommand(t);
        }
        /* MYSTERIOUS ERROR/COMMAND */
        else {
            throw new UnknownCommandException("Error: Unknown command");
        }

        return new Parser(e);
    }


    /**
     * Case insensitive check for a word.
     * @param line
     * @param word
     * @return
     */
    private static boolean checkForWord(String line, String word) {
        return Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(line).find();
    }

    /**
     * Case insensitive cutting out of the word.
     * @param line
     * @param cmd
     * @return
     */
    private static String cutOutTheWord(String line, String cmd) {
        String c2 = cmd.toUpperCase(Locale.UK);
        String l2 = line.toUpperCase(Locale.UK);
        int idx = l2.indexOf(c2);
        return line.substring(idx + cmd.length()).trim();

    }

    private boolean isTodo(String s) {return false;}

    public boolean isValidCommand(String s) {
        return false;
    }

    /* ---------------------------------------- Additional Feature Section ------------------------------------------ */

    /**
     * Instead of parsing just natural language, this function will be able to parse a natural sentence.
     * eg. "I have a meeting on Monday." -> "event meeting /at Monday"
     * Remember to edit this only in the branch "add-ons".
     *
     * @param s
     * @return
     */
    private static Parser parseNaturalLanguage(String s) {

        return new Parser(null);

    }

}
