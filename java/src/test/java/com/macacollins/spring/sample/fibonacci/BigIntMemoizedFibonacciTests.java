package com.macacollins.spring.sample.fibonacci;

import org.junit.jupiter.api.*;

class BigIntMemoizedFibonacciTests {

	@Test
	void sanityTest() {
		FibonacciGenerator generator = new BigIntMemoizedFibonacciGenerator();

		String result = generator.calculateFibonacci(3);

		Assertions.assertTrue(result.equals("2"));
	}

	// Test up to 300 sequence items
	@Test
	void test300() {
		FibonacciGenerator generator = new BigIntMemoizedFibonacciGenerator();

		for (int sequenceNumber = 0; sequenceNumber < 300; sequenceNumber++) {
			String result = generator.calculateFibonacci(sequenceNumber);

			String expected = FibonacciNumbers.SEQUENCE[sequenceNumber];

			Assertions.assertEquals(
					expected,
					result,
					String.format("Got %s for %d, expecting %s",
							result,
							sequenceNumber,
							expected));
		}
	}

}
