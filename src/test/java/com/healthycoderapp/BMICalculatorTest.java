package com.healthycoderapp;

import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class BMICalculatorTest {

	private String enviroment = "prod";

	@BeforeAll
	static void beforeAll() {
		System.out.println("Before all unit tests");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("After all unit tests");
	}

	@Nested
	class isDietRecommendedTests {
		@ParameterizedTest(name = "weight = {0}, height = {1}")
		@CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
		void shouldReturnTrueWhenDietRecommended(Double coderWeight, Double coderHeight) {

			// given
			double weight = coderWeight;
			double height = coderHeight;

			// when

			boolean recommended = BMICalculator.isDietRecommended(weight, height);

			// then
			assertTrue(recommended);

		}

		@Test
		void shouldReturnFalseWhenDietNotRecommended() {

			// given
			double weight = 50.0;
			double height = 1.92;

			// when

			boolean recommended = BMICalculator.isDietRecommended(weight, height);

			// then
			assertFalse(recommended);

		}

		@Test
		void shouldThrowArithmeticExceptionWhenHeightZero() {
			// given
			double weight = 89.0;
			double height = 0.0;

			// when and then
			Executable executable = () -> BMICalculator.isDietRecommended(weight, height);
			assertThrows(ArithmeticException.class, executable);
		}
	}

	@Nested
	class FindCodeWithWorstBMITests {
		@Test
		@DisplayName(">>>>> sample method display name")
		@DisabledOnOs(OS.MAC)
		void shouldReturnCoderWithWorstBMIWhenCoderListNotEmpty() {
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.82, 64.7));

			Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
			assertAll(() -> assertEquals(1.82, coderWorstBMI.getHeight()),
					() -> assertEquals(98.0, coderWorstBMI.getWeight()));

		}

		@Test
		@Disabled
		void shouldReturnCoderWithWorstBMIIn1MsWhenCoderListHas10000Elements() {

			assumeTrue(BMICalculatorTest.this.enviroment.equals("prod"));
			List<Coder> coders = new ArrayList<>();
			for (int i = 0; i < 10000; i++) {
				coders.add(new Coder(1.0 + i, 10.0 + i));
			}
			Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);
			assertTimeout(Duration.ofMillis(500), executable);
		}

		@Test
		void shouldReturnNullWorstBMIWhenCoderListEmpty() {
			List<Coder> coders = new ArrayList<>();

			Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
			assertNull(coderWorstBMI);
		}
	}

	@Nested
	class GetBMIScoresTests {
		@Test
		void shouldReturnCorrecttBMIScoreArrayWhenCoderListNotEmpty() {
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.82, 64.7));

			double[] expected = { 18.52, 29.59, 19.53 };

			double[] bmiScores = BMICalculator.getBMIScores(coders);

			assertArrayEquals(expected, bmiScores);

		}
	}

}
