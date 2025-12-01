package com.group4.cesc.pattern.command;

import com.group4.cesc.dao.MaintenanceDAO;

/** Replace tire command. */
public class ReplaceTireCommand extends AbstractMaintenanceCommand {

    private final MaintenanceDAO dao;

    public ReplaceTireCommand(String scooterId, MaintenanceDAO dao) {
        super(scooterId);
        this.dao = dao;
    }

    @Override
    public void execute() {
        try {
            dao.updateTaskStatus(scooterId, "TIRES_REPLACED");
            completed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
