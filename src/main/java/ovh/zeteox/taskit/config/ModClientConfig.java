package ovh.zeteox.taskit.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ovh.zeteox.taskit.tasks.Task;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@code ModClientConfig} is a class that permit to manage the mod config file
 * and the tasks in it.
 */
public class ModClientConfig {
    private static final String MOD_CONFIG = "config/taskitconfig.json";
    private static List<Task> tasks = new ArrayList<>();

    /**
     * {@code saveConfig()} is a method of the {@link ModClientConfig} class that permit
     * to save the mod data in the config file.
     */
    public static void saveConfig() {
        HashMap<String, List<Task>> taskMap = new HashMap<>();
        taskMap.put("tasks", tasks);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(taskMap);

        try (FileWriter writer = new FileWriter(MOD_CONFIG)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@code loadConfig()} is a method from the {@link ModClientConfig} class that permit
     * to load the config files or initialize them to use them.
     */
    public static void loadConfig() {
        File configFile = new File(MOD_CONFIG);
        if (!configFile.exists()) {
            saveConfig(); //initialise the file with nothing in it
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            Gson gson = new Gson();
            // Use TypeToken to specify the type of the object to get a Hashmap from the JSON
            Type type = new TypeToken<HashMap<String, List<Task>>>(){}.getType();
            HashMap<String, List<Task>> config = gson.fromJson(reader, type);

            tasks = config.get("tasks");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@code addTasks()} is a method from the {@link ModClientConfig} class that permit
     * to add a task to the tasks list saved in the config file.
     *
     * @param task {@code Task} to add in the config file
     */
    public static void addTask(Task task) {
        loadConfig();
        tasks.add(task);
        saveConfig();
    }

    /**
     * {@code removeTask()} is a method from the {@link ModClientConfig} class that permit
     * to remove a task from the tasks list saved in the config file.
     *
     * @param task {@code Task} to remove from the config file
     */
    public static void removeTask(Task task) {
        loadConfig();
        tasks.remove(task);
        saveConfig();
    }

    /**
     * {@code getTasks()} is a method from the {@link ModClientConfig} class that permit
     * to get the tasks list saved in the config file.
     *
     * @return {@code List<Task>} the tasks list saved in the config file
     */
    public static List<Task> getTasks() {
        loadConfig();
        return tasks;
    }

    /**
     * {@code updateTasks()} is a method from the {@link ModClientConfig} class that permit
     * to update the config file with the given list of tasks.
     *
     * @param updTasks {@code List<Task>} to put in the config file
     */
    public static void updateTasks(List<Task> updTasks) {
        tasks = updTasks;
        saveConfig();
    }
}
