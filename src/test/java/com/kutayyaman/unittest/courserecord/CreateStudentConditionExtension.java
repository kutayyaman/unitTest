package com.kutayyaman.unittest.courserecord;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateStudentConditionExtension implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        if (new ArrayList<String>(Arrays.asList("create")).containsAll(context.getTags())) {
            return ConditionEvaluationResult.enabled("create student is enabled");
        }
        return ConditionEvaluationResult.disabled("Only create student allowed to run");
    }
}
