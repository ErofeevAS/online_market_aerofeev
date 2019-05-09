package com.gmail.erofeev.st.alexei.onlinemarket.controller.util;

import java.util.HashMap;
import java.util.Map;

public class CustomWrapper {
    private Map<Long, Boolean> changes;
    private Integer testVariable = 10;

    public CustomWrapper() {
        changes = new HashMap<>();
        changes.put(11L, true);
        changes.put(131L, false);
        changes.put(141L, false);
    }

    public CustomWrapper(Map<Long, Boolean> changes) {
        this.changes = changes;
    }

    public Map<Long, Boolean> getChanges() {
        return changes;
    }

    public void setChanges(Map<Long, Boolean> changes) {
        this.changes = changes;
    }

    public Integer getTestVariable() {
        return testVariable;
    }

    public void setTestVariable(Integer testVariable) {
        this.testVariable = testVariable;
    }

    @Override
    public String toString() {
        return "CustomWrapper{" +
                "changes=" + changes +
                ", testVariable=" + testVariable +
                '}';
    }
}
