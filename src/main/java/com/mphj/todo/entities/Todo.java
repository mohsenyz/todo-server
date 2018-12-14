package com.mphj.todo.entities;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @ManyToOne
    public UserList userList;

    public String content;
    public long date;
    public int priority;
    public long createdAt;
    public long updatedAt;
    public boolean done;


    /**
     *
     */

    transient public ArrayList<Flag> flagList;
}
