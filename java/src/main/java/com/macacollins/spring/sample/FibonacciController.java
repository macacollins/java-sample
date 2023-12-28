package com.macacollins.spring.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.macacollins.spring.sample.fibonacci.FibonacciFactory;
import com.macacollins.spring.sample.fibonacci.FibonacciGenerator;

@RestController
public class FibonacciController {

    // 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, ....
    // GET is appropriate because this method can be cached
    @GetMapping("/fibonacci")
    public String getFibonacciNumber(
            @RequestParam(name = "sequenceNumber", defaultValue = "0") String sequenceNumber) {
        String result = "-1";

        try {
            FibonacciGenerator generator = FibonacciFactory.getGenerator();

            result = generator.calculateFibonacci(Integer.valueOf(sequenceNumber));
        } catch (NumberFormatException e) {
            // TODO return 500 result here
        }

        return result;
    }
}