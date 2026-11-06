
import model.Expense;
import model.Income;
import manager.ExpenseManager;
import manager.IncomeManager;
import manager.BudgetManager;
import manager.SavingsGoalManager;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final ExpenseManager expenseManager = new ExpenseManager();
    private static final IncomeManager incomeManager = new IncomeManager();
    private static final BudgetManager budgetManager = new BudgetManager();
    private static final SavingsGoalManager savingsGoalManager =
            new SavingsGoalManager();

    public static void main(String[] args) {

        expenseManager.loadExpenses();
        incomeManager.loadIncomes();

        boolean running = true;

        while (running) {

            displayMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addExpense();
                    break;

                case 2:
                    expenseManager.viewExpenses();
                    break;

                case 3:
                    searchExpense();
                    break;

                case 4:
                    updateExpense();
                    break;

                case 5:
                    deleteExpense();
                    break;

                case 6:
                    System.out.println("Total Expenses: ₹"
                            + expenseManager.getTotalExpenses());
                    break;

                case 7:
                    expenseManager.showCategorySummary();
                    break;

                case 8:
                    addIncome();
                    break;

                case 9:
                    incomeManager.viewIncomes();
                    break;

                case 10:
                    searchIncome();
                    break;

                case 11:
                    deleteIncome();
                    break;

                case 12:
                    System.out.println("Total Income: ₹"
                            + incomeManager.getTotalIncome());
                    break;

                case 13:
                    viewBalance();
                    break;

                case 14:
                    showMonthlyExpenseReport();
                    break;

                case 15:
                    setMonthlyBudget();
                    break;

                case 16:
                    viewMonthlyBudget();
                    break;

                case 17:
                    budgetManager.viewAllBudgets();
                    break;

                case 18:
                    filterExpensesByCategory();
                    break;

                case 19:
                    filterExpensesByDateRange();
                    break;

                case 20:
                    updateIncome();
                    break;

                case 21:
                    searchExpensesByTitle();
                    break;

                case 22:
                    monthlyIncomeReport();
                    break;

                case 23:
                    showExpenseStatistics();
                    break;

                case 24:
                    showIncomeStatistics();
                    break;

                case 25:
                    setSavingsGoal();
                    break;

                case 26:
                    showSavingsProgress();
                    break;

                case 27:
                    System.out.println("You have selected Exit.");

                    expenseManager.saveExpenses();
                    incomeManager.saveIncomes();

                    System.out.println("Data saved successfully.");
                    System.out.println("Thank you for using Expense Tracker!");

                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Select 1-27.");
            }
        }

        sc.close();
    }

    // Display Menu
    private static void displayMenu() {

        System.out.println("\n========== EXPENSE TRACKER ==========");
        System.out.println("1. Add Expense");
        System.out.println("2. View Expenses");
        System.out.println("3. Search Expense by ID");
        System.out.println("4. Update Expense");
        System.out.println("5. Delete Expense");
        System.out.println("6. Calculate Total Expenses");
        System.out.println("7. Category-wise Expense Summary");
        System.out.println("8. Add Income");
        System.out.println("9. View Income");
        System.out.println("10. Search Income by ID");
        System.out.println("11. Delete Income");
        System.out.println("12. Calculate Total Income");
        System.out.println("13. View Balance");
        System.out.println("14. Monthly Expense Report");
        System.out.println("15. Set Monthly Budget");
        System.out.println("16. View Monthly Budget and Alerts");
        System.out.println("17. View All Budgets");
        System.out.println("18. Filter Expenses by Category");
        System.out.println("19. Filter Expenses by Date Range");
        System.out.println("20. Edit Income");
        System.out.println("21. Search Expenses by Title");
        System.out.println("22. Monthly Income Report");
        System.out.println("23. Expense Statistics Dashboard");
        System.out.println("24. Income Statistics Dashboard");
        System.out.println("25. Set Savings Goal");
        System.out.println("26. View Savings Progress");
        System.out.println("27. Exit");
        System.out.println("=====================================");
    }

    // Add Expense
    private static void addExpense() {

        System.out.println("\n--- Add Expense ---");

        int id = readInt("Enter Expense ID: ");

        if (expenseManager.searchExpense(id) != null) {
            System.out.println("Expense ID already exists.");
            return;
        }

        String title = readText("Enter Expense Title: ");
        double amount = readAmount("Enter Amount: ");
        String category = readText("Enter Category: ");
        String date = readDateString();

        Expense expense = new Expense(id, title, amount, category, date);

        expenseManager.addExpense(expense);

        checkBudgetForExpense(expense);
    }

    // Search Expense by ID
    private static void searchExpense() {

        int id = readInt("Enter Expense ID: ");

        Expense expense = expenseManager.searchExpense(id);

        if (expense != null) {
            System.out.println(expense);
        } else {
            System.out.println("Expense not found.");
        }
    }

    // Update Expense
    private static void updateExpense() {

        int id = readInt("Enter Expense ID to update: ");

        Expense existing = expenseManager.searchExpense(id);

        if (existing == null) {
            System.out.println("Expense not found.");
            return;
        }

        String title = readText("Enter New Title: ");
        double amount = readAmount("Enter New Amount: ");
        String category = readText("Enter New Category: ");
        String date = readDateString();

        expenseManager.updateExpense(id, title, amount, category, date);

        Expense updated = expenseManager.searchExpense(id);

        if (updated != null) {
            checkBudgetForExpense(updated);
        }
    }

    // Delete Expense
    private static void deleteExpense() {

        int id = readInt("Enter Expense ID to delete: ");

        expenseManager.deleteExpense(id);
    }

    // Add Income
    private static void addIncome() {

        System.out.println("\n--- Add Income ---");

        int id = readInt("Enter Income ID: ");

        if (incomeManager.searchIncome(id) != null) {
            System.out.println("Income ID already exists.");
            return;
        }

        String source = readText("Enter Income Source: ");
        double amount = readAmount("Enter Income Amount: ");
        String date = readDateString();

        Income income = new Income(id, source, amount, date);

        incomeManager.addIncome(income);
    }

    // Search Income
    private static void searchIncome() {

        int id = readInt("Enter Income ID: ");

        Income income = incomeManager.searchIncome(id);

        if (income != null) {
            System.out.println(income);
        } else {
            System.out.println("Income not found.");
        }
    }

    // Delete Income
    private static void deleteIncome() {

        int id = readInt("Enter Income ID to delete: ");

        incomeManager.deleteIncome(id);
    }

    // Feature 20: Edit Income
    private static void updateIncome() {

        System.out.println("\n--- Edit Income ---");

        int id = readInt("Enter Income ID to update: ");

        Income existingIncome = incomeManager.searchIncome(id);

        if (existingIncome == null) {
            System.out.println("Income not found.");
            return;
        }

        System.out.println("Current Income: " + existingIncome);

        String source = readText("Enter New Income Source: ");
        double amount = readAmount("Enter New Amount: ");
        String date = readDateString();

        incomeManager.updateIncome(id, source, amount, date);
    }

    // View Balance
    private static void viewBalance() {

        double income = incomeManager.getTotalIncome();
        double expenses = expenseManager.getTotalExpenses();

        System.out.println("\n--- Financial Summary ---");
        System.out.println("Total Income: ₹" + income);
        System.out.println("Total Expenses: ₹" + expenses);
        System.out.println("Current Balance: ₹" + (income - expenses));
    }

    // Monthly Expense Report
    private static void showMonthlyExpenseReport() {

        String month = readMonth();

        expenseManager.showMonthlyReport(month);
    }

    // Set Monthly Budget
    private static void setMonthlyBudget() {

        String month = readMonth();
        double amount = readAmount("Enter Monthly Budget: ");

        budgetManager.setBudget(month, amount);
    }

    // View Monthly Budget
    private static void viewMonthlyBudget() {

        String month = readMonth();

        budgetManager.viewBudget(month, expenseManager);
    }

    // Filter Expenses by Category
    private static void filterExpensesByCategory() {

        String category = readText("Enter Category: ");

        expenseManager.showExpensesByCategory(category);
    }

    // Filter Expenses by Date Range
    private static void filterExpensesByDateRange() {

        System.out.println("\n--- Filter Expenses by Date Range ---");

        LocalDate startDate = readDate("Enter Start Date (YYYY-MM-DD): ");
        LocalDate endDate = readDate("Enter End Date (YYYY-MM-DD): ");

        if (startDate.isAfter(endDate)) {
            System.out.println("Start date cannot be after end date.");
            return;
        }

        expenseManager.showExpensesByDateRange(
                startDate.toString(),
                endDate.toString()
        );
    }

    // Feature 21: Search Expenses by Title
    private static void searchExpensesByTitle() {

        System.out.println("\n--- Search Expenses by Title ---");

        String title = readText("Enter Expense Title or Keyword: ");

        expenseManager.searchExpensesByTitle(title);
    }

    // Feature 23: Monthly Income Report
    private static void monthlyIncomeReport() {

        System.out.println("\n--- Monthly Income Report ---");

        String month = readMonth();

        incomeManager.showMonthlyIncomeReport(month);
    }

    // Feature 24: Expense Statistics Dashboard
    private static void showExpenseStatistics() {

        System.out.println("\n--- Expense Statistics Dashboard ---");

        expenseManager.showExpenseStatistics();
    }

    // Feature 25: Income Statistics Dashboard
    private static void showIncomeStatistics() {

        System.out.println("\n--- Income Statistics Dashboard ---");

        incomeManager.showIncomeStatistics();
    }

    // Feature 26: Set Savings Goal
    private static void setSavingsGoal() {

        System.out.println("\n--- Set Savings Goal ---");

        double goal = readAmount("Enter Your Savings Goal: ₹");

        savingsGoalManager.setSavingsGoal(goal);
    }

    // Feature 26: View Savings Progress
    private static void showSavingsProgress() {

        double totalIncome = incomeManager.getTotalIncome();
        double totalExpenses = expenseManager.getTotalExpenses();

        double currentSavings = totalIncome - totalExpenses;

        savingsGoalManager.showSavingsProgress(currentSavings);
    }

    // Budget Alert
    private static void checkBudgetForExpense(Expense expense) {

        String month = expense.getDate().substring(0, 7);

        double spent = expenseManager.getMonthlyExpenses(month);

        budgetManager.checkBudget(month, spent);
    }

    // Read Integer
    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    // Read Amount
    private static double readAmount(String message) {

        while (true) {

            System.out.print(message);

            try {
                double amount = Double.parseDouble(sc.nextLine().trim());

                if (!Double.isFinite(amount) || amount <= 0) {
                    System.out.println("Amount must be greater than zero.");
                    continue;
                }

                return amount;

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid amount.");
            }
        }
    }

    // Read Text
    private static String readText(String message) {

        while (true) {

            System.out.print(message);

            String input = sc.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    // Read Date as String
    private static String readDateString() {

        while (true) {

            System.out.print("Enter Date (YYYY-MM-DD): ");

            String date = sc.nextLine().trim();

            try {
                LocalDate.parse(date);
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
            }
        }
    }

    // Read Date as LocalDate
    private static LocalDate readDate(String message) {

        while (true) {

            System.out.print(message);

            String date = sc.nextLine().trim();

            try {
                return LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use YYYY-MM-DD.");
            }
        }
    }

    // Read Month
    private static String readMonth() {

        while (true) {

            System.out.print("Enter Month (YYYY-MM): ");

            String month = sc.nextLine().trim();

            try {
                YearMonth.parse(month);
                return month;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid month format.");
            }
        }
    }
}