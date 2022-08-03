package com.v1.iskream.layer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    User buyer;

    @OneToOne
    Price price;

    public Orders(User buyer, Price price) {
        this.buyer = buyer;
        this.price = price;
    }
}
