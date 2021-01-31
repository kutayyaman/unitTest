package com.kutayyaman.unittest.whatistheunittest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FizzBuzzTest {

    private FizzBuzz fizzBuzz;

    @BeforeEach
    void setUp() {
        fizzBuzz = new FizzBuzz();
    }

    @Test
    void returnFizzWhenTheNumberIsDividedByThree() {

        Assertions.assertEquals("Fizz", fizzBuzz.stringFor(3));

    }

    @Test
    void returnBuzzWhenTheNumberIsDividedByFive() {
        Assertions.assertEquals("Buzz", fizzBuzz.stringFor(5));
    }

    @Test
    void returnFizzBuzzWhenTheNumberIsDividedBothOfThreeAndFive(){
        Assertions.assertEquals("FizzBuzz", fizzBuzz.stringFor(15));
    }

    @Test
    void returnTheNumberItselfWhenTheNumberIsNotDividedAnyOfThreeAndFive(){
        Assertions.assertEquals("7", fizzBuzz.stringFor(7));
    }

    @Test
    void 0(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> fizzBuzz.stringFor(-1));
        Assertions.assertThrows(IllegalArgumentException.class,() -> fizzBuzz.stringFor(101));

    }

}
