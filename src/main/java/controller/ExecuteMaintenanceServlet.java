package com.group4.cesc.controller;

import com.group4.cesc.dao.impl.MySQLMaintenanceDAO;
import com.group4.cesc.pattern.command.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Execute maintenance commands on request. */
@WebServlet("/executeMaintenance")
public class ExecuteMaintenanceServlet extends HttpServlet {

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws IOException {
        String scooterId = req.getParameter("scooterId");
        String action = req.getParameter("action");

        MySQLMaintenanceDAO dao = new MySQLMaintenanceDAO();
        CommandInvoker invoker = new CommandInvoker();

        switch (action) {
            case "tire": invoker.addCommand(new ReplaceTireCommand(scooterId, dao)); break;
            case "battery": invoker.addCommand(new RechargeBatteryCommand(scooterId, dao)); break;
            case "brake": invoker.addCommand(new BrakeInspectionCommand(scooterId, dao)); break;
            default:
                resp.sendError(400, "Unknown action");
                return;
        }

        invoker.executeAll();
        resp.sendRedirect("maintenance.jsp");
    }
}
