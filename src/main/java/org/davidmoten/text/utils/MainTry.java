package org.davidmoten.text.utils;

public class MainTry {
    public static void main(String[] args) {
        String text = "hi there how are you going?";
        System.out.println(WordWrap.from(text).maxWidth(10).wrap());
    }
}
