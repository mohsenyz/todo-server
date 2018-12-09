package com.mphj.todo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public String name;
    public String email;
    public String password;
    public long createdAt;
    public boolean isVerified;


}
