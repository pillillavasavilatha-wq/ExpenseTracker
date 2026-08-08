import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();

        manager.loadExpenses();

        while (true) {

            System.out.println("\n================================");
            System.out.println("       EXPENSE TRACKER");
            System.out.println("================================");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Search Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Update Expense");
            System.out.println("6. Total Expenses");
            System.out.println("7. Category Summary");
            System.out.println("8. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a number from 1 to 8.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Expense ID: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("ID must be a number.");
                        scanner.nextLine();
                        break;
                    }

                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();

                    System.out.print("Enter Amount: ");

                    if (!scanner.hasNextDouble()) {
                        System.out.println("Amount must be a number.");
                        scanner.nextLine();
                        break;
                    }

                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    if (amount <= 0) {
                        System.out.println("Amount must be greater than 0.");
                        break;
                    }

                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine();

                    System.out.print("Enter Date: ");
                    String date = scanner.nextLine();

                    Expense expense =
                            new Expense(id, title, amount, category, date);

                    manager.addExpense(expense);
                    break;

                case 2:

                    manager.viewExpenses();
                    break;

                case 3:

                    System.out.print("Enter Expense ID: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("ID must be a number.");
                        scanner.nextLine();
                        break;
                    }

                    int searchId = scanner.nextInt();
                    scanner.nextLine();

                    manager.searchExpense(searchId);
                    break;

                case 4:

                    System.out.print("Enter Expense ID: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("ID must be a number.");
                        scanner.nextLine();
                        break;
                    }

                    int deleteId = scanner.nextInt();
                    scanner.nextLine();

                    manager.deleteExpense(deleteId);
                    break;

                case 5:

                    System.out.print("Enter Expense ID to update: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("ID must be a number.");
                        scanner.nextLine();
                        break;
                    }

                    int updateId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter new Title: ");
                    String newTitle = scanner.nextLine();

                    System.out.print("Enter new Amount: ");

                    if (!scanner.hasNextDouble()) {
                        System.out.println("Amount must be a number.");
                        scanner.nextLine();
                        break;
                    }

                    double newAmount = scanner.nextDouble();
                    scanner.nextLine();

                    if (newAmount <= 0) {
                        System.out.println("Amount must be greater than 0.");
                        break;
                    }

                    System.out.print("Enter new Category: ");
                    String newCategory = scanner.nextLine();

                    System.out.print("Enter new Date: ");
                    String newDate = scanner.nextLine();

                    manager.updateExpense(
                            updateId,
                            newTitle,
                            newAmount,
                            newCategory,
                            newDate
                    );

                    break;

                case 6:

                    double total = manager.getTotalExpenses();

                    System.out.println("Total Expenses: ₹" + total);
                    break;

                case 7:

                    manager.categorySummary();
                    break;

                case 8:

                    manager.saveExpenses();

                    System.out.println(
                            "Thank you for using Expense Tracker!"
                    );

                    scanner.close();
                    return;

                default:

                    System.out.println(
                            "Invalid choice. Please choose 1 to 8."
                    );
            }
        }
    }
}