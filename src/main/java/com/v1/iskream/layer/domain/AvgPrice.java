package com.v1.iskream.layer.domain;

import java.math.BigInteger;
import java.time.LocalDate;

public interface AvgPrice {
    Long getProductId();
    LocalDate getDate();
    Integer getAvgPrice();
}
