package com.macacollins.spring.sample.fibonacci;

import org.junit.jupiter.api.*;

class BigIntFibonacciTests {

	@Test
	void sanityTest() {
		FibonacciGenerator generator = new BigIntFibonacciGenerator();

		String result = generator.calculateFibonacci(3);

		Assertions.assertTrue(result.equals("2"));
	}

	// Test up to 49 sequence items
	// This can be quite slow, so 50-300 were not included
	@Test
	void testFullRange() {
		FibonacciGenerator generator = new BigIntFibonacciGenerator();

		for (int sequenceNumber = 0; sequenceNumber < 48; sequenceNumber++) {
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
