package com.macacollins.spring.sample.fibonacci;

import org.junit.jupiter.api.*;

class IntegerFibonacciTests {

	@Test
	void sanityTest() {
		FibonacciGenerator generator = new IntegerFibonacciGenerator();

		String result = generator.calculateFibonacci(3);

		Assertions.assertTrue(result.equals("2"));
	}

	// Test the whole range of possible valid integer inputs
	// After 47, the result overflows the int range
	@Test
	void testIntRange() {
		FibonacciGenerator generator = new IntegerFibonacciGenerator();

		for (int sequenceNumber = 0; sequenceNumber < 47; sequenceNumber++) {
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

	// Normally a bad case like this would mean removing or patching the code in question
	// This is included to make sure the documentation is correct
	@Test
	void testOverflow() {
		FibonacciGenerator generator = new IntegerFibonacciGenerator();

		String result = generator.calculateFibonacci(47);

		Assertions.assertTrue(result.startsWith("-"),
				String.format("Got %s for %d, expecting negative number", result, 48));
	}
}
