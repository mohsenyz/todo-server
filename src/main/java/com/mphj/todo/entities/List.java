package com.mphj.todo.entities;

import javax.persistence.*;

@Entity
public class List {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User user;

    private String name;
    private long createdAt;

}
