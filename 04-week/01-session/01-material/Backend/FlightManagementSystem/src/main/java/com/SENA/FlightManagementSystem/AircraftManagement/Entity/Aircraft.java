package com.SENA.FlightManagementSystem.AircraftManagement.Entity;

import com.SENA.FlightManagementSystem.Security.Entity.ABaseEntity;
import com.SENA.FlightManagementSystem.Parameterization.Entity.AircraftType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an aircraft in the fleet management system.
 * Stores comprehensive information about aircraft including manufacturer,
 * model, registration, maintenance, and operational data.
 * 
 * @author Flight Management System Team
 * @version 2.0
 */
@Entity
@Table(name = "aircraft", 
       indexes = {
           @Index(name = "idx_aircraft_registration", columnList = "registration_code", unique = true),
           @Index(name = "idx_aircraft_serial", columnList = "serial_number", unique = true),
           @Index(name = "idx_aircraft_type", columnList = "aircraft_type_id"),
           @Index(name = "idx_aircraft_status", columnList = "status"),
           @Index(name = "idx_aircraft_manufacturer", columnList = "manufacturer")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_registration_code", columnNames = "registration_code"),
           @UniqueConstraint(name = "uk_serial_number", columnNames = "serial_number")
       })
@Schema(name = "Aircraft", description = "Aircraft entity for fleet management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"statusHistory", "maintenanceSchedules", "flights"})
public class Aircraft extends ABaseEntity {
    
    /**
     * Aircraft manufacturer (e.g., Boeing, Airbus).
     */
    @NotBlank(message = "Manufacturer is required")
    @Size(min = 2, max = 100, message = "Manufacturer must be between 2 and 100 characters")
    @Column(name = "manufacturer", nullable = false, length = 100)
    @Schema(description = "Aircraft manufacturer name", example = "Boeing", required = true)
    private String manufacturer;
    
    /**
     * Aircraft model (e.g., 737-800, A320).
     */
    @NotBlank(message = "Model is required")
    @Size(min = 2, max = 100, message = "Model must be between 2 and 100 characters")
    @Column(name = "model", nullable = false, length = 100)
    @Schema(description = "Aircraft model", example = "737-800", required = true)
    private String model;
    
    /**
     * Registration code (tail number) - unique identifier.
     * Format: Usually country code + alphanumeric (e.g., N12345 for USA).
     */
    @NotBlank(message = "Registration code is required")
    @Pattern(regexp = "^[A-Z0-9-]{5,10}$", 
             message = "Registration code must be 5-10 uppercase alphanumeric characters with optional hyphens")
    @Column(name = "registration_code", nullable = false, unique = true, length = 10)
    @Schema(description = "Aircraft registration code (tail number)", example = "N12345", required = true)
    private String registrationCode;
    
    /**
     * Manufacturer's serial number - unique per aircraft.
     */
    @NotBlank(message = "Serial number is required")
    @Size(max = 50, message = "Serial number max length is 50")
    @Column(name = "serial_number", nullable = false, unique = true, length = 50)
    @Schema(description = "Manufacturer serial number", example = "MSN12345", required = true)
    private String serialNumber;
    
    /**
     * Date when the aircraft was manufactured.
     */
    @Past(message = "Manufacturing date must be in the past")
    @Column(name = "manufacturing_date")
    @Schema(description = "Aircraft manufacturing date", example = "2015-06-15")
    private LocalDate manufacturingDate;
    
    /**
     * Date when the aircraft was acquired by the company.
     */
    @PastOrPresent(message = "Acquisition date must be in the past or present")
    @Column(name = "acquisition_date")
    @Schema(description = "Aircraft acquisition date", example = "2016-01-10")
    private LocalDate acquisitionDate;
    
    /**
     * Total flight hours accumulated by the aircraft.
     */
    @Min(value = 0, message = "Hours in use cannot be negative")
    @Column(name = "hours_in_use")
    @Schema(description = "Total flight hours", example = "25000", minimum = "0")
    @Builder.Default
    private Integer hoursInUse = 0;
    
    /**
     * Total number of flight cycles (takeoff-landing) completed.
     */
    @Min(value = 0, message = "Cycles completed cannot be negative")
    @Column(name = "cycles_completed")
    @Schema(description = "Total number of flight cycles", example = "15000", minimum = "0")
    @Builder.Default
    private Integer cyclesCompleted = 0;
    
    /**
     * Passenger capacity of the aircraft.
     */
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 1000, message = "Capacity cannot exceed 1000")
    @Column(name = "capacity", nullable = false)
    @Schema(description = "Passenger capacity", example = "189", required = true, minimum = "1", maximum = "1000")
    private Integer capacity;
    
    /**
     * Type/category of aircraft (e.g., Commercial, Cargo, Private).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_type_id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_aircraft_type"))
    @NotNull(message = "Aircraft type is required")
    @Schema(description = "Aircraft type reference", required = true)
    private AircraftType aircraftType;
    
    /**
     * History of aircraft status changes.
     */
    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("changeDate DESC")
    @Builder.Default
    private List<AircraftStatusHistory> statusHistory = new ArrayList<>();
    
    /**
     * Scheduled maintenance for this aircraft.
     */
    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MaintenanceSchedule> maintenanceSchedules = new ArrayList<>();
    
    /**
     * Flights operated by this aircraft.
     */
    @OneToMany(mappedBy = "aircraft")
    @Builder.Default
    private List<Flight> flights = new ArrayList<>();
    
    // ==================== Business Methods ====================
    
    /**
     * Adds a status history entry to this aircraft.
     * 
     * @param history the status history entry to add
     */
    public void addStatusHistory(AircraftStatusHistory history) {
        statusHistory.add(history);
        history.setAircraft(this);
    }
    
    /**
     * Removes a status history entry.
     * 
     * @param history the status history entry to remove
     */
    public void removeStatusHistory(AircraftStatusHistory history) {
        statusHistory.remove(history);
        history.setAircraft(null);
    }
    
    /**
     * Checks if the aircraft is operational.
     * An aircraft is operational if it's active and hasn't exceeded maximum flight hours.
     * 
     * @return true if operational, false otherwise
     */
    public boolean isOperational() {
        return isActive() && hoursInUse < 50000;
    }
    
    /**
     * Checks if the aircraft needs maintenance based on hours.
     * Typically, aircraft need maintenance every 5000 hours.
     * 
     * @return true if maintenance is needed
     */
    public boolean needsMaintenance() {
        return hoursInUse % 5000 < 100; // Within 100 hours of maintenance interval
    }
    
    /**
     * Increments the flight hours of the aircraft.
     * 
     * @param hours number of hours to add
     * @throws IllegalArgumentException if hours is negative
     */
    public void incrementHours(int hours) {
        if (hours < 0) {
            throw new IllegalArgumentException("Hours cannot be negative");
        }
        this.hoursInUse += hours;
    }
    
    /**
     * Increments the flight cycles counter.
     */
    public void incrementCycles() {
        this.cyclesCompleted++;
    }
    
    /**
     * Resets hours and cycles (typically after major overhaul).
     * 
     * @param reason reason for reset
     */
    public void resetOperationalData(String reason) {
        this.hoursInUse = 0;
        this.cyclesCompleted = 0;
        // Log this event in audit or history
    }
    
    /**
     * Calculates the aircraft's age in years.
     * 
     * @return age in years, or 0 if manufacturing date is not set
     */
    public int getAgeInYears() {
        if (manufacturingDate == null) {
            return 0;
        }
        return LocalDate.now().getYear() - manufacturingDate.getYear();
    }
    
    /**
     * Gets a formatted display name for the aircraft.
     * 
     * @return formatted string with manufacturer, model, and registration
     */
    public String getDisplayName() {
        return String.format("%s %s (%s)", manufacturer, model, registrationCode);
    }
    
    /**
     * Lifecycle callback - validate before persisting.
     */
    @PrePersist
    protected void onCreate() {
        if (hoursInUse == null) {
            hoursInUse = 0;
        }
        if (cyclesCompleted == null) {
            cyclesCompleted = 0;
        }
    }
    
    /**
     * Lifecycle callback - validate before updating.
     */
    @PreUpdate
    protected void onUpdate() {
        if (hoursInUse < 0) {
            throw new IllegalStateException("Hours in use cannot be negative");
        }
        if (cyclesCompleted < 0) {
            throw new IllegalStateException("Cycles completed cannot be negative");
        }
    }
}
