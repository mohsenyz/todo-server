package com.mphj.todo.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Transient
    public int localId;

    @ManyToOne
    public UserList userList;

    @ManyToOne
    @JsonBackReference
    @JsonIgnore
    public User user;

    public String content;
    public long date;
    public int priority;
    public long createdAt;
    public long updatedAt;
    public boolean done;


    /**
     *
     */

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    public List<Flag> flags;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    public List<TodoTask> todoTasks;


}
