package com.group4.cesc.dao.impl;

import com.group4.cesc.dao.MaintenanceDAO;
import com.group4.cesc.model.MaintenanceTask;
import com.group4.cesc.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MySQL implementation for MaintenanceDAO.
 */
public class MySQLMaintenanceDAO implements MaintenanceDAO {

    @Override
    public void createTask(MaintenanceTask task) throws Exception {
        String sql = "INSERT INTO maintenance_tasks (taskId, scooterId, maintainerId, taskType, status, description) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (task.getTaskId() == null) task.setTaskId(UUID.randomUUID().toString());
            ps.setString(1, task.getTaskId());
            ps.setString(2, task.getScooterId());
            ps.setString(3, task.getMaintainerId());
            ps.setString(4, task.getTaskType());
            ps.setString(5, task.getStatus());
            ps.setString(6, task.getDescription());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateTaskStatus(String scooterId, String newStatus) throws Exception {
        String sql = "UPDATE maintenance_tasks SET status=?, completed_date=CURRENT_TIMESTAMP WHERE scooterId=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, scooterId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<MaintenanceTask> getPendingTasks() throws Exception {
        List<MaintenanceTask> list = new ArrayList<>();
        String sql = "SELECT * FROM maintenance_tasks WHERE status='PENDING'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MaintenanceTask t = new MaintenanceTask();
                t.setTaskId(rs.getString("taskId"));
                t.setScooterId(rs.getString("scooterId"));
                t.setMaintainerId(rs.getString("maintainerId"));
                t.setTaskType(rs.getString("taskType"));
                t.setStatus(rs.getString("status"));
                t.setDescription(rs.getString("description"));
                list.add(t);
            }
        }
        return list;
    }
}
