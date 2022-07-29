package com.v1.iskream.layer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String password;

    @Column(nullable = false)
    private String nickname;
}
