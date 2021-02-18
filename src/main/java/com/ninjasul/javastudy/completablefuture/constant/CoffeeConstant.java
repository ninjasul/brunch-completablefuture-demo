package com.ninjasul.javastudy.completablefuture.constant;

import com.ninjasul.javastudy.completablefuture.domain.Coffee;

public class CoffeeConstant {
    public static final String LATTE_NAME = "latte";
    public static final String MOCHA_NAME = "mocha";
    public static final String AMERICANO_NAME = "americano";

    public static final int LATTE_PRICE = 1_100;
    public static final int MOCHA_PRICE = 1_300;
    public static final int AMERICANO_PRICE = 900;

    public static final Coffee LATTE = new Coffee(LATTE_NAME, LATTE_PRICE);
    public static final Coffee MOCHA = new Coffee(MOCHA_NAME, MOCHA_PRICE);
    public static final Coffee AMERICANO = new Coffee(AMERICANO_NAME, AMERICANO_PRICE);

}
