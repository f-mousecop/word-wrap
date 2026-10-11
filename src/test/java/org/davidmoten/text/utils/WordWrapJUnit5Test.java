package org.davidmoten.text.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WordWrapJUnit5Test {

    /**
     * First boundary test case: on point (exactly at the maximum width)
     *<p>
     *     We set the max width exactly equal to the number of characters in the input string:
     *     {@code maxWidth(n)}, where {@code n == input.length()}.
     *</p>
     *
     * <p>
     *     <strong>Expected result:</strong> the string does not wrap because
     * {@code "cat dog"} contains 7 characters, including the space
     * </p>
     *
     * @see WordWrap
     */
    @Test
    void doesNotWrapWhenTextFitsExactlyWithinWidth() {
        String input = "cat dog";

        String actual = WordWrap.from(input)
                .maxWidth(7)
                .wrap();

        // compare the expected output do see if characters are counted correctly
        assertEquals("cat dog", actual);
    }

    /**
     * Second boundary test: just below the boundary (one below the text length)
     * <p>
     *     We set the max width one below the number of characters in the input string:
     *     {@code maxWidth(n)}, where {@code n < input.length()}.
     * </p>
     *
     * <p>
     *     <strong>Expected result:</strong> the string will wrap
     * </p>
     */
    @Test
    void doesWrapWhenMaxWidthIsOneBelowTextLength() {
        String input = "cat dog";

        String actual = WordWrap.from(input)
                .maxWidth(6)
                .wrap();

        assertEquals("cat\ndog", actual);
    }


    /**
     * Third boundary test: just above the boundary (one above text length)
     *
     * <p>
     *     We set the max width one above the number of characters in the input string:
     *     {@code maxWidth(n)}, where {@code n > input.length()}.
     * </p>
     *
     * <p>
     *     <strong>Expected result:</strong> the string will not wrap
     * </p>
     */
    @Test
    void doesNotWrapWhenMaxWidthIsOneAboveTextLength() {
        String input = "cat dog";

        String actual = WordWrap.from(input)
                .maxWidth(8)
                .wrap();

        assertEquals("cat dog", actual);
    }


    /**
     * Invalid equivalence partition: {@code maxWidth()} must be greater than zero
     *
     * <p>
     *     We give max width a negative integer:
     *     {@code maxWidth(n)}, where {@code n < 0}.
     * </p>
     *
     * <p>
     *     <strong>Expected result:</strong> an {@code IllegalArgumentException} is thrown
     * </p>
     *
     * @see WordWrap
     */
    @Test
    void negativeMaxWidthThrowsIllegalArgumentException() {
        String input = "cat dog";

        // use a lambda expression for JUnit 5's assertThrows
        assertThrows(
                IllegalArgumentException.class,
                () -> WordWrap.from(input)
                        .maxWidth(-1)
        );
    }

    /**
     * Boundary test involving extra whitespace
     */
    @Test
    void willTrimExtraWhiteSpaceAndWrapWhenExceedsMaxWidth() {
        String input = "cat      dog";
        String actual = WordWrap.from(input)
                .maxWidth(7)
                .wrap();
        assertEquals("cat\ndog", actual);
    }

    /**
     * =======================================================================
     * leftTrim tests (similar to preexisting JUnit4 suite - just for reference
     * =======================================================================
     **/

    @Test
    void willTrimExtraWhiteSpaceBeforeWord() {
        StringBuilder2 input = new StringBuilder2("         cat");
        WordWrap.leftTrim(input);
        assertEquals("cat", input.toString());
    }

    @Test
    void willTrimExtraWhiteSpaceBeforeWord1() {
        StringBuilder2 input = new StringBuilder2(" cat");
        WordWrap.leftTrim(input);
        assertEquals("cat", input.toString());
    }

    @Test
    void willTrimExtraWhiteSpaceBeforeWord2() {
        StringBuilder2 input = new StringBuilder2("cat");
        WordWrap.leftTrim(input);
        assertEquals("cat", input.toString());
    }

    @Test
    void willTrimExtraWhiteSpaceBeforeWord3() {
        StringBuilder2 input = new StringBuilder2("");
        WordWrap.leftTrim(input);
        assertEquals("", input.toString());
    }


    /**
     * =======================================================================
     * Tests exercising relevant paths in the writeBrokenWord CFG and DU chains
     * =======================================================================
     **/


    /**
     * DU chains:
     * D602(insertHyphens) -> U606(insertHyphens)
     * D607(x) -> U608(x)
     * <p>
     * No data-flow anomaly expected: <code>x</code> is assigned before being used to write the substring
     */
    @Test
    void breaksLongWordsWithHyphens() {
        String input = "elephant";
        String actual = WordWrap.from(input)
                .maxWidth(4)
                .insertHyphens(true)
                .wrap();
        assertEquals("ele-\npha-\nnt", actual);
    }

    /**
     * DU chains:
     * D602(insertHyphens) -> U606(insertHyphens)
     * D613(prefix) -> U614(prefix)
     * D613(prefix) -> U615(prefix)
     * <p>
     *
     * No data-flow anomaly expected: prefix is initialized before being checked
     * and written to the output
     */
    @Test
    void breaksLongWordWithoutHyphens() {
        String input = "elephant";
        String actual = WordWrap.from(input)
                .maxWidth(4)
                .insertHyphens(false)
                .wrap();
        assertEquals("elep\nhant", actual);
    }


    /**
     * DU chains:
     * D602(word) -> U606(word)
     * D613(prefix) -> U614(prefix)
     * D613(prefix) -> U615(prefix)
     * <p>
     * No data-flow anomaly expected: prefix is defined before use
     */
    @Test
    void skipsHyphenInsertWhenWordIsTooShort() {
        String input = "cat";
        String actual = WordWrap.from(input)
                .maxWidth(1)
                .insertHyphens(true)
                .wrap();
        assertEquals("c\na\nt", actual);
    }


    /**
     * DU chains:
     * D607(x) -> No subsequent use on this path
     * D613(prefix) -> U614(prefix)
     * D613(prefix) -> U615(prefix)
     * <p>
     * Uses extraWordChars(" ") to treat spaces as word characters,
     * allowing the third condition to evaluate to false.
     * Verifies that the fallback branch writes a non-whitespace prefix.
     * <p>
     * No use-before-definition anomaly expected. x is assigned but
     * not used because the hyphen-insertion condition is false
     */
    @Test
    void skipsHyphenWhenPrefixStartsWithWhitespace() {
        String actual = WordWrap.from("  ab")
                .maxWidth(3)
                .extraWordChars(" ")
                .insertHyphens(true)
                .wrap();
        assertEquals("  a\nb", actual);
    }

    /**
     * DU chains:
     * D607(x) -> No subsequent use on this path
     * D613(prefix) -> U614(prefix)
     * <p>
     * Uses extraWordChars(" ") to produce a whitespace-only prefix.
     * Verifies that the prefix is not written when it contains
     * only whitespace, while the newline is still generated.
     * <p>
     * No use-before-definition anomaly expected. Both x and prefix
     * are assigned, but neither is written to the output on this path
     */
    @Test
    void skipsWhiteSpaceOnlyPrefix() {
        String actual = WordWrap.from("   a")
                .maxWidth(3)
                .extraWordChars(" ")
                .insertHyphens(true)
                .wrap();
        assertEquals("\na", actual);
    }
}
