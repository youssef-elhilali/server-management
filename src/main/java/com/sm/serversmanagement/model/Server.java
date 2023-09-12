package com.sm.serversmanagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sm.serversmanagement.enumerations.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "servers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotEmpty(message = "IP Address can't be empty")
    private String ipAddress;
    private String name;
    private String memory;
    private String type;
    private String imageUrl;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm")
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm")
    private Date modificationDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        creationDate = new Date();
        isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        modificationDate = new Date();
    }

}
