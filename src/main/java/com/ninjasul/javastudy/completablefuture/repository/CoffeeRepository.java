package com.ninjasul.javastudy.completablefuture.repository;

import com.ninjasul.javastudy.completablefuture.domain.Coffee;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static com.ninjasul.javastudy.completablefuture.constant.CoffeeConstant.*;

@Repository
public class CoffeeRepository {
    private Map<String, Coffee> coffees = new HashMap<>();

    @PostConstruct
    public void init() {
        coffees.put(AMERICANO_NAME, AMERICANO);
        coffees.put(LATTE_NAME, LATTE);
        coffees.put(MOCHA_NAME, MOCHA);
    }

    @SneakyThrows
    public int getPriceByName(String name) {
        Thread.sleep(1000L);
        return coffees.get(name).getPrice();
    }
}
