package com.kiganda.servspace.model;

// import javax.persistence.*;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Task {

        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)

        private Long id;
        private String title;
        private String description;
        private boolean completed;
        private LocalDateTime createdAt;


}
