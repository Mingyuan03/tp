package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYMENT_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_SKILLS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.job.Job;

/**
 * Adds a {@code Job} to the address book.
 */
public class AddJobCommand extends Command {

    public static final String COMMAND_WORD = "addjob";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a job to the model manager. " + "Parameters: "
            + PREFIX_JOB_TITLE + "JOB TITLE "
            + PREFIX_JOB_COMPANY + "COMPANY'S NAME "
            + PREFIX_JOB_ROUNDS + "NUMBER OF ROUNDS OF INTERVIEWS "
            + PREFIX_JOB_SKILLS + "SKILLS "
            + PREFIX_JOB_ADDRESS + "COMPANY ADDRESS "
            + PREFIX_EMPLOYMENT_TYPE + "JOB TYPE";
    public static final String MESSAGE_SUCCESS = "New job added: %1$s";
    public static final String MESSAGE_DUPLICATE_JOB = "This job already exists in the address book";

    private final Job jobToAdd;

    /**
     * Creates an AddJobCommand to add the specified {@code Job}.
     * @param job Job parsed from {@code AddJobCommandParser::parse}.
     */
    public AddJobCommand(Job job) {
        requireNonNull(job);
        this.jobToAdd = job;
    }

    /**
     * Executes {@code AddJobCommand} and returns the resulting success or failure message.
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display.
     * @throws CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasJob(this.jobToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_JOB);
        }
        model.addJob(this.jobToAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(this.jobToAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof AddJobCommand otherAddJobCommand)) {
            return false;
        }
        return this.jobToAdd.equals(otherAddJobCommand.jobToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("toAddJob", this.jobToAdd).toString();
    }
}
