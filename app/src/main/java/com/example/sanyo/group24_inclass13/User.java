package com.example.sanyo.group24_inclass13;

import java.io.Serializable;

/**
 * Created by sanyo on 4/23/2018.
 */

public class User implements Serializable {
    public String email, id, name;

    public User() {
    }

    public User(String email, String id, String name) {

        this.email = email;
        this.id = id;
        this.name = name;
    }
}
