
package model;

import java.time.LocalDate;

public class Income {

    private final int id;
    private String source;
    private double amount;
    private String date;

    public Income(int id, String source, double amount, String date) {

        if (id <= 0) {
            throw new IllegalArgumentException("Income ID must be positive.");
        }

        this.id = id;
        setSource(source);
        setAmount(amount);
        setDate(date);
    }

    public int getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void setSource(String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be empty.");
        }
        this.source = source.trim();
    }

    public void setAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Income amount must be greater than zero.");
        }
        this.amount = amount;
    }

    public void setDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be empty.");
        }

        try {
            LocalDate.parse(date.trim());
            this.date = date.trim();
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date. Use YYYY-MM-DD format.");
        }
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Source: " + source +
                " | Amount: ₹" + amount +
                " | Date: " + date;
    }
}
