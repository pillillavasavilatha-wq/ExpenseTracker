
package manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SavingsGoalManager {

    private static final Path FILE_PATH = Path.of("savings_goal.txt");

    private double savingsGoal;

    public SavingsGoalManager() {
        loadSavingsGoal();
    }

    // Set savings goal
    public void setSavingsGoal(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Savings goal must be a positive amount.");
            return;
        }

        savingsGoal = amount;
        saveSavingsGoal();

        System.out.println("Savings goal saved successfully.");
    }

    // Get saved savings goal
    public double getSavingsGoal() {
        return savingsGoal;
    }

    // Calculate current savings
    public double getCurrentSavings(double totalIncome, double totalExpenses) {
        if (!Double.isFinite(totalIncome)
                || !Double.isFinite(totalExpenses)) {
            return 0;
        }

        return totalIncome - totalExpenses;
    }

    // Get remaining amount to reach goal
    public double getRemainingAmount(double currentSavings) {
        if (savingsGoal <= 0) {
            return 0;
        }

        return Math.max(0, savingsGoal - currentSavings);
    }

    // Get progress percentage
    public double getProgressPercentage(double currentSavings) {
        if (savingsGoal <= 0) {
            return 0;
        }

        double percentage = (currentSavings / savingsGoal) * 100;

        return Math.max(0, Math.min(100, percentage));
    }

    // Display savings progress
    public void showSavingsProgress(double currentSavings) {
        if (savingsGoal <= 0) {
            System.out.println("Please set a savings goal first.");
            return;
        }

        if (!Double.isFinite(currentSavings)) {
            System.out.println("Invalid current savings amount.");
            return;
        }

        double progress = getProgressPercentage(currentSavings);
        double remaining = getRemainingAmount(currentSavings);

        System.out.println("\nSavings Goal Progress");
        System.out.printf("Target: %.2f%n", savingsGoal);
        System.out.printf("Current savings: %.2f%n", currentSavings);
        System.out.printf("Progress: %.2f%%%n", progress);
        System.out.printf("Remaining: %.2f%n", remaining);

        if (currentSavings >= savingsGoal) {
            System.out.println("Congratulations! You reached your savings goal.");
        } else {
            System.out.println("Keep saving to reach your goal!");
        }
    }

    // Save goal using the existing plain-number format
    private void saveSavingsGoal() {
        try {
            Files.writeString(
                    FILE_PATH,
                    Double.toString(savingsGoal),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.out.println("Error saving savings goal: " + e.getMessage());
        }
    }

    // Load saved goal
    private void loadSavingsGoal() {
        if (!Files.exists(FILE_PATH)) {
            savingsGoal = 0;
            return;
        }

        try {
            String content = Files.readString(
                    FILE_PATH,
                    StandardCharsets.UTF_8
            ).trim();

            if (content.isEmpty()) {
                savingsGoal = 0;
                return;
            }

            double loadedGoal = Double.parseDouble(content);

            savingsGoal = Double.isFinite(loadedGoal) && loadedGoal > 0
                    ? loadedGoal
                    : 0;

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading savings goal: " + e.getMessage());
            savingsGoal = 0;
        }
    }
}
