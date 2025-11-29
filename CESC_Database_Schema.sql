CREATE USER IF NOT EXISTS 'cst8288'@'localhost' IDENTIFIED BY 'cst8288';
GRANT ALL PRIVILEGES ON books.* TO 'cst8288'@'localhost';
FLUSH PRIVILEGES;


CREATE DATABASE IF NOT EXISTS cesc_db;
USE cesc_db;

-- ============================================================
-- TABLE: users
-- Purpose: Store all user accounts (User, Sponsor, Maintainer)
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    userId VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    userType VARCHAR(20) NOT NULL CHECK (userType IN ('USER', 'SPONSOR', 'MAINTAINER')),
    credits DOUBLE NOT NULL DEFAULT 0.0,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    
    KEY idx_email (email),
    KEY idx_userType (userType),
    KEY idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: sponsors
-- Purpose: Store Sponsor-specific information
-- Relationship: One-to-One with users table
-- ============================================================
CREATE TABLE IF NOT EXISTS sponsors (
    userId VARCHAR(36) PRIMARY KEY,
    rating DOUBLE NOT NULL DEFAULT 0.0,
    scooters_donated INT NOT NULL DEFAULT 0,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: maintainers
-- Purpose: Store Maintainer-specific information
-- Relationship: One-to-One with users table
-- ============================================================
CREATE TABLE IF NOT EXISTS maintainers (
    userId VARCHAR(36) PRIMARY KEY,
    rating DOUBLE NOT NULL DEFAULT 0.0,
    tasks_completed INT NOT NULL DEFAULT 0,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: charging_stations
-- Purpose: Store charging station locations and capacities
-- Location: Woodroffe Campus only
-- ============================================================
CREATE TABLE IF NOT EXISTS charging_stations (
    stationId VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    building VARCHAR(100),
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    maxCapacity INT NOT NULL,
    chargers INT NOT NULL DEFAULT 5,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    KEY idx_name (name),
    KEY idx_building (building)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: e_scooters
-- Purpose: Store e-scooter inventory and status
-- Relationship: Many-to-One with users (owner) and charging_stations
-- ============================================================
CREATE TABLE IF NOT EXISTS e_scooters (
    scooterId VARCHAR(36) PRIMARY KEY,
    vehicleNumber VARCHAR(50) UNIQUE NOT NULL,
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    color VARCHAR(50),
    batteryCapacity DOUBLE NOT NULL,
    currentBattery DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'IN_TRANSIT', 'CHARGING', 'MAINTENANCE', 'INACTIVE')),
    latitude DOUBLE,
    longitude DOUBLE,
    hoursUsed DOUBLE NOT NULL DEFAULT 0.0,
    ownerId VARCHAR(36),
    currentStationId VARCHAR(36),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    KEY idx_vehicleNumber (vehicleNumber),
    KEY idx_status (status),
    KEY idx_ownerId (ownerId),
    KEY idx_stationId (currentStationId),
    
    FOREIGN KEY (ownerId) REFERENCES users(userId) ON DELETE SET NULL,
    FOREIGN KEY (currentStationId) REFERENCES charging_stations(stationId) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: transactions
-- Purpose: Log all financial transactions (credits/debits)
-- Relationship: Many-to-One with users and e_scooters
-- ============================================================
CREATE TABLE IF NOT EXISTS transactions (
    transactionId VARCHAR(36) PRIMARY KEY,
    userId VARCHAR(36) NOT NULL,
    scooterId VARCHAR(36),
    transactionType VARCHAR(20) NOT NULL CHECK (transactionType IN ('DEBIT', 'CREDIT', 'REFUND', 'PENALTY')),
    amount DOUBLE NOT NULL,
    description TEXT,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    KEY idx_userId (userId),
    KEY idx_scooterId (scooterId),
    KEY idx_transactionType (transactionType),
    KEY idx_created_date (created_date),
    
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY (scooterId) REFERENCES e_scooters(scooterId) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: maintenance_tasks
-- Purpose: Track maintenance operations on e-scooters
-- Relationship: Many-to-One with e_scooters and maintainers
-- ============================================================
CREATE TABLE IF NOT EXISTS maintenance_tasks (
    taskId VARCHAR(36) PRIMARY KEY,
    scooterId VARCHAR(36) NOT NULL,
    maintainerId VARCHAR(36),
    taskType VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    description TEXT,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_date TIMESTAMP NULL,
    
    KEY idx_scooterId (scooterId),
    KEY idx_maintainerId (maintainerId),
    KEY idx_status (status),
    KEY idx_taskType (taskType),
    
    FOREIGN KEY (scooterId) REFERENCES e_scooters(scooterId) ON DELETE CASCADE,
    FOREIGN KEY (maintainerId) REFERENCES users(userId) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: scooter_usage_history
-- Purpose: Track scooter usage for analytics and billing
-- Relationship: Many-to-One with e_scooters and users
-- ============================================================
CREATE TABLE IF NOT EXISTS scooter_usage_history (
    usageId VARCHAR(36) PRIMARY KEY,
    scooterId VARCHAR(36) NOT NULL,
    userId VARCHAR(36) NOT NULL,
    startStation VARCHAR(36),
    endStation VARCHAR(36),
    distanceTraveled DOUBLE,
    durationMinutes INT,
    batteryUsed DOUBLE,
    costIncurred DOUBLE,
    checkout_time TIMESTAMP,
    return_time TIMESTAMP,
    
    KEY idx_scooterId (scooterId),
    KEY idx_userId (userId),
    KEY idx_checkout_time (checkout_time),
    
    FOREIGN KEY (scooterId) REFERENCES e_scooters(scooterId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: system_configuration
-- Purpose: Store system-wide settings and parameters
-- ============================================================
CREATE TABLE IF NOT EXISTS system_configuration (
    configKey VARCHAR(100) PRIMARY KEY,
    configValue VARCHAR(255) NOT NULL,
    description TEXT,
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- SAMPLE DATA INSERTION (Optional)
-- Uncomment to add test data to your database
-- ============================================================

-- Insert system configuration
INSERT INTO system_configuration (configKey, configValue, description) VALUES
('DEBIT_PER_MINUTE', '0.25', 'Cost per minute of usage'),
('DEBIT_PER_KM', '1.50', 'Cost per kilometer traveled'),
('CREDIT_PER_HOUR_DONATED', '0.50', 'Credit earned per hour scooter is available'),
('CREDIT_PER_SCOOTER_RECOVERED', '5.00', 'Credit earned per misplaced scooter recovered'),
('MAINTENANCE_ALERT_HOURS', '500', 'Alert maintenance after this many usage hours'),
('LOW_BATTERY_THRESHOLD', '20', 'Battery percentage to trigger low battery alert'),
('MAX_DAILY_DEBIT', '50.00', 'Maximum debit allowed per day per user'),
('PAYMENT_DUE_DAYS', '10', 'Days after month-end to pay outstanding debits');

-- Insert sample charging stations (Woodroffe Campus locations)
INSERT INTO charging_stations (stationId, name, building, latitude, longitude, maxCapacity, chargers) VALUES
('STATION-001', 'North Campus Station', 'North Entrance', 45.4275, -75.6445, 10, 5),
('STATION-002', 'South Campus Station', 'South Entrance', 45.4260, -75.6445, 10, 5),
('STATION-003', 'East Campus Station', 'East Parking', 45.4269, -75.6430, 8, 4),
('STATION-004', 'West Campus Station', 'West Building', 45.4269, -75.6465, 8, 4),
('STATION-005', 'Library Station', 'Main Library', 45.4270, -75.6450, 12, 6),
('STATION-006', 'Cafeteria Station', 'Dining Hall', 45.4268, -75.6442, 6, 3);

