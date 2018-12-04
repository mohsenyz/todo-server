package com.mphj.todo.entities;

import javax.persistence.*;

@Entity
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User user;

    private String token;
    private String imei;
    private long createdAt;
    private long lastSeen;

}
