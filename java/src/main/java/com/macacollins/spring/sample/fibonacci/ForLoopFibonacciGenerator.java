package com.macacollins.spring.sample.fibonacci;

import java.math.BigInteger;

// Package scoped intentionally to force usage of the Factory
class ForLoopFibonacciGenerator implements FibonacciGenerator {

    public String calculateFibonacci(int sequenceNumber) {
        BigInteger result = getFibonacciForLoop(sequenceNumber);

        return result.toString();
    }

    // This method will handle arbitrary sized results
    // and will not fail due to recursion stack limits
    public static BigInteger getFibonacciForLoop(int sequenceNumber) {
        if (sequenceNumber == 0) {
            return BigInteger.ZERO;
        } else if (sequenceNumber == 1) {
            return BigInteger.ONE;
        } else {
            BigInteger oneBefore = BigInteger.ZERO;
            BigInteger twoBefore = BigInteger.ONE;
            BigInteger current = BigInteger.ZERO;

            for (int i = 2; i <= sequenceNumber; i++) {
                current = oneBefore.add(twoBefore);
                oneBefore = twoBefore;
                twoBefore = current;
            }

            return current;
        }
    }
}