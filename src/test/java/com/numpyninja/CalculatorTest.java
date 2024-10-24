package com.numpyninja;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CalculatorTest {

	Calculator calculator = new Calculator();

	@Test
	public void testAdd() {
		assertEquals(5, calculator.add(2, 3));
	}

	@Test
	public void testSubtract() {
		assertEquals(1, calculator.subtract(3, 2));
	}

	@Test
	public void testMultiply() {
		assertEquals(6, calculator.multiply(2, 3));
	}

	@Test
	public void testDivide() {
		assertEquals(2, calculator.divide(6, 3));
	}

	@Test
	public void testDivideByZero() {
		assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
	}
}
