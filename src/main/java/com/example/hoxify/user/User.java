package com.example.hoxify.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String userName;
    private String displayName;
    private String password;

}
