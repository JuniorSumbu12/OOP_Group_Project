package com.group4.cesc.pattern.command;

import java.util.ArrayList;
import java.util.List;

/** Invoker that queues and runs commands. */
public class CommandInvoker {
    private final List<MaintenanceCommand> queue = new ArrayList<>();

    public void addCommand(MaintenanceCommand cmd) { queue.add(cmd); }

    public void executeAll() {
        for (MaintenanceCommand cmd : queue) {
            cmd.execute();
        }
        queue.clear();
    }
}
