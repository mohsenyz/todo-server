package com.mphj.todo.entities;

import javax.persistence.*;

@Entity
public class TodoTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Todo todo;

    private String task;
    private boolean done;


}
