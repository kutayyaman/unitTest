package com.kutayyaman.unittest.primefactor;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrimeFactorsTest {

    private Map<Integer, List<Integer>> primeFactorExpectations = new HashMap<>();

    @BeforeAll
    void setUp() {
        primeFactorExpectations.put(1, Collections.EMPTY_LIST);
        primeFactorExpectations.put(2, Arrays.asList(2));
        primeFactorExpectations.put(3, Arrays.asList(3));
        primeFactorExpectations.put(4, Arrays.asList(2, 2));
        primeFactorExpectations.put(5, Arrays.asList(5));
        primeFactorExpectations.put(6, Arrays.asList(2, 3));
        primeFactorExpectations.put(7, Arrays.asList(7));
        primeFactorExpectations.put(8, Arrays.asList(2, 2, 2));
        primeFactorExpectations.put(9, Arrays.asList(3, 3));
    }

    @Test
    @DisplayName("Generate PrimeFactors")
    void generateWithStandartTest() {

        assertEquals(Collections.EMPTY_LIST, PrimeFactors.generate(1));
        assertEquals(new ArrayList<>(Arrays.asList(2)), PrimeFactors.generate(2));
        assertEquals(new ArrayList<>(Arrays.asList(3)), PrimeFactors.generate(3));
        assertEquals(new ArrayList<>(Arrays.asList(2, 2)), PrimeFactors.generate(4));
        assertEquals(new ArrayList<>(Arrays.asList(5)), PrimeFactors.generate(5));
        assertEquals(new ArrayList<>(Arrays.asList(2, 3)), PrimeFactors.generate(6));
        assertEquals(new ArrayList<>(Arrays.asList(7)), PrimeFactors.generate(7));
        assertEquals(new ArrayList<>(Arrays.asList(2, 2, 2)), PrimeFactors.generate(8));
        assertEquals(new ArrayList<>(Arrays.asList(3, 3)), PrimeFactors.generate(9));
    }

    @RepeatedTest(9)
    void generateWithRepeatedTest(RepetitionInfo repetitionInfo) {
        assertEquals(primeFactorExpectations.get(repetitionInfo.getCurrentRepetition()), PrimeFactors.generate(repetitionInfo.getCurrentRepetition()));
    }

    @ParameterizedTest(name = "Generate Prime Factors for {arguments}")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void generateWitParameterizedTest(Integer number) {
        assertEquals(primeFactorExpectations.get(number), PrimeFactors.generate(number));
    }

    @TestFactory
    Stream<DynamicTest> generateWithDynamicTest() {
        return Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .map(number -> DynamicTest.dynamicTest("Generate prime factors for " + number,
                        () -> assertEquals(primeFactorExpectations.get(number), PrimeFactors.generate(number))));
    }

}