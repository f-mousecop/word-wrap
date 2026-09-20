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
}
