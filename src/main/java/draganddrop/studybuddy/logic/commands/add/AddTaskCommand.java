package draganddrop.studybuddy.logic.commands.add;

import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.Task;

/**
 *
 */
public class AddTaskCommand extends Command {

    final public static String COMMAND_WORD = "add";

    final public static String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task list. "
        + "Parameters: TaskName, TaskType, TaskDateTime";

    final public static String MESSAGE_SUCCESS = "New Task added: %1$s";
    final public static String MESSAGE_DUPLICATE_TASK = "This Task already exists. Are you sure you want to proceed?";

    private final Task toAdd;

    /**
     * Creates an AddTaskCommand to add the specified {@code Task}
     */
    public AddTaskCommand(Task task) {
        requireNonNull(task);
        this.toAdd = task;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasTask(toAdd)) {
            if (!toAdd.isDuplicate()) {
                toAdd.setDuplicate(true);
                return new CommandResult(String.format(MESSAGE_DUPLICATE_TASK, toAdd));
            }
        }
        model.addTask(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof AddTaskCommand // instanceof handles nulls
            && toAdd.equals(((AddTaskCommand) other).toAdd));
    }

}
