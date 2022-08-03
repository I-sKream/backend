package com.v1.iskream.layer.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvgPriceResponseDto {
    private Long product_id;
    private LocalDate date;
    private Integer avg_price;


    @Override
    public String toString() {
        return "AvgPriceResponseDto{" +
                ", product_id='" + product_id + '\'' +
                ", date='" + date + '\'' +
                ", avg_price='" + avg_price + '\'' +
                '}';
    }
}