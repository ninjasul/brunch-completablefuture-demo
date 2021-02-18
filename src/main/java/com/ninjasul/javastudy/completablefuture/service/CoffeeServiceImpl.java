package com.ninjasul.javastudy.completablefuture.service;

import com.ninjasul.javastudy.completablefuture.repository.CoffeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoffeeServiceImpl implements CoffeeService {
    private final CoffeeRepository coffeeRepository;
    private final Executor threadPoolExecutor;

    @Override
    public int getPrice(String name) {
        log.info("동기 호출 방식으로 가격 조회 시작: {}", name);
        return coffeeRepository.getPriceByName(name);
    }

    @Override
    public CompletableFuture<Integer> getPriceAsync(String name) {
        log.info("비동기 호출 방식으로 가격 조회 시작: {}", name);

        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            log.info("새로운 쓰레드로 작업 시작");
            Integer price = coffeeRepository.getPriceByName(name);
            future.complete(price);
        }).start();

        return future;
    }

    @Override
    public CompletableFuture<Integer> getPriceAsync2(String name) {
        log.info("비동기 호출 방식으로 가격 조회 시작: {}", name);

        return CompletableFuture.supplyAsync(() -> {
                log.info("supplyAsync");
                return coffeeRepository.getPriceByName(name);
            },
             // common ThreadPool 대신 별도 ThreadPool 을 인자로 넘겨주어 처리
            threadPoolExecutor
        );
    }

    @Override
    public Future<Integer> getDiscountPriceAsync(Integer price) {
        return CompletableFuture.supplyAsync(() -> {
                log.info("supplyAsync");
                return (int)(price * 0.9);
            },
            threadPoolExecutor
        );
    }
}
