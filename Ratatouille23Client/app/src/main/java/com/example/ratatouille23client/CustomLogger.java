package com.example.ratatouille23client;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CustomLogger {
    private static Logger logger;

    public static void initializeLogger(Context context) {
        if (logger == null) {
            logger = Logger.getLogger(CustomLogger.class.getName());
            FileHandler fh;

            try {
                // Create a log directory in the app's internal storage
                File logDir = new File(context.getFilesDir(), "logs");
                if (!logDir.exists()) {
                    logDir.mkdirs();
                }

                // Create a FileHandler with log file path
                fh = new FileHandler(logDir.getPath() + "/RAT23Mobile.log");
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);

                // Set the FileHandler for the logger
                logger.addHandler(fh);

                // Disable the parent handlers to avoid duplicate logs
                logger.setUseParentHandlers(false);
            } catch (IOException e) {
                Log.e("CustomLogger", "Error occurred while initializing logger", e);
            }
        }
    }

    public static Logger getLogger() {
        if (logger == null) {
            Log.e("CustomLogger", "Logger not initialized. Call initializeLogger() first.");
        }
        return logger;
    }
}
