/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.file;

/**
 * todo Document type TestFile
 */
public class TestFile {

    public static void main(String[] args) {
        FileReadQueueHandler fileReadQueueHandler = new FileReadQueueHandler(1);
        fileReadQueueHandler.getThreadPool().shutdown();
    }

}
