package com.kutayyaman.unittest.whatistheunittest;

public class FizzBuzz {

    public String stringFor(int number) {
        if (number < 1 || number > 100) {
            throw new IllegalArgumentException();
        }
        if (number % 15 == 0) {
            return "FizzBuzz";
        }
        if (number % 3 == 0) {
            return "Fizz";
        }
        if (number % 5 == 0) {
            return "Buzz";
        }
        return String.valueOf(number);
    }
}
