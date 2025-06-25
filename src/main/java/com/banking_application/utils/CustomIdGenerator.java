package com.banking_application.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        long timestamp = System.currentTimeMillis();
        int randomNum = new Random().nextInt(1000);
        return timestamp * 1000 + randomNum;
    }
}
