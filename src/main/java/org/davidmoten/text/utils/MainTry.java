package org.davidmoten.text.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class MainTry {
    public static void main(String[] args) {
        // trying leftTrim() method
        String text = "              hi there! how are you going?";
        StringBuilder2 txt = new StringBuilder2(text);
        WordWrap.leftTrim(txt);
        System.out.println("Before WordWrap.leftTrim(): " + text);
        System.out.println("After:");
        System.out.println(txt);

        // trying reading from file and outputting wrapped text to different file
        WordWrap.from(new File("something.txt"), StandardCharsets.UTF_8)
                .maxWidth(15)
                .wrapUtf8("something2.txt");

        // checking hyphens are being inserted correctly
        System.out.println();
        System.out.println(WordWrap.from(txt)
                .maxWidth(4)
                .insertHyphens(true)
                .wrap());

        System.out.println("Hello");

    }
}
