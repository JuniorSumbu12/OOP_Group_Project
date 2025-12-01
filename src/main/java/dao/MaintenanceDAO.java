package com.group4.cesc.dao;

import com.group4.cesc.model.MaintenanceTask;
import java.util.List;

public interface MaintenanceDAO {
    void createTask(MaintenanceTask task) throws Exception;
    void updateTaskStatus(String scooterId, String newStatus) throws Exception;
    List<MaintenanceTask> getPendingTasks() throws Exception;
}
