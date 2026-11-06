
package manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BudgetManager {

    private final Map<String, Double> budgets = new LinkedHashMap<>();
    private static final Path FILE_PATH = Path.of("budgets.txt");

    public BudgetManager() {
        loadBudgets();
    }

    // Set or update a monthly budget
    public void setBudget(String month, double amount) {
        if (!isValidMonth(month) || !isValidAmount(amount)) {
            System.out.println("Invalid month or budget amount.");
            return;
        }

        budgets.put(month, amount);
        saveBudgets();
        System.out.println("Budget saved successfully.");
    }

    // View a monthly budget
    public void viewBudget(String month) {
        Double budget = getBudget(month);

        if (budget == null) {
            System.out.println("No budget set for this month.");
            return;
        }

        System.out.printf("Budget for %s: %.2f%n", month, budget);
    }

    // View budget with expense information
    public void viewBudget(String month, ExpenseManager expenseManager) {
        if (!isValidMonth(month) || expenseManager == null) {
            System.out.println("Invalid month or expense manager.");
            return;
        }

        Double budget = getBudget(month);

        if (budget == null) {
            System.out.println("No budget set for this month.");
            return;
        }

        double spent = expenseManager.getMonthlyExpenses(month);
        double remaining = budget - spent;

        System.out.printf("Monthly budget: %.2f%n", budget);
        System.out.printf("Amount spent: %.2f%n", spent);
        System.out.printf("Remaining budget: %.2f%n", remaining);

        if (remaining < 0) {
            System.out.println("ALERT: You have exceeded your budget!");
        } else {
            System.out.println("You are within your budget.");
        }
    }

    // Check budget
    public void checkBudget(String month, double spent) {
        Double budget = getBudget(month);

        if (budget == null) {
            System.out.println("No budget set for this month.");
            return;
        }

        if (!Double.isFinite(spent) || spent < 0) {
            System.out.println("Invalid expense amount.");
            return;
        }

        if (spent > budget) {
            System.out.println("ALERT: Monthly budget exceeded!");
        } else if (spent >= budget * 0.8) {
            System.out.println("WARNING: You have used 80% or more of your budget.");
        } else {
            System.out.println("Budget is under control.");
        }
    }

    // View all budgets
    public void viewAllBudgets() {
        if (budgets.isEmpty()) {
            System.out.println("No budgets available.");
            return;
        }

        for (Map.Entry<String, Double> entry : budgets.entrySet()) {
            System.out.printf("%s : %.2f%n",
                    entry.getKey(), entry.getValue());
        }
    }

    // Get budget for one month
    public Double getBudget(String month) {
        if (!isValidMonth(month)) {
            return null;
        }

        return budgets.get(month);
    }

    // Get a copy of all budgets
    public Map<String, Double> getAllBudgets() {
        return new LinkedHashMap<>(budgets);
    }

    // Get spent amount
    public double getSpent(String month, ExpenseManager expenseManager) {
        if (!isValidMonth(month) || expenseManager == null) {
            return 0;
        }

        return expenseManager.getMonthlyExpenses(month);
    }

    // Get remaining budget
    public double getRemaining(String month, ExpenseManager expenseManager) {
        Double budget = getBudget(month);

        if (budget == null) {
            return 0;
        }

        return budget - getSpent(month, expenseManager);
    }

    // Get budget usage percentage
    public double getUsagePercentage(String month, ExpenseManager expenseManager) {
        Double budget = getBudget(month);

        if (budget == null || budget <= 0) {
            return 0;
        }

        double percentage =
                (getSpent(month, expenseManager) / budget) * 100;

        return Math.max(0, percentage);
    }

    // Check if budget is exceeded
    public boolean isBudgetExceeded(String month, ExpenseManager expenseManager) {
        Double budget = getBudget(month);

        return budget != null
                && getSpent(month, expenseManager) > budget;
    }

    // Save budgets without changing the existing file format
    public void saveBudgets() {
        java.util.List<String> lines = new java.util.ArrayList<>();

        for (Map.Entry<String, Double> entry : budgets.entrySet()) {
            lines.add(entry.getKey() + "|" + entry.getValue());
        }

        try {
            Files.write(FILE_PATH, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error saving budgets: " + e.getMessage());
        }
    }

    // Load existing budgets
    public void loadBudgets() {
        budgets.clear();

        if (!Files.exists(FILE_PATH)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8)) {
                if (line == null || line.isBlank()) {
                    continue;
                }

                try {
                    String[] parts = line.split("\\|", -1);

                    if (parts.length != 2) {
                        continue;
                    }

                    String month = parts[0].trim();
                    double amount = Double.parseDouble(parts[1].trim());

                    if (isValidMonth(month) && isValidAmount(amount)) {
                        budgets.put(month, amount);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid budget record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading budgets: " + e.getMessage());
        }
    }

    private boolean isValidAmount(double amount) {
        return Double.isFinite(amount) && amount > 0;
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
}
