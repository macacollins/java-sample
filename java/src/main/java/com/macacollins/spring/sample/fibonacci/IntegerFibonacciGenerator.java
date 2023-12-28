package com.macacollins.spring.sample.fibonacci;

// Package scoped intentionally to force usage of the Factory
class IntegerFibonacciGenerator implements FibonacciGenerator {
    public String calculateFibonacci(int sequenceNumber) {
        int result = getFibonacciInt(sequenceNumber);

        return String.valueOf(result);
    }

    // This method only handles up to the 48th sequence number
    // At that point the result overflows and becomes negative
    // We could do unsigned to partially extend one
    // Or long to get up to the 92nd value in the sequence
    // BigInteger is the way for arbitrary size
    public int getFibonacciInt(int sequenceNumber) {
        if (sequenceNumber == 0) {
            return 0;
        } else if (sequenceNumber == 1) {
            return 1;
        } else {
            // Basic fibonacci algorithm
            return getFibonacciInt(sequenceNumber - 1) +
                    getFibonacciInt(sequenceNumber - 2);
        }
    }
}