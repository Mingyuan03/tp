package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditJobCommand;
import seedu.address.logic.commands.EditJobCommand.EditJobDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.skill.Skill;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditJobCommandParser implements Parser<EditJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditCommand and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditJobCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_JOB_TITLE, PREFIX_JOB_ROUNDS,
                PREFIX_SKILL);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditJobCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_JOB_TITLE, PREFIX_JOB_ROUNDS);

        EditJobDescriptor editJobDescriptor = new EditJobDescriptor();

        if (argMultimap.getValue(PREFIX_JOB_TITLE).isPresent()) {
            editJobDescriptor.setJobTitle(ParserUtil.parseJobTitle(argMultimap.getValue(PREFIX_JOB_TITLE).get()));
        }
        if (argMultimap.getValue(PREFIX_JOB_ROUNDS).isPresent()) {
            editJobDescriptor.setJobRounds(ParserUtil.parseJobRounds(argMultimap.getValue(PREFIX_JOB_ROUNDS).get()));
        }

        parseSkillsForEdit(argMultimap.getAllValues(PREFIX_SKILL)).ifPresent(editJobDescriptor::setSkills);

        if (!editJobDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditJobCommand.MESSAGE_NOT_EDITED);
        }

        return new EditJobCommand(index, editJobDescriptor);
    }

    /**
     * Parses {@code Collection<String> skills} into a {@code Set<Skill>} if
     * {@code skills} is non-empty. If {@code skills} contain only one element which is
     * an empty string, it will be parsed into a {@code Set<Skill>} containing zero
     * skills.
     */
    private Optional<Set<Skill>> parseSkillsForEdit(Collection<String> skills) throws ParseException {
        assert skills != null;

        if (skills.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> skillSet = skills.size() == 1 && skills.contains("") ? Collections.emptySet() : skills;
        return Optional.of(ParserUtil.parseSkills(skillSet));
    }
}
