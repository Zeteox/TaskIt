package ovh.zeteox.taskit.tasks;

import ovh.zeteox.taskit.config.ModClientConfig;

import java.util.List;
import java.util.Objects;

public class Task {
    private final String taskName;
    private final TaskTypes type;
    private final int numberToDo;
    private int actualNumber;
    private boolean pined;
    private boolean completed;

    public Task(String taskName, TaskTypes type, int numberToDo, int actualNumber, boolean pined, boolean completed) {
        this.taskName = taskName;
        this.type = type;
        this.numberToDo = numberToDo;
        this.actualNumber = actualNumber;
        this.pined = pined;
        this.completed = completed;
    }

    public String getTaskName() {
        return taskName;
    }

    public TaskTypes getTaskType() {
        return this.type;
    }

    public int getNumberOfTime() {
        return this.numberToDo;
    }

    public int getActualNumber() {
        return this.actualNumber;
    }
    public void addNumber(int number) {
        if (!this.isCompleted()) {
            List<Task> allTasks = ModClientConfig.getTasks();
            (allTasks.get(allTasks.indexOf(this))).actualNumber += 1;
            this.actualNumber += number;
            if (this.getActualNumber() == this.getNumberOfTime()) {
                (allTasks.get(allTasks.indexOf(this))).setCompleted(true);
                this.setCompleted(true);
            }
            ModClientConfig.updateTasks(allTasks);
        }
    }

    public void removeNumber(int number) {
        if (this.getActualNumber() > 0) {
            List<Task> allTasks = ModClientConfig.getTasks();
            (allTasks.get(allTasks.indexOf(this))).actualNumber -= 1;
            this.actualNumber -= number;
            if (this.getActualNumber() != this.getNumberOfTime()) {
                (allTasks.get(allTasks.indexOf(this))).setCompleted(false);
                this.setCompleted(false);
            }
            ModClientConfig.updateTasks(allTasks);
        }
    }

    public boolean isPined() {
        return this.pined;
    }

    public void setPined(boolean pined) {
        this.pined = pined;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(this.taskName, task.taskName)
                && this.type == task.type
                && this.actualNumber == task.actualNumber
                && this.numberToDo == task.numberToDo
                && this.pined == task.pined
                && this.completed == task.completed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.taskName,
                this.type,
                this.actualNumber,
                this.numberToDo,
                this.pined,
                this.completed);
    }
}
