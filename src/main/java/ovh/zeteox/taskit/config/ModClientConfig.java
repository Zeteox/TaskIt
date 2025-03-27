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

public class ModClientConfig {
    private static final String MOD_CONFIG = "config/taskitconfig.json";
    private static List<Task> tasks = new ArrayList<>();

    /**
     * {@code saveConfig()} is a method of the ModClientConfig class that permit
     * to save the mod data in the config file
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
     * {@code loadConfig()} is a method from the ModClientConfig class that permit
     * to load the configs or initialize it.
     */
    public static void loadConfig() {
        File configFile = new File(MOD_CONFIG);
        if (!configFile.exists()) {
            saveConfig(); //initialise the file with nothing in it
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, List<Task>>>(){}.getType();
            HashMap<String, Object> config = gson.fromJson(reader, type);

            tasks = (List<Task>) config.get("tasks");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addTask(Task task) {
        loadConfig();
        tasks.add(task);
        saveConfig();
    }

    public static void removeTask(Task task) {
        loadConfig();
        tasks.remove(task);
        saveConfig();
    }

    public static List<Task> getTasks() {
        loadConfig();
        return tasks;
    }

    public static void updateTasks(List<Task> updTasks) {
        tasks = updTasks;
        saveConfig();
    }
}
