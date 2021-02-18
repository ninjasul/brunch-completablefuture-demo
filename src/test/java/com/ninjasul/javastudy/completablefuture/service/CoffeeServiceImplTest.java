package com.ninjasul.javastudy.completablefuture.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.ninjasul.javastudy.completablefuture.constant.CoffeeConstant.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class CoffeeServiceImplTest {
    Logger log = LoggerFactory.getLogger(CoffeeServiceImplTest.class);

    @Autowired
    private Executor threadPoolExecutor;

    @Autowired
    private CoffeeService coffeeService;

    @Test
    @DisplayName("동기/블록킹 방식으로 가격조회 호출")
    void getPrice() {
        int expected = LATTE_PRICE;
        int actual = coffeeService.getPrice(LATTE_NAME);
        log.info("가격 조회 완료");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("비동기/블록킹 방식으로 가격조회 호출")
    void getPriceAsync() {
        int expected = LATTE_PRICE;

        CompletableFuture<Integer> future = coffeeService.getPriceAsync(LATTE_NAME);
        log.info("가격 조회 미완료. 다른 작업 수행 중");
        int actual = future.join();
        log.info("가격 조회 완료");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("비동기/블록킹 방식으로 가격조회 호출, 서버는 CompletableFuture.supplyAsync() 로 응답")
    void getPriceAsync2() {
        int expected = LATTE_PRICE;

        CompletableFuture<Integer> future = coffeeService.getPriceAsync2(LATTE_NAME);
        log.info("가격 조회 미완료. 다른 작업 수행 중");

        int actual = future.join();
        log.info("가격 조회 완료");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("async/non-blocking 방식으로 가격조회 호출. thenAccept() 메소드로 non-blocking 구현.")
    void getPriceAsync2WithThenAccept() {
        Integer expected = LATTE_PRICE;

        CompletableFuture<Void> future = coffeeService
            .getPriceAsync2(LATTE_NAME)
            .thenAccept(actual -> {
                log.info("thenAccept() 메소드 실행. 가격: {}, 하지만 리턴 값은 없음.", actual);
                assertThat(actual).isEqualTo(expected);
            });

        log.info("가격 조회 미완료. non-blocking으로 다른 작업 수행 중");


        // join 코드가 없으면 테스트 코드의 main thread가 바로 종료되는 관계로 종료 방지를 위해 실행
        assertThat(future.join()).isNull();
    }

    @Test
    @DisplayName("async/non-blocking 방식으로 가격조회 호출. thenApply() 메소드로 non-blocking 구현.")
    void getPriceAsync2WithThenApply() {
        Integer expected = LATTE_PRICE + 100;

        CompletableFuture<Void> future = coffeeService
            .getPriceAsync2(LATTE_NAME)
            .thenApply(actual -> {
                log.info("thenApply() 메소드 실행. 가격: {}", actual);
                return actual + 100;
            })
            .thenAccept(actual -> {
                log.info("thenAccept() 메소드 실행. 가격: {}", actual);
                assertThat(actual).isEqualTo(expected);
            });

        log.info("가격 조회 미완료. non-blocking으로 다른 작업 수행 중");


        // join 코드가 없으면 테스트 코드의 main thread가 바로 종료되는 관계로 종료 방지를 위해 실행
        assertThat(future.join()).isNull();
    }

    @Test
    @DisplayName("async/non-blocking 방식으로 가격조회 호출. thenApply() 메소드와 thenAccept() 메소드 실행을 별도의 쓰레드로 분리하기 위해 thenAcceptAsync() 메소드를 사용.")
    void getPriceAsync2WithThenApplyAsync() {
        Integer expected = LATTE_PRICE + 100;


        CompletableFuture<Void> future = coffeeService
            .getPriceAsync2(LATTE_NAME)
            .thenApplyAsync(actual -> {
                log.info("thenApplyAsync() 메소드 실행. 가격: {}", actual);
                return actual + 100;
            }, threadPoolExecutor)
            .thenAcceptAsync(actual -> {
                log.info("thenAcceptAsync() 메소드 실행. 가격: {}", actual);
                assertThat(actual).isEqualTo(expected);
            }, threadPoolExecutor);

        log.info("가격 조회 미완료. non-blocking으로 다른 작업 수행 중");


        // join 코드가 없으면 테스트 코드의 main thread가 바로 종료되는 관계로 종료 방지를 위해 실행
        assertThat(future.join()).isNull();
    }

    @Test
    @DisplayName("async/blocking 방식으로 가격조회 호출. thenCombine() 메소드로 가격의 합 계산.")
    void getPriceAsync2ThenCombine() {
        Integer expected = LATTE_PRICE + MOCHA_PRICE;

        CompletableFuture<Integer> futureLatte = coffeeService.getPriceAsync2(LATTE_NAME);
        CompletableFuture<Integer> futureMocha = coffeeService.getPriceAsync2(MOCHA_NAME);

        Integer actual = futureLatte.thenCombine(futureMocha, Integer::sum).join();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("async/blocking 방식으로 가격조회 호출. allOf() 메소드로 가격의 합 계산.")
    void getPriceAsync2AllOf() {
        Integer expected = LATTE_PRICE + MOCHA_PRICE + AMERICANO_PRICE;

        CompletableFuture<Integer> futureLatte = coffeeService.getPriceAsync2(LATTE_NAME);
        CompletableFuture<Integer> futureMocha = coffeeService.getPriceAsync2(MOCHA_NAME);
        CompletableFuture<Integer> futureAmericano = coffeeService.getPriceAsync2(AMERICANO_NAME);

        List<CompletableFuture<Integer>> coffeeFutures = Arrays.asList(futureLatte, futureMocha, futureAmericano);

        Integer actual = CompletableFuture.allOf(coffeeFutures.toArray(new CompletableFuture[0]))
            .thenApply(Void -> getJoinedCoffeeFutures(coffeeFutures))
            .join()
            .stream()
            .reduce(0, Integer::sum);

        assertThat(actual).isEqualTo(expected);
    }

    private List<Integer> getJoinedCoffeeFutures(List<CompletableFuture<Integer>> coffeeFutures) {
        return coffeeFutures.stream()
                   .map(CompletableFuture::join)
                   .collect(Collectors.toList());
    }
}