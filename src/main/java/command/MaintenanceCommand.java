package com.group4.cesc.pattern.command;

/** Command interface for maintenance actions. */
public interface MaintenanceCommand {
    void execute();
    boolean isCompleted();
    String getScooterId();
}
