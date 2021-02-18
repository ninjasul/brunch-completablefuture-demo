package com.ninjasul.javastudy.completablefuture.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Coffee {
    private String name;
    private int price;

    @Builder
    public Coffee(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
