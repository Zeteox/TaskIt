package ovh.zeteox.taskit.tasks;

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
    public void addNumber() {
        this.actualNumber++;
    }

    public void removeNumber() {
        this.actualNumber--;
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
}
