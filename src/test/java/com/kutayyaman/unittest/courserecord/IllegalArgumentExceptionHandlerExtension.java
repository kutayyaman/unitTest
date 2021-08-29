package com.kutayyaman.unittest.courserecord;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.util.logging.Logger;

public class IllegalArgumentExceptionHandlerExtension implements TestExecutionExceptionHandler {

    private static Logger logger = Logger.getLogger(IllegalArgumentExceptionHandlerExtension.class.getName());

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        logger.severe(String.format("IllegalArgumentException was thrown by a method %s with description: %s", context.getRequiredTestMethod().getName(), throwable.getMessage()));
        throw throwable; //bunu kaldirirsak test exception throw etmez sadece ekrana logu basar yani o testi basarili sayar ama throw yaparsak bu sekilde o zaman test basarisiz olur ama loguda basar ekrana
    }
}
