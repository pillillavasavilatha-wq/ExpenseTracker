
import manager.ExpenseManager;
import manager.IncomeManager;
import manager.BudgetManager;
import manager.SavingsGoalManager;
import model.Expense;
import model.Income;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExpenseTrackerGUI extends JFrame {

    private final ExpenseManager expenseManager = new ExpenseManager();
    private final IncomeManager incomeManager = new IncomeManager();
    private final BudgetManager budgetManager = new BudgetManager();
    private final SavingsGoalManager savingsGoalManager = new SavingsGoalManager();

    private JPanel mainPanel;

    private final Color backgroundColor = new Color(245, 247, 250);
    private final Color sidebarColor = new Color(28, 39, 58);

    public ExpenseTrackerGUI() {
        setTitle("Personal Expense Tracker");
        setSize(1150, 720);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createSidebar();
        showDashboard();
    }

    // SIDEBAR

    private void createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(sidebarColor);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 12, 20, 12));

        JLabel heading = new JLabel("EXPENSE TRACKER");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 17));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(heading);
        sidebar.add(Box.createVerticalStrut(30));

        addSidebarButton(sidebar, "Dashboard", this::showDashboard);
        addSidebarButton(sidebar, "Add Expense", this::addExpenseDialog);
        addSidebarButton(sidebar, "View Expenses", this::showExpenses);
        addSidebarButton(sidebar, "Add Income", this::addIncomeDialog);
        addSidebarButton(sidebar, "View Income", this::showIncomes);

        sidebar.add(Box.createVerticalStrut(15));

        addSidebarButton(sidebar, "Search Expense", this::searchExpenseDialog);
        addSidebarButton(sidebar, "Search Income", this::searchIncomeDialog);
        addSidebarButton(sidebar, "Filter Expenses", this::filterExpensesDialog);
        addSidebarButton(sidebar, "Expense Reports", this::expenseReportsDialog);
        addSidebarButton(sidebar, "Income Reports", this::incomeReportsDialog);

        sidebar.add(Box.createVerticalStrut(15));

        addSidebarButton(sidebar, "Monthly Budget", this::budgetDialog);
        addSidebarButton(sidebar, "Savings Goal", this::savingsDialog);
        addSidebarButton(sidebar, "Statistics", this::statisticsDialog);

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("Personal Finance Manager");
        footer.setForeground(new Color(190, 200, 215));
        footer.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);

        add(sidebar, BorderLayout.WEST);
    }

    private void addSidebarButton(JPanel sidebar, String title, Runnable action) {
        JButton button = new JButton(title);
        button.setMaximumSize(new Dimension(195, 42));
        button.setPreferredSize(new Dimension(195, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(sidebarColor);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> action.run());

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(5));
    }

    // MAIN PANEL

    private void setMainPanel(JPanel panel) {
        if (mainPanel != null) {
            remove(mainPanel);
        }

        mainPanel = panel;
        add(mainPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createPage(String title) {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(new Color(35, 45, 60));

        panel.add(heading, BorderLayout.NORTH);

        return panel;
    }

    // DASHBOARD

    private void showDashboard() {
        JPanel panel = createPage("Dashboard");

        JPanel content = new JPanel(new GridLayout(0, 2, 18, 18));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(25, 0, 0, 0));

        double totalIncome = incomeManager.getTotalIncome();
        double totalExpenses = expenseManager.getTotalExpenses();
        double balance = totalIncome - totalExpenses;

        double currentSavings = savingsGoalManager.getCurrentSavings(
                totalIncome, totalExpenses
        );

        content.add(createSummaryCard(
                "Total Income", totalIncome, new Color(34, 145, 95)));

        content.add(createSummaryCard(
                "Total Expenses", totalExpenses, new Color(220, 75, 75)));

        content.add(createSummaryCard(
                "Current Balance", balance, new Color(52, 120, 246)));

        content.add(createSummaryCard(
                "Current Savings", currentSavings, new Color(130, 90, 190)));

        String month = YearMonth.now().toString();
        Double monthlyBudget = budgetManager.getBudget(month);

        String budgetText = monthlyBudget == null
                ? "Not set"
                : String.format("%.2f", monthlyBudget);

        content.add(createTextCard(
                "This Month's Budget", budgetText, new Color(210, 145, 45)));

        JPanel quickActions = new JPanel(new GridLayout(1, 3, 10, 10));
        quickActions.setOpaque(false);

        JButton addExpense = new JButton("Add Expense");
        JButton addIncome = new JButton("Add Income");
        JButton reports = new JButton("View Reports");

        addExpense.addActionListener(e -> addExpenseDialog());
        addIncome.addActionListener(e -> addIncomeDialog());
        reports.addActionListener(e -> expenseReportsDialog());

        quickActions.add(addExpense);
        quickActions.add(addIncome);
        quickActions.add(reports);

        JPanel center = new JPanel(new BorderLayout(15, 20));
        center.setOpaque(false);
        center.add(content, BorderLayout.CENTER);
        center.add(quickActions, BorderLayout.SOUTH);

        panel.add(center, BorderLayout.CENTER);

        setMainPanel(panel);
    }

    private JPanel createSummaryCard(String title, double value, Color color) {
        return createTextCard(title, String.format("%.2f", value), color);
    }

    private JPanel createTextCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, color),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Arial", Font.PLAIN, 15));
        heading.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 23));
        valueLabel.setForeground(new Color(35, 45, 60));

        card.add(heading, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void refreshDashboard() {
        showDashboard();
    }

    // INPUT HELPERS

    private String askText(String message, String initialValue) {
        return (String) JOptionPane.showInputDialog(
                this, message, "Expense Tracker",
                JOptionPane.PLAIN_MESSAGE, null, null, initialValue
        );
    }

    private String askText(String message) {
        return askText(message, "");
    }

    private Integer askId() {
        String value = askText("Enter ID:");

        if (value == null) return null;

        try {
            int id = Integer.parseInt(value.trim());

            if (id <= 0) throw new NumberFormatException();

            return id;
        } catch (NumberFormatException e) {
            showError("Enter a valid positive ID.");
            return null;
        }
    }

    private Double askAmount(String message) {
        String value = askText(message);

        if (value == null) return null;

        try {
            double amount = Double.parseDouble(value.trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                throw new NumberFormatException();
            }

            return amount;
        } catch (NumberFormatException e) {
            showError("Enter a valid positive amount.");
            return null;
        }
    }

    private String askDate() {
        String date = askText(
                "Enter date (yyyy-MM-dd):",
                LocalDate.now().toString()
        );

        if (date == null) return null;

        try {
            LocalDate.parse(date.trim());
            return date.trim();
        } catch (DateTimeParseException e) {
            showError("Invalid date. Use yyyy-MM-dd.");
            return null;
        }
    }

    private String askMonth() {
        String month = askText(
                "Enter month (yyyy-MM):",
                YearMonth.now().toString()
        );

        if (month == null) return null;

        try {
            YearMonth.parse(month.trim());
            return month.trim();
        } catch (DateTimeParseException e) {
            showError("Invalid month. Use yyyy-MM.");
            return null;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this, message, "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(
                this, message, "Expense Tracker",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private boolean confirmDelete() {
        return JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }

    // EXPENSE CRUD

    private int generateExpenseId() {
        int max = 0;

        for (Expense expense : expenseManager.getAllExpenses()) {
            max = Math.max(max, expense.getId());
        }

        return max + 1;
    }

    private void addExpenseDialog() {
        String title = askText("Expense title:");

        if (title == null || title.trim().isEmpty()) return;

        Double amount = askAmount("Expense amount:");
        if (amount == null) return;

        String category = askText("Expense category:");
        if (category == null || category.trim().isEmpty()) return;

        String date = askDate();
        if (date == null) return;

        int id = generateExpenseId();

        expenseManager.addExpense(new Expense(
                id, title.trim(), amount, category.trim(), date
        ));

        expenseManager.saveExpenses();

        showMessage("Expense added successfully.\nExpense ID: " + id);
        refreshDashboard();
    }

    private void showExpenses() {
        showExpenseTable(expenseManager.getAllExpenses(), "All Expenses");
    }

    private void showExpenseTable(ArrayList<Expense> list, String title) {
        String[] columns = {"ID", "Title", "Amount", "Category", "Date"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Expense expense : list) {
            model.addRow(new Object[]{
                    expense.getId(),
                    expense.getTitle(),
                    String.format("%.2f", expense.getAmount()),
                    expense.getCategory(),
                    expense.getDate()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 350));

        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton closeButton = new JButton("Close");

        JPanel buttons = new JPanel();
        buttons.add(editButton);
        buttons.add(deleteButton);
        buttons.add(closeButton);

        JDialog dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row < 0) {
                showError("Select an expense first.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            dialog.dispose();
            editExpense(id);
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row < 0) {
                showError("Select an expense first.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);

            if (confirmDelete()) {
                expenseManager.deleteExpense(id);
                expenseManager.saveExpenses();
                dialog.dispose();
                refreshDashboard();
                showExpenses();
            }
        });

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editExpense(int id) {
        Expense expense = expenseManager.searchExpense(id);

        if (expense == null) {
            showError("Expense not found.");
            return;
        }

        String title = askText("Expense title:", expense.getTitle());
        if (title == null || title.trim().isEmpty()) return;

        String amountText = askText(
                "Expense amount:", String.valueOf(expense.getAmount())
        );

        if (amountText == null) return;

        double amount;

        try {
            amount = Double.parseDouble(amountText.trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showError("Enter a valid positive amount.");
            return;
        }

        String category = askText("Category:", expense.getCategory());
        if (category == null || category.trim().isEmpty()) return;

        String date = askText("Date (yyyy-MM-dd):", expense.getDate());
        if (date == null) return;

        try {
            LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            showError("Invalid date.");
            return;
        }

        expenseManager.updateExpense(
                id, title.trim(), amount, category.trim(), date.trim()
        );

        expenseManager.saveExpenses();

        showMessage("Expense updated successfully.");
        refreshDashboard();
    }

    // INCOME CRUD

    private int generateIncomeId() {
        int max = 0;

        for (Income income : incomeManager.getAllIncomes()) {
            max = Math.max(max, income.getId());
        }

        return max + 1;
    }

    private void addIncomeDialog() {
        String source = askText("Income source:");

        if (source == null || source.trim().isEmpty()) return;

        Double amount = askAmount("Income amount:");
        if (amount == null) return;

        String date = askDate();
        if (date == null) return;

        int id = generateIncomeId();

        incomeManager.addIncome(new Income(
                id, source.trim(), amount, date
        ));

        incomeManager.saveIncomes();

        showMessage("Income added successfully.\nIncome ID: " + id);
        refreshDashboard();
    }

    private void showIncomes() {
        showIncomeTable(incomeManager.getAllIncomes());
    }

    private void showIncomeTable(ArrayList<Income> list) {
        String[] columns = {"ID", "Source", "Amount", "Date"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Income income : list) {
            model.addRow(new Object[]{
                    income.getId(),
                    income.getSource(),
                    String.format("%.2f", income.getAmount()),
                    income.getDate()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 350));

        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton closeButton = new JButton("Close");

        JPanel buttons = new JPanel();
        buttons.add(editButton);
        buttons.add(deleteButton);
        buttons.add(closeButton);

        JDialog dialog = new JDialog(this, "Income Records", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row < 0) {
                showError("Select an income record first.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            dialog.dispose();
            editIncome(id);
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row < 0) {
                showError("Select an income record first.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);

            if (confirmDelete()) {
                incomeManager.deleteIncome(id);
                incomeManager.saveIncomes();
                dialog.dispose();
                refreshDashboard();
                showIncomes();
            }
        });

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editIncome(int id) {
        Income income = incomeManager.searchIncome(id);

        if (income == null) {
            showError("Income not found.");
            return;
        }

        String source = askText("Income source:", income.getSource());
        if (source == null || source.trim().isEmpty()) return;

        String amountText = askText(
                "Income amount:", String.valueOf(income.getAmount())
        );

        if (amountText == null) return;

        double amount;

        try {
            amount = Double.parseDouble(amountText.trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showError("Enter a valid positive amount.");
            return;
        }

        String date = askText("Date (yyyy-MM-dd):", income.getDate());
        if (date == null) return;

        try {
            LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            showError("Invalid date.");
            return;
        }

        incomeManager.updateIncome(id, source.trim(), amount, date.trim());
        incomeManager.saveIncomes();

        showMessage("Income updated successfully.");
        refreshDashboard();
    }

    // SEARCH

    private void searchExpenseDialog() {
        String[] options = {"Search by ID", "Search by Title"};

        int choice = JOptionPane.showOptionDialog(
                this, "Choose search method", "Search Expense",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        if (choice == 0) {
            Integer id = askId();
            if (id == null) return;

            Expense expense = expenseManager.searchExpense(id);

            if (expense == null) {
                showMessage("Expense not found.");
            } else {
                ArrayList<Expense> results = new ArrayList<>();
                results.add(expense);
                showExpenseTable(results, "Search Result");
            }

        } else if (choice == 1) {
            String title = askText("Enter expense title:");

            if (title == null || title.trim().isEmpty()) return;

            ArrayList<Expense> results = new ArrayList<>();

            for (Expense expense : expenseManager.getAllExpenses()) {
                if (expense.getTitle().toLowerCase()
                        .contains(title.trim().toLowerCase())) {
                    results.add(expense);
                }
            }

            if (results.isEmpty()) {
                showMessage("No matching expenses found.");
            } else {
                showExpenseTable(results, "Title Search Results");
            }
        }
    }

    private void searchIncomeDialog() {
        Integer id = askId();
        if (id == null) return;

        Income income = incomeManager.searchIncome(id);

        if (income == null) {
            showMessage("Income not found.");
        } else {
            ArrayList<Income> results = new ArrayList<>();
            results.add(income);
            showIncomeTable(results);
        }
    }

    // FILTERS

    private void filterExpensesDialog() {
        String[] options = {
                "Filter by Category",
                "Filter by Date Range"
        };

        int choice = JOptionPane.showOptionDialog(
                this, "Select filter", "Filter Expenses",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        if (choice == 0) {
            String category = askText("Enter category:");

            if (category == null || category.trim().isEmpty()) return;

            ArrayList<Expense> results = new ArrayList<>();

            for (Expense expense : expenseManager.getAllExpenses()) {
                if (expense.getCategory().equalsIgnoreCase(category.trim())) {
                    results.add(expense);
                }
            }

            if (results.isEmpty()) {
                showMessage("No expenses found in this category.");
            } else {
                showExpenseTable(results, "Category Filter Results");
            }

        } else if (choice == 1) {
            String startDate = askText("Start date (yyyy-MM-dd):");
            if (startDate == null) return;

            String endDate = askText("End date (yyyy-MM-dd):");
            if (endDate == null) return;

            try {
                LocalDate start = LocalDate.parse(startDate.trim());
                LocalDate end = LocalDate.parse(endDate.trim());

                if (start.isAfter(end)) {
                    showError("Start date cannot be after end date.");
                    return;
                }

                ArrayList<Expense> results = new ArrayList<>();

                for (Expense expense : expenseManager.getAllExpenses()) {
                    LocalDate date = LocalDate.parse(expense.getDate());

                    if (!date.isBefore(start) && !date.isAfter(end)) {
                        results.add(expense);
                    }
                }

                if (results.isEmpty()) {
                    showMessage("No expenses found in this date range.");
                } else {
                    showExpenseTable(results, "Date Range Results");
                }

            } catch (DateTimeParseException e) {
                showError("Enter dates in yyyy-MM-dd format.");
            }
        }
    }

    // EXPENSE REPORTS

    private void expenseReportsDialog() {
        String[] options = {
                "Monthly Expense Report",
                "Category-wise Summary",
                "Total Expenses",
                "Expense Statistics"
        };

        int choice = JOptionPane.showOptionDialog(
                this, "Select expense report", "Expense Reports",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        if (choice == 0) {
            String month = askMonth();
            if (month == null) return;

            ArrayList<Expense> results = new ArrayList<>();

            for (Expense expense : expenseManager.getAllExpenses()) {
                if (expense.getDate().startsWith(month)) {
                    results.add(expense);
                }
            }

            double total = expenseManager.getMonthlyExpenses(month);

            showExpenseTable(results, "Monthly Report: " + month);
            showMessage("Total expenses for " + month + ": "
                    + String.format("%.2f", total));

        } else if (choice == 1) {
            Map<String, Double> summary = new LinkedHashMap<>();

            for (Expense expense : expenseManager.getAllExpenses()) {
                summary.merge(
                        expense.getCategory(),
                        expense.getAmount(),
                        Double::sum
                );
            }

            StringBuilder result = new StringBuilder(
                    "CATEGORY-WISE EXPENSE SUMMARY\n\n"
            );

            for (Map.Entry<String, Double> entry : summary.entrySet()) {
                result.append(entry.getKey())
                        .append(" : ")
                        .append(String.format("%.2f", entry.getValue()))
                        .append("\n");
            }

            if (summary.isEmpty()) {
                result.append("No expenses available.");
            }

            showMessage(result.toString());

        } else if (choice == 2) {
            showMessage("Total Expenses: "
                    + String.format("%.2f", expenseManager.getTotalExpenses()));

        } else if (choice == 3) {
            showExpenseStatistics();
        }
    }

    private void showExpenseStatistics() {
        ArrayList<Expense> list = expenseManager.getAllExpenses();

        if (list.isEmpty()) {
            showMessage("No expense data available.");
            return;
        }

        double total = expenseManager.getTotalExpenses();

        Expense highest = list.get(0);
        Expense lowest = list.get(0);

        for (Expense expense : list) {
            if (expense.getAmount() > highest.getAmount()) {
                highest = expense;
            }

            if (expense.getAmount() < lowest.getAmount()) {
                lowest = expense;
            }
        }

        String result = "EXPENSE STATISTICS\n\n"
                + "Total expenses: " + String.format("%.2f", total) + "\n"
                + "Number of transactions: " + list.size() + "\n"
                + "Average expense: "
                + String.format("%.2f", total / list.size()) + "\n"
                + "Highest expense: " + highest.getTitle()
                + " (" + String.format("%.2f", highest.getAmount()) + ")\n"
                + "Lowest expense: " + lowest.getTitle()
                + " (" + String.format("%.2f", lowest.getAmount()) + ")";

        showMessage(result);
    }

    // INCOME REPORTS

    private void incomeReportsDialog() {
        String[] options = {
                "Monthly Income Report",
                "Total Income",
                "Income Statistics"
        };

        int choice = JOptionPane.showOptionDialog(
                this, "Select income report", "Income Reports",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        if (choice == 0) {
            String month = askMonth();
            if (month == null) return;

            ArrayList<Income> results = new ArrayList<>();
            double total = 0;

            for (Income income : incomeManager.getAllIncomes()) {
                if (income.getDate().startsWith(month)) {
                    results.add(income);
                    total += income.getAmount();
                }
            }

            showIncomeTable(results);
            showMessage("Total income for " + month + ": "
                    + String.format("%.2f", total));

        } else if (choice == 1) {
            showMessage("Total Income: "
                    + String.format("%.2f", incomeManager.getTotalIncome()));

        } else if (choice == 2) {
            showIncomeStatistics();
        }
    }

    private void showIncomeStatistics() {
        ArrayList<Income> list = incomeManager.getAllIncomes();

        if (list.isEmpty()) {
            showMessage("No income data available.");
            return;
        }

        double total = incomeManager.getTotalIncome();

        Income highest = list.get(0);
        Income lowest = list.get(0);

        for (Income income : list) {
            if (income.getAmount() > highest.getAmount()) {
                highest = income;
            }

            if (income.getAmount() < lowest.getAmount()) {
                lowest = income;
            }
        }

        String result = "INCOME STATISTICS\n\n"
                + "Total income: " + String.format("%.2f", total) + "\n"
                + "Number of transactions: " + list.size() + "\n"
                + "Average income: "
                + String.format("%.2f", total / list.size()) + "\n"
                + "Highest income: " + highest.getSource()
                + " (" + String.format("%.2f", highest.getAmount()) + ")\n"
                + "Lowest income: " + lowest.getSource()
                + " (" + String.format("%.2f", lowest.getAmount()) + ")";

        showMessage(result);
    }

    // BUDGET

    private void budgetDialog() {
        String[] options = {
                "Set Monthly Budget",
                "View Monthly Budget",
                "View All Budgets"
        };

        int choice = JOptionPane.showOptionDialog(
                this, "Choose a budget operation", "Monthly Budget",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        if (choice == 0) {
            String month = askMonth();
            if (month == null) return;

            Double amount = askAmount("Enter monthly budget:");
            if (amount == null) return;

            budgetManager.setBudget(month, amount);

            double spent = expenseManager.getMonthlyExpenses(month);

            if (spent >= amount) {
                showMessage("Budget saved.\nWarning: Your expenses have reached or exceeded this budget.");
            } else {
                showMessage("Monthly budget saved successfully.");
            }

            refreshDashboard();

        } else if (choice == 1) {
            String month = askMonth();
            if (month == null) return;

            Double budget = budgetManager.getBudget(month);

            if (budget == null) {
                showMessage("No budget set for " + month);
                return;
            }

            double spent = expenseManager.getMonthlyExpenses(month);
            double remaining = budget - spent;
            double percentage = budgetManager.getUsagePercentage(
                    month, expenseManager
            );

            String message = "BUDGET REPORT: " + month + "\n\n"
                    + "Budget: " + String.format("%.2f", budget) + "\n"
                    + "Spent: " + String.format("%.2f", spent) + "\n"
                    + "Remaining: " + String.format("%.2f", remaining) + "\n"
                    + "Used: " + String.format("%.2f", percentage) + "%";

            if (remaining < 0) {
                message += "\n\nALERT: Budget exceeded!";
            } else if (percentage >= 80) {
                message += "\n\nWARNING: You have used 80% or more of your budget.";
            }

            showMessage(message);

        } else if (choice == 2) {
            Map<String, Double> budgets = budgetManager.getAllBudgets();

            if (budgets.isEmpty()) {
                showMessage("No budgets available.");
                return;
            }

            StringBuilder result = new StringBuilder("ALL MONTHLY BUDGETS\n\n");

            for (Map.Entry<String, Double> entry : budgets.entrySet()) {
                result.append(entry.getKey())
                        .append(" : ")
                        .append(String.format("%.2f", entry.getValue()))
                        .append("\n");
            }

            showMessage(result.toString());
        }
    }

    // SAVINGS GOAL

    private void savingsDialog() {
        String[] options = {
                "Set Savings Goal",
                "View Savings Progress"
        };

        int choice = JOptionPane.showOptionDialog(
                this, "Choose savings operation", "Savings Goal Tracker",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        if (choice == 0) {
            Double target = askAmount("Enter your savings target:");
            if (target == null) return;

            savingsGoalManager.setSavingsGoal(target);

            showMessage("Savings goal saved successfully.");
            refreshDashboard();

        } else if (choice == 1) {
            double currentSavings = savingsGoalManager.getCurrentSavings(
                    incomeManager.getTotalIncome(),
                    expenseManager.getTotalExpenses()
            );

            double target = savingsGoalManager.getSavingsGoal();

            if (target <= 0) {
                showMessage("Please set a savings goal first.");
                return;
            }

            double progress = savingsGoalManager.getProgressPercentage(
                    currentSavings
            );

            double remaining = savingsGoalManager.getRemainingAmount(
                    currentSavings
            );

            String message = "SAVINGS GOAL PROGRESS\n\n"
                    + "Target: " + String.format("%.2f", target) + "\n"
                    + "Current savings: " + String.format("%.2f", currentSavings) + "\n"
                    + "Progress: " + String.format("%.2f", progress) + "%\n"
                    + "Remaining: " + String.format("%.2f", remaining);

            if (currentSavings >= target) {
                message += "\n\nCongratulations! Goal reached.";
            }

            showMessage(message);
        }
    }

    // COMBINED STATISTICS

    private void statisticsDialog() {
        String message = "FINANCIAL OVERVIEW\n\n"
                + "Total Income: "
                + String.format("%.2f", incomeManager.getTotalIncome()) + "\n"
                + "Total Expenses: "
                + String.format("%.2f", expenseManager.getTotalExpenses()) + "\n"
                + "Balance: "
                + String.format("%.2f",
                incomeManager.getTotalIncome() - expenseManager.getTotalExpenses());

        showMessage(message);

        String[] options = {
                "Expense Statistics",
                "Income Statistics"
        };

        int choice = JOptionPane.showOptionDialog(
                this, "View detailed statistics", "Statistics",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        if (choice == 0) {
            showExpenseStatistics();
        } else if (choice == 1) {
            showIncomeStatistics();
        }
    }

    // MAIN

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseTrackerGUI gui = new ExpenseTrackerGUI();
            gui.setVisible(true);
        });
    }
}
