package com.macacollins.spring.sample.fibonacci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Return the proper class based on an environment variable
public class FibonacciFactory {
    private static FibonacciGenerator actualGenerator = null;

    private final static Logger logger = LoggerFactory.getLogger(FibonacciFactory.class);

    public static FibonacciGenerator getGenerator() {
        if (actualGenerator == null) {
            // You can override this in several ways
            // For instance, The kubernetes deployment YAML or the Dockerfile

            String fibonacciSetting = System.getenv("FIBONACCI_SETTING");

            if (fibonacciSetting == null) {
                logger.warn("The FIBONACCI_SETTING environment variable was not set. Defaulting to integer generation");
                logger.warn("FIBONACCI_SETTING environment variable should be one of: bigint, bigint-memoized, bigint-forloop, or int");
                actualGenerator = new IntegerFibonacciGenerator();

                return actualGenerator;
            }

            switch (System.getenv("FIBONACCI_SETTING")) {
                case "bigint":
                    logger.info("The FIBONACCI_SETTING environment variable was bigint. Returning BigInt generator.");
                    actualGenerator = new BigIntFibonacciGenerator();
                    break;
                case "bigint-memoized":
                    logger.info("The FIBONACCI_SETTING environment variable was bigint-memoized. Returning BigIntMemoized generator.");
                    actualGenerator = new BigIntMemoizedFibonacciGenerator();
                    break;
                case "bigint-forloop":
                    logger.info("The FIBONACCI_SETTING environment variable was bigint-forloop. Returning ForLoopFibonacci generator.");
                    actualGenerator = new ForLoopFibonacciGenerator();
                    break;
                case "int":
                default: // get any incorrect values
                    logger.info("The FIBONACCI_SETTING environment variable was int or other value. Returning int generator.");
                    actualGenerator = new IntegerFibonacciGenerator();
            }
        }

        return actualGenerator;
    }
}