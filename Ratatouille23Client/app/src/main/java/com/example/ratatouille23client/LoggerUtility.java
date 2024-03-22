package com.example.ratatouille23client;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtility {
    private static final Logger logger = Logger.getLogger(LoggerUtility.class.getName());

    public static void initializeLogger(Context context) {
        try {
            ContextWrapper contextWrapper = new ContextWrapper(context);
            FileHandler fh = new FileHandler(contextWrapper.getExternalFilesDir(null).getPath() + "/logfile.log");
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            logger.severe("Error initializing logger: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        if (logger == null) {
            Log.e("CustomLogger", "Logger not initialized. Call initializeLogger() first.");
        }
        return logger;
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logWarning(String message) {
        logger.warning(message);
    }

    public static void logError(String message, Throwable throwable) {
        logger.severe(message);
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }
}

