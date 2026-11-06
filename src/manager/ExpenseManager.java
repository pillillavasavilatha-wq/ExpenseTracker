
package manager;

import model.Expense;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExpenseManager {

    private final ArrayList<Expense> expenses = new ArrayList<>();
    private static final Path FILE_PATH = Path.of("expenses.txt");

    public ExpenseManager() {
        loadExpenses();
    }

    // 1. Add expense
    public void addExpense(Expense expense) {
        if (expense == null) {
            System.out.println("Expense cannot be null.");
            return;
        }

        if (searchExpense(expense.getId()) != null) {
            System.out.println("Expense ID already exists.");
            return;
        }

        if (!isValidAmount(expense.getAmount())
                || !isValidDate(expense.getDate())
                || isBlank(expense.getTitle())
                || isBlank(expense.getCategory())) {
            System.out.println("Invalid expense details.");
            return;
        }

        expenses.add(expense);
        System.out.println("Expense added successfully.");
    }

    // 2. View all expenses
    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        for (Expense expense : expenses) {
            printExpense(expense);
        }
    }

    // 3. Search expense by ID
    public Expense searchExpense(int id) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }
        return null;
    }

    // 4. Update expense
    public void updateExpense(int id, String title, double amount,
                              String category, String date) {
        Expense expense = searchExpense(id);

        if (expense == null) {
            System.out.println("Expense not found.");
            return;
        }

        if (isBlank(title) || isBlank(category)
                || !isValidAmount(amount) || !isValidDate(date)) {
            System.out.println("Invalid expense details.");
            return;
        }

        expense.setTitle(title.trim());
        expense.setAmount(amount);
        expense.setCategory(category.trim());
        expense.setDate(date);

        System.out.println("Expense updated successfully.");
    }

    // 5. Delete expense
    public void deleteExpense(int id) {
        Expense expense = searchExpense(id);

        if (expense == null) {
            System.out.println("Expense not found.");
            return;
        }

        expenses.remove(expense);
        System.out.println("Expense deleted successfully.");
    }

    // 6. Calculate total expenses
    public double getTotalExpenses() {
        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        return total;
    }

    // 7. Calculate expenses for a month (format: yyyy-MM)
    public double getMonthlyExpenses(String month) {
        if (!isValidMonth(month)) {
            return 0;
        }

        double total = 0;

        for (Expense expense : expenses) {
            if (expense.getDate().startsWith(month)) {
                total += expense.getAmount();
            }
        }

        return total;
    }

    // 8. Category-wise expense summary
    public void showCategorySummary() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses available.");
            return;
        }

        Map<String, Double> summary = new LinkedHashMap<>();

        for (Expense expense : expenses) {
            summary.merge(
                    expense.getCategory(),
                    expense.getAmount(),
                    Double::sum
            );
        }

        System.out.println("\nCategory-wise Expense Summary");

        for (Map.Entry<String, Double> entry : summary.entrySet()) {
            System.out.printf("%s : %.2f%n",
                    entry.getKey(), entry.getValue());
        }
    }

    // 9. Monthly expense report
    public void showMonthlyReport(String month) {
        if (!isValidMonth(month)) {
            System.out.println("Enter month in yyyy-MM format.");
            return;
        }

        System.out.println("\nExpense Report for " + month);

        boolean found = false;

        for (Expense expense : expenses) {
            if (expense.getDate().startsWith(month)) {
                printExpense(expense);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found for this month.");
        }

        System.out.printf("Total: %.2f%n", getMonthlyExpenses(month));
    }

    // 10. Save expenses
    public void saveExpenses() {
        ArrayList<String> lines = new ArrayList<>();

        for (Expense expense : expenses) {
            String line = "V2|"
                    + expense.getId() + "|"
                    + encode(expense.getTitle()) + "|"
                    + expense.getAmount() + "|"
                    + encode(expense.getCategory()) + "|"
                    + encode(expense.getDate());

            lines.add(line);
        }

        try {
            Files.write(FILE_PATH, lines, StandardCharsets.UTF_8);
            System.out.println("Expenses saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    // 11. Load expenses
    public void loadExpenses() {
        expenses.clear();

        if (!Files.exists(FILE_PATH)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8)) {
                if (line == null || line.isBlank()) {
                    continue;
                }

                try {
                    Expense expense;

                    if (line.startsWith("V2|")) {
                        expense = parseV2(line);
                    } else {
                        expense = parseLegacy(line);
                    }

                    if (expense != null && searchExpense(expense.getId()) == null) {
                        expenses.add(expense);
                    }

                } catch (RuntimeException e) {
                    System.out.println("Skipping invalid expense record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }

    // 12. Filter expenses by category
    public void showExpensesByCategory(String category) {
        if (isBlank(category)) {
            System.out.println("Category cannot be empty.");
            return;
        }

        boolean found = false;

        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(category.trim())) {
                printExpense(expense);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found in this category.");
        }
    }

    // 13. Filter expenses by date range
    public void showExpensesByDateRange(String startDate, String endDate) {
        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            System.out.println("Dates must use yyyy-MM-dd format.");
            return;
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (start.isAfter(end)) {
            System.out.println("Start date cannot be after end date.");
            return;
        }

        boolean found = false;

        for (Expense expense : expenses) {
            LocalDate expenseDate = LocalDate.parse(expense.getDate());

            if (!expenseDate.isBefore(start) && !expenseDate.isAfter(end)) {
                printExpense(expense);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found in this date range.");
        }
    }

    // 14. Search expenses by title
    public void searchExpensesByTitle(String title) {
        if (isBlank(title)) {
            System.out.println("Enter a title to search.");
            return;
        }

        boolean found = false;

        for (Expense expense : expenses) {
            if (expense.getTitle().toLowerCase()
                    .contains(title.trim().toLowerCase())) {
                printExpense(expense);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching expenses found.");
        }
    }

    // 15. Expense statistics
    public void showExpenseStatistics() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses available for statistics.");
            return;
        }

        double total = getTotalExpenses();
        double highest = -Double.MAX_VALUE;
        double lowest = Double.MAX_VALUE;
        Expense highestExpense = null;
        Expense lowestExpense = null;

        for (Expense expense : expenses) {
            double amount = expense.getAmount();

            if (amount > highest) {
                highest = amount;
                highestExpense = expense;
            }

            if (amount < lowest) {
                lowest = amount;
                lowestExpense = expense;
            }
        }

        System.out.println("\nExpense Statistics");
        System.out.printf("Total expenses: %.2f%n", total);
        System.out.printf("Number of expenses: %d%n", expenses.size());
        System.out.printf("Average expense: %.2f%n", total / expenses.size());

        if (highestExpense != null) {
            System.out.printf("Highest expense: %s (%.2f)%n",
                    highestExpense.getTitle(), highest);
        }

        if (lowestExpense != null) {
            System.out.printf("Lowest expense: %s (%.2f)%n",
                    lowestExpense.getTitle(), lowest);
        }
    }

    // 16. Get all expenses for GUI tables and reports
    public ArrayList<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    // Helper: parse V2 record
    private Expense parseV2(String line) {
        String[] parts = line.split("\\|", -1);

        if (parts.length != 6) {
            return null;
        }

        int id = Integer.parseInt(parts[1]);
        String title = decode(parts[2]);
        double amount = Double.parseDouble(parts[3]);
        String category = decode(parts[4]);
        String date = decode(parts[5]);

        if (id <= 0 || isBlank(title) || isBlank(category)
                || !isValidAmount(amount) || !isValidDate(date)) {
            return null;
        }

        return new Expense(id, title, amount, category, date);
    }

    // Helper: support older comma-separated records
    private Expense parseLegacy(String line) {
        String[] parts = line.split(",", -1);

        if (parts.length != 5) {
            return null;
        }

        int id = Integer.parseInt(parts[0].trim());
        String title = parts[1].trim();
        double amount = Double.parseDouble(parts[2].trim());
        String category = parts[3].trim();
        String date = parts[4].trim();

        if (id <= 0 || isBlank(title) || isBlank(category)
                || !isValidAmount(amount) || !isValidDate(date)) {
            return null;
        }

        return new Expense(id, title, amount, category, date);
    }

    private String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decode(String value) {
        return new String(
                Base64.getUrlDecoder().decode(value),
                StandardCharsets.UTF_8
        );
    }

    private boolean isValidAmount(double amount) {
        return Double.isFinite(amount) && amount > 0;
    }

    private boolean isValidDate(String date) {
        if (date == null || date.isBlank()) {
            return false;
        }

        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidMonth(String month) {
        if (month == null || !month.matches("\\d{4}-\\d{2}")) {
            return false;
        }

        try {
            YearMonth.parse(month);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void printExpense(Expense expense) {
        System.out.printf(
                "ID: %d | Title: %s | Amount: %.2f | Category: %s | Date: %s%n",
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate()
        );
    }
}
