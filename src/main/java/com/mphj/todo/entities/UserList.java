package com.mphj.todo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class UserList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Transient
    public int localId;

    @ManyToOne
    @JsonBackReference
    @JsonIgnore
    public User user;

    public String name;
    public long createdAt;
    public long updatedAt;

}
