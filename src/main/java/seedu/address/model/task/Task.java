package seedu.address.model.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import seedu.address.logic.parser.TimeParser;
import seedu.address.model.Module;

/**
 * pending.
 */
public class Task {
    /**
     * The acceptable data and time format.
     */
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private Module module;
    private TaskType taskType;
    private String taskName;
    private String taskDescription;
    private double weight;
    private TaskStatus taskStatus;
    private LocalDateTime[] dateTimes;
    private String estimatedTimeCost;

    public Task(Module module, TaskType taskType, String taskName, String taskDescription, double weight,
                TaskStatus taskStatus, LocalDateTime[] dateTimes, String estimatedTimeCost) {
        this.module = module;
        this.taskType = taskType;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.weight = weight;
        this.taskStatus = taskStatus;
        this.dateTimes = dateTimes;
        this.estimatedTimeCost = estimatedTimeCost;
    }

    public Task() {
        dateTimes = new LocalDateTime[2];
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setStatus(String status) {
        switch (status) {
        case "pending":
            this.taskStatus = TaskStatus.PENDING;
            break;
        case "finished":
            this.taskStatus = TaskStatus.FINISHED;
            break;
        default:
        }
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTimeString() {
        String timeString = "";
        if (dateTimes.length == 1) {
            timeString = TimeParser.getDateTimeString(dateTimes[0]);
        } else if (dateTimes.length == 2) {
            timeString = TimeParser.getDateTimeString(dateTimes[0]) + "-" + TimeParser.getDateTimeString(dateTimes[0]);
        }
        return timeString;
    }

    public LocalDateTime[] getDateTimes() {
        return dateTimes;
    }

    //need to add the outOfRangeExceptionHandler
    public void setDateTimes(LocalDateTime[] dateTimes) {
        this.dateTimes = dateTimes;
    }

    public String getEstimatedTimeCost() {
        return estimatedTimeCost;
    }

    public void setEstimatedTimeCost(String estimatedTimeCost) {
        this.estimatedTimeCost = estimatedTimeCost;
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(module, taskType, taskName, taskDescription, weight, taskStatus, dateTimes);
    }

    /**
     * pending change
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return otherTask.getTaskName().equals(getTaskName())
            && otherTask.getModule().equals(getModule())
            && otherTask.getTimeString().equals(getTimeString())
            && otherTask.getTaskType().equals(getTaskType())
            && otherTask.getTaskDescription().equals(getTaskDescription())
            && otherTask.getTaskStatus().equals(getTaskStatus());
    }

    @Override
    public String toString() {
        String string = "";
        return string;
    }

    /**
     * Pending change
     * Returns true if both task of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }

        return otherTask != null
            && otherTask.getTaskName().equals(getTaskName())
            && otherTask.getModule().equals(getModule())
            && otherTask.getTimeString().equals(getTimeString())
            && otherTask.getTaskType().equals(getTaskType())
            && otherTask.getTaskDescription().equals(getTaskDescription())
            && otherTask.getTaskStatus().equals(getTaskStatus());
    }

}