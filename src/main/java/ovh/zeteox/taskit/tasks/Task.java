package ovh.zeteox.taskit.tasks;

import ovh.zeteox.taskit.config.ModClientConfig;

import java.util.List;
import java.util.Objects;

/**
 * This class represents a task in the TaskIt mod.
 * It contains information about the task such as its name, type, number of times to do it,
 * actual number done, whether it's pinned or completed, and the task item.
 */
public class Task {
    private final String taskName;
    private final TaskTypes type;
    private final int numberToDo;
    private int actualNumber;
    private boolean pinned;
    private boolean completed;
    private final String taskItem;

    /**
     * Constructor for the Task class.
     *
     * @param taskName    The name of the task.
     * @param type        The type of the task.
     * @param numberToDo  The number of times to do the task.
     * @param actualNumber The actual number done.
     * @param pined       Whether the task is pinned or not.
     * @param completed   Whether the task is completed or not.
     * @param taskItem    The item associated with the task.
     */
    public Task(String taskName, TaskTypes type, int numberToDo, int actualNumber, boolean pined, boolean completed, String taskItem) {
        this.taskName = taskName;
        this.type = type;
        this.numberToDo = numberToDo;
        this.actualNumber = actualNumber;
        this.pinned = pined;
        this.completed = completed;
        this.taskItem = taskItem;
    }

    /**
     * This method returns the name of the task.
     *
     * @return {@code String} The name of the task.
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * This method returns the type of the task.
     *
     * @return {@code TaskTypes} The type of the task.
     */
    public TaskTypes getTaskType() {
        return this.type;
    }

    /**
     * This method returns the number of times to do the task.
     *
     * @return {@code int} The number of times to do the task.
     */
    public int getNumberOfTime() {
        return this.numberToDo;
    }

    /**
     * This method returns the actual number done.
     *
     * @return {@code int} The actual number done.
     */
    public int getActualNumber() {
        return this.actualNumber;
    }

    /**
     * This method add {@code x} to the actual number done.
     * If the actual number done equals the number of times to do the task,
     * it sets the task as completed.
     *
     * @param x {@code int} The number to add to the actual number done.
     */
    public void addNumber(int x) {
        if (!this.isCompleted()) {
            List<Task> allTasks = ModClientConfig.getTasks();
            (allTasks.get(allTasks.indexOf(this))).actualNumber += 1;
            this.actualNumber += x;
            if (this.getActualNumber() == this.getNumberOfTime()) {
                (allTasks.get(allTasks.indexOf(this))).setCompleted(true);
                this.setCompleted(true);
            }
            ModClientConfig.updateTasks(allTasks);
        }
    }

    /**
     * This method remove {@code x} from the actual number done.
     * If the actual number done is less than the number of times to do the task,
     * it sets the task as not completed.
     *
     * @param x {@code int} The number to remove from the actual number done.
     */
    public void removeNumber(int x) {
        if (this.getActualNumber() > 0) {
            List<Task> allTasks = ModClientConfig.getTasks();
            (allTasks.get(allTasks.indexOf(this))).actualNumber -= 1;
            this.actualNumber -= x;
            if (this.getActualNumber() != this.getNumberOfTime()) {
                (allTasks.get(allTasks.indexOf(this))).setCompleted(false);
                this.setCompleted(false);
            }
            ModClientConfig.updateTasks(allTasks);
        }
    }

    /**
     * This method return if the task is pinned or not.
     *
     * @return {@code boolean} True if the task is pinned, false otherwise.
     */
    public boolean isPinned() {
        return this.pinned;
    }

    /**
     * This method set the task as pinned or not.
     *
     * @param pinned {@code boolean} True if the task is pinned, false otherwise.
     */
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    /**
     * This method return if the task is completed or not.
     *
     * @return {@code boolean} True if the task is completed, false otherwise.
     */
    public boolean isCompleted() {
        return this.completed;
    }

    /**
     * This method set the task as completed or not.
     *
     * @param completed {@code boolean} True if the task is completed, false otherwise.
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * This method return the name as a {@code String}
     * of the item associated with the task.
     *
     * @return {@code String} The name of the item.
     */
    public String getTaskItem() {
        return taskItem;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(this.taskName, task.taskName)
                && this.type == task.type
                && this.actualNumber == task.actualNumber
                && this.numberToDo == task.numberToDo
                && this.pinned == task.pinned
                && this.completed == task.completed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.taskName,
                this.type,
                this.actualNumber,
                this.numberToDo,
                this.pinned,
                this.completed);
    }
}
