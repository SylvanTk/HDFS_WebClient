package com.company.Extensions;

public class ConsoleColors {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static String MakeRed(String text){
        return ANSI_RED + text + ANSI_RESET;
    }

    public static String MakeGreen(String text){
        return ANSI_GREEN + text + ANSI_RESET;
    }

    public static String MakeCyan(String text){
        return ANSI_CYAN + text + ANSI_RESET;
    }

    public static String MakePurple(String text){
        return ANSI_PURPLE + text + ANSI_RESET;
    }

    public static String MakeBlue(String text){
        return ANSI_BLUE + text + ANSI_RESET;
    }
}
