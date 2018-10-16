package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_VENUE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_VENUE_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.VENUE_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.VENUE_DESC_TUTORIAL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalEvents.AMY;
import static seedu.address.testutil.TypicalEvents.TUTORIAL;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.calendarevent.CalendarEvent;
import seedu.address.model.calendarevent.Description;
import seedu.address.model.calendarevent.Title;
import seedu.address.model.calendarevent.Venue;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.CalendarEventBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        CalendarEvent expectedCalendarEvent = new CalendarEventBuilder(TUTORIAL).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + TITLE_DESC_TUTORIAL + DESCRIPTION_DESC_TUTORIAL
            + VENUE_DESC_TUTORIAL + TAG_DESC_FRIEND, new AddCommand(expectedCalendarEvent));

        // multiple titles - last title accepted
        assertParseSuccess(parser, TITLE_DESC_LECTURE + TITLE_DESC_TUTORIAL + DESCRIPTION_DESC_TUTORIAL
            + VENUE_DESC_TUTORIAL + TAG_DESC_FRIEND, new AddCommand(expectedCalendarEvent));

        // multiple descriptions - last descriptions accepted
        assertParseSuccess(parser, TITLE_DESC_TUTORIAL + DESCRIPTION_DESC_LECTURE + DESCRIPTION_DESC_TUTORIAL
            + VENUE_DESC_TUTORIAL + TAG_DESC_FRIEND, new AddCommand(expectedCalendarEvent));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, TITLE_DESC_TUTORIAL + DESCRIPTION_DESC_TUTORIAL + VENUE_DESC_LECTURE
            + VENUE_DESC_TUTORIAL + TAG_DESC_FRIEND, new AddCommand(expectedCalendarEvent));

        // multiple tags - all accepted
        CalendarEvent expectedCalendarEventMultipleTags = new CalendarEventBuilder(TUTORIAL).withTags(VALID_TAG_FRIEND,
            VALID_TAG_HUSBAND)
            .build();
        assertParseSuccess(parser, TITLE_DESC_TUTORIAL + DESCRIPTION_DESC_TUTORIAL + VENUE_DESC_TUTORIAL
            + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, new AddCommand(expectedCalendarEventMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        CalendarEvent expectedCalendarEvent = new CalendarEventBuilder(AMY).withTags().build();
        assertParseSuccess(parser, TITLE_DESC_LECTURE + DESCRIPTION_DESC_LECTURE + VENUE_DESC_LECTURE,
            new AddCommand(expectedCalendarEvent));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing title prefix
        assertParseFailure(parser, VALID_TITLE_TUTORIAL + DESCRIPTION_DESC_TUTORIAL + VENUE_DESC_TUTORIAL,
            expectedMessage);

        // missing description prefix
        assertParseFailure(parser, TITLE_DESC_TUTORIAL + VALID_DESCRIPTION_TUTORIAL + VENUE_DESC_TUTORIAL,
            expectedMessage);

        // missing address prefix
        assertParseFailure(parser, TITLE_DESC_TUTORIAL + DESCRIPTION_DESC_TUTORIAL + VALID_VENUE_TUTORIAL,
            expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_TITLE_TUTORIAL + VALID_DESCRIPTION_TUTORIAL + VALID_VENUE_TUTORIAL,
            expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid title
        assertParseFailure(parser, INVALID_TITLE_DESC + DESCRIPTION_DESC_TUTORIAL + VENUE_DESC_TUTORIAL
            + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Title.MESSAGE_CONSTRAINTS);

        // invalid description
        assertParseFailure(parser, TITLE_DESC_TUTORIAL + INVALID_DESCRIPTION_DESC + VENUE_DESC_TUTORIAL
            + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Description.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, TITLE_DESC_TUTORIAL + DESCRIPTION_DESC_TUTORIAL + INVALID_VENUE_DESC
            + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Venue.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, TITLE_DESC_TUTORIAL + DESCRIPTION_DESC_TUTORIAL + VENUE_DESC_TUTORIAL
            + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_TITLE_DESC + DESCRIPTION_DESC_TUTORIAL + INVALID_VENUE_DESC,
            Title.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + TITLE_DESC_TUTORIAL + DESCRIPTION_DESC_TUTORIAL
                + VENUE_DESC_TUTORIAL + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
