package com.group4.cesc.pattern.command;

import com.group4.cesc.dao.MaintenanceDAO;

/** Brake inspection command. */
public class BrakeInspectionCommand extends AbstractMaintenanceCommand {
    private final MaintenanceDAO dao;

    public BrakeInspectionCommand(String scooterId, MaintenanceDAO dao) {
        super(scooterId);
        this.dao = dao;
    }

    @Override
    public void execute() {
        try {
            dao.updateTaskStatus(scooterId, "BRAKE_INSPECTION_DONE");
            completed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
