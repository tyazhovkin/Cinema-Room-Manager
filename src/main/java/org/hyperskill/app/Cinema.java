package org.hyperskill.app;

import java.io.InputStream;
import java.util.Scanner;

public class Cinema {
    private Scanner scanner = new Scanner(System.in);
    private int rows;
    private int seats;
    private int[][] seatsArray;

    public int getRows() {
        return rows;
    }

    public int getSeats() {
        return seats;
    }

    public int[][] getSeatsArray() {
        return seatsArray;
    }

    public void start() {
        System.out.println("Enter the number of rows:");
        rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        seats = scanner.nextInt();
        seatsArray = new int[rows][seats];
    }

    public void setInputStream(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }

    public void displayMenu() {
        System.out.println();
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
    }

    public void displayStatistics() {
        int numberOfTickets = 0;
        int currentIncome = 0;
        int totalIncome = 0;
        int totalSeats = rows * seats;

        for (int i = 0; i < seatsArray.length; i++) {
            for (int j = 0; j < seatsArray[i].length; j++) {
                if (seatsArray[i][j] == 1) {
                    numberOfTickets++;
                    if (totalSeats <= 60) {
                        currentIncome += 10;
                    } else {
                        currentIncome += rows / 2 >= i + 1 ? 10 : 8;
                    }
                }
            }
        }

        if (totalSeats <= 60) {
            totalIncome = totalSeats * 10;
        } else {
            int t1, t2;
            t1 = rows / 2 * seats;
            if (rows % 2 == 0) {
                totalIncome = t1 * 10 + t1 * 8;
            } else {
                t2 = t1 + rows % 2 * seats;
                totalIncome = t1 * 10 + t2 * 8;
            }
        }

        double percentage = (double) numberOfTickets / (double) totalSeats * 100;

        System.out.println();
        System.out.printf("Number of purchased tickets: %d\n", numberOfTickets);
        System.out.printf("Percentage: %.2f%s\n", percentage, "%");
        System.out.printf("Current income: $%d\n", currentIncome);
        System.out.printf("Total income: $%d\n", totalIncome);
    }

    public void displayStatus() {
        System.out.println("Cinema:");
        for (int i = 0; i <= seats; i++) {
            if (i == 0) {
                System.out.print("  ");
            } else if (i != seats) {
                System.out.print(i + " ");
            }
            if (i == seats) {
                System.out.print(i);
                System.out.print("\n");
            }
        }
        for (int i = 0; i < seatsArray.length; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < seatsArray[i].length; j++) {
                char state = seatsArray[i][j] == 0 ? 'S' : 'B';
                if (j != rows) {
                    System.out.print(state + " ");
                } else {
                    System.out.print(state);
                }
            }
            System.out.print("\n");
        }
    }

    public void buyTicket() {
        System.out.println();
        System.out.println("Enter a row number:");
        int row = scanner.nextInt();
        System.out.println("Enter a seat number in that row:");
        int seat = scanner.nextInt();

        int totalSeats = rows * seats;
        int price = 0;

        if (totalSeats <= 60) {
            price = 10;
        } else {
            price = rows / 2 >= row ? 10 : 8;
        }

        try {
            if (seatsArray[row - 1][seat - 1] == 1) {
                System.out.println("\nThat ticket has already been purchased!");
                buyTicket();
            } else {
                System.out.printf("\nTicket price: $%d\n", price);
                seatsArray[row - 1][seat - 1] = 1;
            }
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Wrong input!");
            buyTicket();
        }
    }

    public static void main(String[] args) {
        Cinema cinema = new Cinema();
        cinema.start();

        boolean exit = false;
        while (!exit) {
            cinema.displayMenu();
            switch (cinema.scanner.nextInt()) {
                case 0:
                    exit = true;
                case 1:
                    cinema.displayStatus();
                    break;
                case 2:
                    cinema.buyTicket();
                    break;
                case 3:
                    cinema.displayStatistics();
                    break;
            }
        }
    }
}