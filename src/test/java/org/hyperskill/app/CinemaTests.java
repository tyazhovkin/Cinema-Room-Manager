package org.hyperskill.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class CinemaTests {
    private static ByteArrayOutputStream output;
    private static ByteArrayInputStream input;
    private static final PrintStream DEFAULT_STDOUT = System.out;
    private static final InputStream DEFAULT_STDIN = System.in;

    private void provideInput(Cinema cinema, String data) {
        input = new ByteArrayInputStream(data.getBytes());
        cinema.setInputStream(input);
    }

    private String getOutput() {
        return output.toString();
    }

    @BeforeEach
    public void setUpStreams() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void rollbackChangesToStdout() {
        System.setOut(DEFAULT_STDOUT);
    }

    @AfterEach
    public void rollbackChangesToStdin() {
        System.setIn(DEFAULT_STDIN);
    }

    @Test
    public void test_checkDisplayMenu() {
        boolean result = true;
        Cinema cinema = new Cinema();
        cinema.displayMenu();
        String stringOutput = getOutput().toLowerCase();
        if (!stringOutput.contains("show the seats") ||
                !stringOutput.contains("buy a ticket") ||
                !stringOutput.contains("statistics") ||
                !stringOutput.contains("exit")) {
            result = false;
        }
        if (result) {
            Assertions.assertTrue(result);
        } else {
            Assertions.fail("Menu should contain 4 items:\n" +
                    "1. Show the seats\n" +
                    "2. Buy a ticket\n" +
                    "3. Statistics\n" +
                    "0. Exit");
        }
    }

    @Test
    public void test_checkBuyTicket() {
        Cinema cinema = new Cinema();
        provideInput(cinema, "8\n8");
        cinema.start();
        provideInput(cinema, "7\n5\n7\n5\n1\n5");
        cinema.buyTicket();
        checkTicketPrice(getOutput(), "$8");
        setUpStreams();
        cinema.buyTicket();
        checkTicketAlreadyPurchased(getOutput(), "That ticket has already been purchased!");
        checkTicketPrice(getOutput(), "$10");
    }

    @Test
    public void test_checkStatistics() {
        Cinema cinema = new Cinema();
        provideInput(cinema, "8\n8");
        cinema.start();

        checkNumberOfPurchasedTickets(getOutput(), "0");
        checkPercentage(getOutput(), "0%");
        checkCurrentIncome(getOutput(), "$0");
        checkTotalIncome(getOutput(), "$576");

        provideInput(cinema, "7\n5");
        cinema.buyTicket();
        cinema.displayStatistics();

        checkNumberOfPurchasedTickets(getOutput(), "1");
        checkPercentage(getOutput(), "1.56%");
        checkCurrentIncome(getOutput(), "$8");
        checkTotalIncome(getOutput(), "$576");

        setUpStreams();
        provideInput(cinema, "4\n7");
        cinema.buyTicket();
        cinema.displayStatistics();

        checkNumberOfPurchasedTickets(getOutput(), "2");
        checkPercentage(getOutput(), "3.13%");
        checkCurrentIncome(getOutput(), "$18");
        checkTotalIncome(getOutput(), "$576");
    }

    private void checkTicketPrice(String output, String correctNumber) {
        String[] splittedOutput = output.trim().split("\n");
        for (String line : splittedOutput) {
            if (line.toLowerCase().contains("ticket price")) {
                line = line.toLowerCase().replace("ticket price", "").replace(":", "").trim();
                Assertions.assertEquals(line, correctNumber, "Wrong ticket price!");
            }
        }
    }

    private void checkTicketAlreadyPurchased(String output, String correctMessage) {
        String[] splittedOutput = output.trim().split("\n");
        for (String line : splittedOutput) {
            if (line.toLowerCase().contains("that ticket has already been purchased!")) {
                line = line.replace("\r", "");
                Assertions.assertEquals(line, correctMessage, "Wrong ticket price!");
            }
        }
    }

    private void checkNumberOfPurchasedTickets(String output, String correctNumber) {
        String[] splittedOutput = output.trim().split("\n");
        for (String line : splittedOutput) {
            if (line.toLowerCase().contains("number of purchased tickets")) {
                line = line.toLowerCase().replace("number of purchased tickets", "").replace(":", "").trim();
                Assertions.assertEquals(line, correctNumber, "Wrong number of purchased tickets!");
            }
        }
    }

    private void checkPercentage(String output, String correctNumber) {
        String[] splittedOutput = output.trim().split("\n");
        for (String line : splittedOutput) {
            if (line.toLowerCase().contains("percentage")) {
                line = line.toLowerCase().replace("percentage", "").replace(":", "").replace(",", ".").trim();
                Assertions.assertEquals(line, correctNumber, "Wrong percentage!");
            }
        }
    }

    private void checkCurrentIncome(String output, String correctNumber) {
        String[] splittedOutput = output.trim().split("\n");
        for (String line : splittedOutput) {
            if (line.toLowerCase().contains("current income")) {
                line = line.toLowerCase().replace("current income", "").replace(":", "").trim();
                Assertions.assertEquals(line, correctNumber, "Wrong current income!");
            }
        }

    }

    private void checkTotalIncome(String output, String correctNumber) {
        String[] splittedOutput = output.trim().split("\n");
        boolean isFound = false;

        for (String line : splittedOutput) {
            if (line.toLowerCase().contains("total income")) {
                line = line.toLowerCase().replace("total income", "").replace(":", "").trim();
                Assertions.assertEquals(line, correctNumber, "Wrong total income!");
            }
        }
    }
}
