package com.example.sixquiprend_xie_xu_yuan;

public class Card {
    private int number;
    private int bullheads;

    public Card(int number) {
        this.number = number;
        this.bullheads = calculateBullheads(number);
    }

    public int getNumber() {
        return number;
    }

    public int getBullheads() {
        return bullheads;
    }

    // This method calculates the bullheads based on the card number.
    private int calculateBullheads(int number) {
        if (number % 55 == 0) {
            return 7;
        } else if (number % 11 == 0) {
            return 5;
        } else if (number % 10 == 0) {
            return 3;
        } else if (number % 5 == 0) {
            return 2;
        } else {
            return 1;
        }
    }
}

