package com.v1.iskream.layer.domain.dto.response;

import com.v1.iskream.layer.domain.Price;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PriceResponseDto implements Comparable<PriceResponseDto>{
    private int size;
    private int price;
    private int price_diff;

    public PriceResponseDto(Price price, int price_diff) {
        this.size = price.getSize();
        this.price = price.getPrice();
        this.price_diff = price_diff;
    }

    @Override
    public int compareTo(PriceResponseDto responseDto) {
        return Integer.compare(size, responseDto.size);
    }
}
