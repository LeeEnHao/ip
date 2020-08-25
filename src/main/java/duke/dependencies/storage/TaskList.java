package duke.dependencies.storage;

import duke.dependencies.dukeexceptions.MissingListException;
import duke.dependencies.task.Schedulable;
import duke.dependencies.task.Task;


import java.util.ArrayList;

/**
 * Class that implements the list of Tasks the user has. Implemented with an ArrayList,
 *
 */
public class TaskList {

    /** Loader Object to read and write to save file. */
    private Storage l;

    /** todoList that stores the tasks. */
    private ArrayList<Schedulable> todoList;

    /** Private constructor */
    private TaskList() {
        l = new Storage(".", "data", "taskdata.txt");

        // Checks if there are any save files of todoList.
        // If there is, attempt to read the object as arraylist.
        // If there is none, instantiates the file.
        // And assigns the todolist to a new arraylist.
        if (l.isFilePresent()) {
            try {
                todoList = l.openAndReadObject();
            } catch (MissingListException e) {
//                e.printStackTrace();
                System.out.println("OOPS, there seems to be data corruption in the todolist!");
                System.out.println("Initialising new directory for saving your list...");
                todoList = new ArrayList<>();
            }
        } else {
            l.instantiateFile();
            todoList = new ArrayList<>();
        }
    }

    /**
     * Initialises and returns the Store object.
     *
     * @return the Store object
     */
    public static TaskList initStorage() {
        return new TaskList();
    }

    /**
     * Returns a String in the form of a list with \n appended
     * at the end of each item.
     *
     * @return Returns
     */
    public String getTodosInList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < todoList.size(); i++) {
            sb.append(i+1);
            sb.append(". ");
            sb.append(todoList.get(i).toString());
            if (i != todoList.size() - 1) {
                sb.append("\n");
            }
            if (i == todoList.size() - 1) {
                sb.append("\nSo stop procrastinating!");
            }
        }
        if (todoList.size() == 0) {
            sb.append("Theres's nothing here!\n")
                    .append("Try adding something to your list?");
        }

        return sb.toString();
    }

    /**
     * Adds the specified task to the todoList. Returns a string representation
     * of the task that was added as a reply.
     *
     * @param task Task object to be added.
     * @return String representing the newly added task.
     */
    public String add(Task task) {
        todoList.add(task);
        l.overwriteAndSave(todoList);
        return task.toString();
    }

    /**
     * Finds the given task at index and completes it.
     *
     * @param nums An array of numbers in string form.
     * @return String representing the newly completed task.
     */
    public String done(Integer[] nums) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nums.length; i++) {
            Schedulable t = todoList.get(nums[i] - 1);
            t.completed();
            sb.append(t.toString());
            if (i != nums.length - 1) {
                sb.append("\n");
            }
        }
        l.overwriteAndSave(todoList);
        return sb.toString();
    }

    /**
     * Finds all given task specified and deletes it from
     * list.
     *
     * @param nums An array of numbers in string form.
     * @return String representing the deleted task.
     */
    public String deleteTask(Integer nums) {
        Schedulable t = todoList.get(nums - 1);
        todoList.remove(nums - 1);
        l.overwriteAndSave(todoList);
        return t.toString();
    }

    /**
     * Returns the number of tasks in the todoList. Includes completed task.
     *
     * @return Size of the list as a String.
     */
    public int getListSize() {
        return todoList.size();
    }

    /**
     * Returns the number of completed tasks in the list.
     *
     * @return Integer number of completed items in the task list.
     */
    public int getNumOfCompleted() {
        int c = 0;
        for (int i = 0; i < todoList.size(); i++) {
            if (todoList.get(i).isCompleted()) {
                c++;
            }
        }
        return c;
    }

    /**
     * Returns the number of incomplete tasks in the list.
     *
     * @return Integer number of incomplete items in the task list.
     */
    public int getNumOfIncompleted() {
        int c = 0;
        for (int i = 0; i < todoList.size(); i++) {
            if (!todoList.get(i).isCompleted()) {
                c++;
            }
        }
        return c;
    }


}
