package com.v1.iskream.layer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;

    @JsonIgnore
    @Column(nullable = false, unique = true)
    private String password;

    @Column(nullable = false)
    private String nickname;
}
