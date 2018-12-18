package com.mphj.todo.entities;

import javax.persistence.*;

@Entity
public class TodoTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    public Todo todo;
    public String task;
    public boolean done;

    public TodoTask() {

    }

    public TodoTask(String task) {
        this.task = task;
    }


}
