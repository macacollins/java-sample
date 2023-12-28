package com.macacollins.spring.sample.fibonacci;

import java.math.BigInteger;

// Package scoped intentionally to force usage of the Factory
class BigIntFibonacciGenerator implements FibonacciGenerator {

    public String calculateFibonacci(int sequenceNumber) {
        BigInteger result = getFibonacciBigInt(sequenceNumber);

        return result.toString();
    }

    // This method will handle arbitrary sized results
    // But in practice won't work as a REST service
    // Due to the common limit of 30 seconds
    private BigInteger getFibonacciBigInt(int sequenceNumber) {
        if (sequenceNumber == 0) {
            return BigInteger.ZERO;
        } else if (sequenceNumber == 1) {
            return BigInteger.ONE;
        } else {
            return getFibonacciBigInt(sequenceNumber - 1).add(
                    getFibonacciBigInt(sequenceNumber - 2));
        }
    }
}