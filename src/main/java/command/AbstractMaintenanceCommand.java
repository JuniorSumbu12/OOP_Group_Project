package com.group4.cesc.pattern.command;

/** Shared base class. */
public abstract class AbstractMaintenanceCommand implements MaintenanceCommand {
    protected boolean completed = false;
    protected final String scooterId;
    public AbstractMaintenanceCommand(String scooterId) { this.scooterId = scooterId; }
    @Override public boolean isCompleted() { return completed; }
    @Override public String getScooterId() { return scooterId; }
}
