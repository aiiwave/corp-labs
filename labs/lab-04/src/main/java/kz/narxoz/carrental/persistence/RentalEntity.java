package kz.narxoz.carrental.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

/** A row of rental_request. Not the domain: the rule lives in RentalPolicy. */
@Entity
@Table(name = "rental_request")
public class RentalEntity {

    @Id
    private UUID id; // no @GeneratedValue: the UUID comes from Java

    @Column(name = "business_key", nullable = false, unique = true)
    private String businessKey;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String title;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected RentalEntity() {
    }

    public RentalEntity(UUID id, String businessKey, String status, String title) {
        this.id = id;
        this.businessKey = businessKey;
        this.status = status;
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }
}
