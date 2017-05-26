package com.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
    public static void main(String[] args) {
        String result = "asdfasfd8123µã";
        String score= result.replaceAll("(\\.)","");
        System.out.println(score);

        String s = "(\\d)";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
