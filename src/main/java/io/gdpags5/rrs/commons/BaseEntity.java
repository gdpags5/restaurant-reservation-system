package io.gdpags5.rrs.commons;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_and_time_created", nullable = false)
    private LocalDateTime dateAndTimeCreated;

    @Column(name = "date_and_time_updated", nullable = false)
    private LocalDateTime dateAndTimeUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateAndTimeCreated() {
        return dateAndTimeCreated;
    }

    public void setDateAndTimeCreated(LocalDateTime dateAndTimeCreated) {
        this.dateAndTimeCreated = dateAndTimeCreated;
    }

    public LocalDateTime getDateAndTimeUpdated() {
        return dateAndTimeUpdated;
    }

    public void setDateAndTimeUpdated(LocalDateTime dateAndTimeUpdated) {
        this.dateAndTimeUpdated = dateAndTimeUpdated;
    }
}
