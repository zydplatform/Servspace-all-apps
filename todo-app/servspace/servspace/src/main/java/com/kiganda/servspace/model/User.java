package com.kiganda.servspace.model;

// import javax.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
@Id
@GeneratedValue(strategy= GenerationType.IDENTITY)

private Long id;
private String username;
private String password;

}
