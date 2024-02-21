package com.macacollins.spring.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.annotation.Timed;

import com.macacollins.spring.sample.fibonacci.FibonacciFactory;
import com.macacollins.spring.sample.fibonacci.FibonacciGenerator;

@RestController
public class FibonacciController {

    // This should maybe be injected in a ServletFilter somewhere instead
    private MeterRegistry registry;

    public FibonacciController(MeterRegistry registry) {
        this.registry = registry;
    }

    private static final String METRIC_NAME = "fibonacci.status";

    // 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, ....
    // GET is appropriate because this endpoint can be cached
    @GetMapping("/fibonacci")
    @Timed(
            value = "fibonacci.get",
            description = "Time to calculate fibonacci values",
            percentiles = { 0.05, 0.1, 0.2, 0.5, 0.75, 0.9, 0.99, 0.999 }
    )
    public String getFibonacciNumber(
            @RequestParam(name = "sequenceNumber") String sequenceNumber) {

        String result;

        try {
            FibonacciGenerator generator = FibonacciFactory.getGenerator();

            result = generator.calculateFibonacci(Integer.valueOf(sequenceNumber));
        } catch (NumberFormatException e) {
            registry.counter(METRIC_NAME, "status", "400").increment();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid sequence number supplied: " + sequenceNumber);
        }

        if (result.startsWith("-")) {
            // This is a rough way to check for negativity, but it needs to work with any of the FibonacciGenerators
            // This won't catch results that overflow back to positive. Limited precision numbers are bad for this
            registry.counter(METRIC_NAME, "status", "500").increment();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Integer overflow detected--returning 500");
        }

        registry.counter(METRIC_NAME, "status", "200").increment();
        return result;
    }
}