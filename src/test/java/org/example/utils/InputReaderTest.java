package org.example.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class InputReaderTest {

    private InputReader createReaderWithInput(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        return new InputReader(scanner);
    }

    @Test
    void testReadString() {
        InputReader reader = createReaderWithInput("Hello World\n");
        String result = reader.readString("Enter: ");
        assertEquals("Hello World", result);
    }

    @Test
    void testReadStringRejectsEmpty() {
        InputReader reader = createReaderWithInput("\nValid Input\n");
        String result = reader.readString("Enter: ");
        assertEquals("Valid Input", result);
    }

    @Test
    void testReadLong() {
        InputReader reader = createReaderWithInput("123\n");
        long result = reader.readLong("Enter: ");
        assertEquals(123L, result);
    }

    @Test
    void testReadLongRejectsInvalid() {
        InputReader reader = createReaderWithInput("abc\n456\n");
        long result = reader.readLong("Enter: ");
        assertEquals(456L, result);
    }

    @Test
    void testReadBigDecimal() {
        InputReader reader = createReaderWithInput("123.45\n");
        BigDecimal result = reader.readBigDecimal("Enter: ");
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    void testReadBigDecimalWithComma() {
        InputReader reader = createReaderWithInput("123,45\n");
        BigDecimal result = reader.readBigDecimal("Enter: ");
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    void testReadDouble() {
        InputReader reader = createReaderWithInput("99.9\n");
        double result = reader.readDouble("Enter: ");
        assertEquals(99.9, result, 0.001);
    }

    @Test
    void testReadDate() {
        InputReader reader = createReaderWithInput("2024-06-15\n");
        LocalDate result = reader.readDate("Enter: ");
        assertEquals(LocalDate.of(2024, 6, 15), result);
    }

    @Test
    void testReadDateRejectsInvalid() {
        InputReader reader = createReaderWithInput("15-06-2024\n2024-06-15\n");
        LocalDate result = reader.readDate("Enter: ");
        assertEquals(LocalDate.of(2024, 6, 15), result);
    }
}
