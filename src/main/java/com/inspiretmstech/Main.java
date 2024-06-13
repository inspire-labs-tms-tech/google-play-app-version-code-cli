package com.inspiretmstech;

import picocli.CommandLine;

public class Main {

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new CLI());
        commandLine.setCaseInsensitiveEnumValuesAllowed(true);
        System.exit(commandLine.execute(args));
    }

}
