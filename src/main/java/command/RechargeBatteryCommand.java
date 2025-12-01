package com.group4.cesc.pattern.command;

import com.group4.cesc.dao.MaintenanceDAO;

/** Recharge battery command. */
public class RechargeBatteryCommand extends AbstractMaintenanceCommand {
    private final MaintenanceDAO dao;

    public RechargeBatteryCommand(String scooterId, MaintenanceDAO dao) {
        super(scooterId);
        this.dao = dao;
    }

    @Override
    public void execute() {
        try {
            dao.updateTaskStatus(scooterId, "BATTERY_RECHARGED");
            completed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
