package com.banking_application.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class CustomAccountNumberGenerator {

    public static String accountNumberGenerator() {
        long timestamp = System.currentTimeMillis();
        int randomNum = new Random().nextInt(899) + 100;
        return String.format("ACC%d%d", timestamp, randomNum);
    }
}
