package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.job.Job;

/**
 * Adds a Job to the address book.
 */
public class AddJobCommand extends Command {

    public static final String COMMAND_WORD = "addjob";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a job to the model manager. " + "Parameters: "
            + PREFIX_JOB_TITLE + "JOB_TITLE "
            + PREFIX_JOB_ROUNDS + "NUMBER_OF_ROUNDS_OF_INTERVIEWS "
            + PREFIX_SKILL + "SKILLS ";

    public static final String MESSAGE_SUCCESS = "New job added: %1$s";
    public static final String MESSAGE_DUPLICATE_JOB = "This job already exists in the address book";
    public static final String MESSAGE_WRONG_VIEW = "This command is only available in job view. "
            + "Please switch to job view first using 'switchview' command.";

    private final Job toAdd;

    /**
     * Creates an AddJobCommand to add the specified {@code Job}
     */
    public AddJobCommand(Job job) {
        requireNonNull(job);
        toAdd = job;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check that we're in job view
        if (!model.isInJobView()) {
            throw new CommandException(MESSAGE_WRONG_VIEW);
        }

        if (model.hasJob(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_JOB);
        }

        model.addJob(toAdd);
        return CommandResult.withRefreshJobViewOnly(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AddJobCommand otherAddJobCommand)) {
            return false;
        }
        return this.toAdd.equals(otherAddJobCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("job", toAdd)
                .toString();
    }
}
