package com.mygdx;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Logger {

    private static BufferedWriter bw;

    public static void init() {
        try {
            bw = new BufferedWriter(new FileWriter("log.txt"));
        } catch (Exception e) {
            System.out.println("You bozo deleted the log file");
        }
    }

    public static void log(String s) {
        try {
            bw.write(s);
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
