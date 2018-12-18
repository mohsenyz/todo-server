package com.mphj.todo.entities;

import javax.persistence.*;

@Entity
public class Flag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    public Todo todo;
    public String flag;

    public Flag() {

    }

    public Flag(String flag) {
        this.flag = flag;
    }
}
