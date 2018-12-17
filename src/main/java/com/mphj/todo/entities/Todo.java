package com.mphj.todo.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public transient int localId;

    @ManyToOne
    public UserList userList;

    @ManyToOne
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

    @OneToMany(fetch = FetchType.LAZY)
    public List<Flag> flags;

    @OneToMany(fetch = FetchType.LAZY)
    public List<TodoTask> todoTasks;


}
