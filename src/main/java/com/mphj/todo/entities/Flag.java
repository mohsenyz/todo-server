package com.mphj.todo.entities;

import javax.persistence.*;

@Entity
public class Flag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    private String flag;
}
