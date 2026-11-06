
package model;

import java.time.LocalDate;

public class Expense {

    private final int id;
    private String title;
    private double amount;
    private String category;
    private String date;

    public Expense(int id, String title, double amount,
                   String category, String date) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }

        this.id = id;
        setTitle(title);
        setAmount(amount);
        setCategory(category);
        setDate(date);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        this.title = title.trim();
    }

    public void setAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        this.amount = amount;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        this.category = category.trim();
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
                " | Title: " + title +
                " | Amount: ₹" + amount +
                " | Category: " + category +
                " | Date: " + date;
    }
}
