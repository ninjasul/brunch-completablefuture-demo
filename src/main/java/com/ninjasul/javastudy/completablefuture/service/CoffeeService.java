package com.ninjasul.javastudy.completablefuture.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface CoffeeService {
    int getPrice(String name);
    CompletableFuture<Integer> getPriceAsync(String name);
    CompletableFuture<Integer> getPriceAsync2(String name);
    Future<Integer> getDiscountPriceAsync(Integer price);
}
