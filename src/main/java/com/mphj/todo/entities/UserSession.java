package com.mphj.todo.entities;

import javax.persistence.*;

@Entity
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @ManyToOne
    public User user;

    public String token;
    public String imei;
    public long createdAt;
    public long lastSeen;

}
