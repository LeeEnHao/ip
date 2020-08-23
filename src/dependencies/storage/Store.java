package dependencies.storage;

import dependencies.dukeexceptions.MissingListException;
import dependencies.task.Schedulable;
import dependencies.task.Task;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;


public class Store {

    /** Loader Object to read and write to save file. */
    private Loader l;

    /** todoList that stores the tasks. */
    private ArrayList<Schedulable> todoList;

    /** Private constructor */
    private Store() {
        l = new Loader(".", "data", "taskdata.txt");
        if (l.isFilePresent()) {
            try {
                todoList = l.openAndReadObject();
            } catch (MissingListException e) {
                e.printStackTrace();
                System.out.println("Error in save file: Helping you to initialise a new list!!!!!");
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
    public static Store initStorage() {
        return new Store();
    }

    /**
     * Returns a String in the form of a list with \n appended
     * at the end of each item.
     *
     * @return todoList in a form of a String
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
     * @param task
     * @return a string represenitng the newly added task
     */
    public String add(Task task) {
        todoList.add(task);
        l.overwriteAndSave(todoList);
        return task.toString();
    }

    /**
     * Finds the given task at index and completes it.
     *
     * @param nums am array of numbers in string form
     * @return a string represenitng the newly completed task.
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
     * @param nums
     * @return
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
     * @return size of the list as a String
     */
    public int getListSize() {
        return todoList.size();
    }

    /**
     * Returns the number of completed tasks in the list.
     *
     * @return number of completed tasks
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
     * @return number of incomplete tasks
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

    /* ---------------------------------------- Serialisation Methods ------------------------------------ */

//    @SuppressWarnings("unchecked")
//    private ArrayList<Schedulable> read(
//            ObjectInputStream ois
//    ) throws IOException, ClassNotFoundException {
//
//        return (ArrayList<Schedulable>) ois.readObject();
//    }
//
//    /**
//     * Saves the todoList to the taskdata.txt file.
//     * Overwrites the data on the taskdata.txt file.
//     *
//     * @throws IOException
//     */
//    private void save() {
//        try {
//            FileOutputStream fos = new FileOutputStream(FILE_PATH.toString(), false);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(todoList);
//            oos.close();
//            System.out.println("Successfully saved your data.");
//        } catch (IOException e) {
//            System.out.println("Error in saving your data: " + e.getMessage());
//        }
//    }
//
//    private void instantiateSaveFile() {
//        try {
//            if (Files.exists(DIR_PATH)) {
//                System.out.println("Dir exist");
//                if (Files.exists(FILE_PATH)) {
//                    System.out.println("File exists");
//                } else {
//                    Files.createFile(FILE_PATH);
//                    System.out.println("File created");
//                }
//            } else {
//                // Directory and storage file not present
//                Files.createDirectory(DIR_PATH);
//                System.out.println("Dir created");
//                Files.createFile(FILE_PATH);
//                System.out.println("File created");
//            }
//        } catch (IOException e) {
//            System.out.println("Unexpected error occurred while trying to create a file to save your data.");
//        }
//    }
//
//    private void openAndRead() throws IOException, ClassNotFoundException {
//        FileInputStream fis = new FileInputStream(FILE_PATH.toString());
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        todoList = read(ois);
//        ois.close();
//    }

    /* ---------------------------------- END OF SERIALIZABLE METHODS ---------------------------------- */

    public static void main(String[] args) {
        Store s = initStorage();
        s.add(Task.createEvent("Meeting", "Monday"));
        System.out.println(s.getTodosInList());
    }

}
