package com.SENA.FlightManagementSystem.Security.Entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Base entity class providing common attributes for all entities in the system.
 * Includes auditing fields, soft delete support, and optimistic locking.
 * 
 * @author Flight Management System Team
 * @version 2.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class ABaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Primary key using UUID for better distribution and security.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    
    /**
     * Entity status for soft delete and state management.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status = Status.ACTIVE;
    
    /**
     * Timestamp when the entity was created.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * User who created the entity.
     */
    @CreatedBy
    @Column(name = "created_by", length = 100, updatable = false)
    private String createdBy;
    
    /**
     * Timestamp of the last update.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * User who last updated the entity.
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
    
    /**
     * Soft delete timestamp.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    /**
     * User who deleted the entity (soft delete).
     */
    @Column(name = "deleted_by", length = 100)
    private String deletedBy;
    
    /**
     * Version for optimistic locking to prevent concurrent modification issues.
     */
    @Version
    @Column(name = "version")
    private Long version;
    
    /**
     * Lifecycle callback executed before entity removal.
     * Implements soft delete by updating status and deletedAt timestamp.
     */
    @PreRemove
    public void preRemove() {
        this.deletedAt = LocalDateTime.now();
        this.status = Status.DELETED;
    }
    
    /**
     * Check if entity is active (not deleted).
     * 
     * @return true if entity status is ACTIVE
     */
    public boolean isActive() {
        return Status.ACTIVE.equals(this.status);
    }
    
    /**
     * Check if entity is deleted (soft delete).
     * 
     * @return true if entity status is DELETED
     */
    public boolean isDeleted() {
        return Status.DELETED.equals(this.status);
    }
    
    /**
     * Marks entity as deleted (soft delete).
     * 
     * @param deletedBy username of who performed the delete
     */
    public void markAsDeleted(String deletedBy) {
        this.status = Status.DELETED;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
    
    /**
     * Restores a soft-deleted entity.
     */
    public void restore() {
        this.status = Status.ACTIVE;
        this.deletedAt = null;
        this.deletedBy = null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ABaseEntity that = (ABaseEntity) o;
        return id != null && Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
    
    /**
     * Enum representing entity status.
     */
    public enum Status {
        ACTIVE,
        INACTIVE,
        DELETED,
        PENDING,
        SUSPENDED
    }
}
