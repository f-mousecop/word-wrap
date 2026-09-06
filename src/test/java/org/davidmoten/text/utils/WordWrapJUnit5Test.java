package org.davidmoten.text.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordWrapJUnit5Test {

    @Test
    void wrapsAtSpaceWhenNextWordDoesNotFit() {
        // choose the input
        String input = "hello there";

        // call WordWrap.from()
        String actual = WordWrap.from(input)
                .maxWidth(6)
                .wrap();

        // compare expected output with actual output
        assertEquals("hello\nthere", actual);
        System.out.println(actual);
    }
}
