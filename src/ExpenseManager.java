import java.util.ArrayList;
import java.io.*;

public class ExpenseManager {

    private ArrayList<Expense> expenses = new ArrayList<>();

    // Add expense
    public void addExpense(Expense expense) {
        expenses.add(expense);
        System.out.println("Expense added successfully!");
    }

    // View all expenses
    public void viewExpenses() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    // Search expense by ID
    public void searchExpense(int id) {

        for (Expense expense : expenses) {

            if (expense.getId() == id) {
                System.out.println(expense);
                return;
            }
        }

        System.out.println("Expense not found.");
    }

    // Delete expense by ID
    public void deleteExpense(int id) {

        for (int i = 0; i < expenses.size(); i++) {

            if (expenses.get(i).getId() == id) {
                expenses.remove(i);
                System.out.println("Expense deleted successfully!");
                return;
            }
        }

        System.out.println("Expense not found.");
    }

    // Update expense
    public void updateExpense(int id, String title, double amount,
                              String category, String date) {

        for (Expense expense : expenses) {

            if (expense.getId() == id) {

                expense.setTitle(title);
                expense.setAmount(amount);
                expense.setCategory(category);
                expense.setDate(date);

                System.out.println("Expense updated successfully!");
                return;
            }
        }

        System.out.println("Expense not found.");
    }

    // Calculate total expenses
    public double getTotalExpenses() {

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        return total;
    }

    // Category-wise expense summary
    public void categorySummary() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        ArrayList<String> categories = new ArrayList<>();
        ArrayList<Double> totals = new ArrayList<>();

        for (Expense expense : expenses) {

            String category = expense.getCategory();
            double amount = expense.getAmount();

            int index = categories.indexOf(category);

            if (index == -1) {
                categories.add(category);
                totals.add(amount);
            } else {
                totals.set(index, totals.get(index) + amount);
            }
        }

        System.out.println("\n===== CATEGORY SUMMARY =====");

        for (int i = 0; i < categories.size(); i++) {
            System.out.println(
                    categories.get(i) + ": ₹" + totals.get(i)
            );
        }
    }

    // Save expenses to file
    public void saveExpenses() {

        try {

            FileWriter writer = new FileWriter("expenses.txt");

            for (Expense expense : expenses) {

                writer.write(
                        expense.getId() + "," +
                                expense.getTitle() + "," +
                                expense.getAmount() + "," +
                                expense.getCategory() + "," +
                                expense.getDate() + "\n"
                );
            }

            writer.close();

            System.out.println("Expenses saved successfully!");

        } catch (IOException e) {

            System.out.println("Error while saving expenses.");
        }
    }

    // Load expenses from file
    public void loadExpenses() {

        File file = new File("expenses.txt");

        if (!file.exists()) {
            return;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String title = data[1];
                double amount = Double.parseDouble(data[2]);
                String category = data[3];
                String date = data[4];

                Expense expense =
                        new Expense(id, title, amount, category, date);

                expenses.add(expense);
            }

            reader.close();

            System.out.println("Expenses loaded successfully!");

        } catch (IOException e) {

            System.out.println("Error while loading expenses.");
        }
    }
}