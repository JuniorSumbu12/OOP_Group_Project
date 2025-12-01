package com.group4.cesc.controller;

import com.group4.cesc.model.GeoPoint;
import com.group4.cesc.pattern.adapter.CoordinateAdapter;
import com.group4.cesc.service.ScooterService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Handles GPS location update requests from scooter devices or dashboard.
 * <p>
 * This servlet converts raw latitude and longitude into a {@link GeoPoint}
 * using the Adapter Pattern and updates the database via {@link ScooterService}.
 */
@WebServlet("/updateLocation")
public class UpdateLocationServlet extends HttpServlet {

    private ScooterService scooterService;

    /**
     * Initializes the ScooterService used for updating GPS coordinates.
     */
    @Override
    public void init() throws ServletException {
        scooterService = new ScooterService();
    }

    /**
     * Processes the GPS update POST request.
     * <p>
     * Converts the latitude and longitude into a {@link GeoPoint},
     * and updates the scooter's location in the database.
     *
     * @param request  HTTP request containing GPS parameters
     * @param response HTTP response redirecting the result page
     * @throws ServletException if servlet execution fails
     * @throws IOException      if I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int scooterId = Integer.parseInt(request.getParameter("scooterId"));
        String lat = request.getParameter("latitude");
        String lon = request.getParameter("longitude");

        GeoPoint point = CoordinateAdapter.convert(lat, lon);

        boolean updated = scooterService.updateScooterLocation(scooterId, point);

        if (updated) {
            response.sendRedirect("gps_update.jsp?msg=success");
        } else {
            response.sendRedirect("gps_update.jsp?msg=failed");
        }
    }
}
