package com.project.oag.config.identity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

import static com.project.oag.common.AppConstants.SECURE_RANDOM;

public class CustomIdGenerator implements IdentifierGenerator {

    private static final long MIN_START_VALUE = 11111L;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        long currentTimestamp = System.currentTimeMillis();
        long randomValue = SECURE_RANDOM.nextLong(MIN_START_VALUE, Long.MAX_VALUE);
        return currentTimestamp + randomValue;
    }
}