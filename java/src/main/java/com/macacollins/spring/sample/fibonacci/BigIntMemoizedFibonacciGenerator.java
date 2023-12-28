package com.macacollins.spring.sample.fibonacci;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

// Package scoped intentionally to force usage of the Factory
class BigIntMemoizedFibonacciGenerator implements FibonacciGenerator {

    public String calculateFibonacci(int sequenceNumber) {
        BigInteger result = getFibonacciBigIntMemoized(sequenceNumber);

        return result.toString();
    }

    // Because the fibonacci function is pure, it's a natural fit for memoization
    // BigIntegers also aren't very large, so storage is relatively cheap
    // See prometheus testing for impact to memory
    private Map<Integer, BigInteger> memo = new HashMap<>();

    // This method handles arbitrary sized results
    private BigInteger getFibonacciBigIntMemoized(int sequenceNumber) {
        // If the service already calculated the number, no need to do it again
        if (memo.containsKey(sequenceNumber)) {
            return memo.get(sequenceNumber);
        }

        BigInteger result;
        if (sequenceNumber == 0) {
            result = BigInteger.ZERO;
        } else if (sequenceNumber == 1) {
            result = BigInteger.ONE;
        } else {
            result = getFibonacciBigIntMemoized(sequenceNumber - 1).add(
                    getFibonacciBigIntMemoized(sequenceNumber - 2));
        }

        // Save the new computed result
        memo.put(sequenceNumber, result);

        return result;
    }
}