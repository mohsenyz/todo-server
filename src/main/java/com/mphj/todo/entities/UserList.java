package com.mphj.todo.entities;

import javax.persistence.*;

@Entity
public class UserList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public transient int localId;

    @ManyToOne
    public User user;

    public String name;
    public long createdAt;
    public long updatedAt;

}
