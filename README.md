# Personal Expense Tracker

A desktop-based Expense Tracker application developed using Java Swing. It helps users manage daily expenses, track income, monitor monthly budgets, generate financial reports, and track savings goals through a graphical user interface.

## Features

### Expense Management
1. Add expenses
2. View all expenses
3. Search expenses by ID
4. Update expenses
5. Delete expenses
6. Calculate total expenses
7. Category-wise expense summary
8. Monthly expense reports
9. Filter expenses by category
10. Filter expenses by date range
11. Search expenses by title
12. Expense statistics

### Income Management
13. Add income
14. View income records
15. Search income by ID
16. Delete income
17. Update income
18. Calculate total income
19. Monthly income reports
20. Income statistics

### Financial Management
21. Calculate available balance
22. Monthly budget management
23. Budget alerts
24. Savings goal tracking

### Additional Features
25. Input validation and exception handling
26. Save and load records using text files, with duplicate ID prevention

## Technologies Used

- Java
- Java Swing
- Object-Oriented Programming (OOP)
- File Handling
- Java Collections Framework
- Java Time API
- IntelliJ IDEA
- JDK 17

## Project Structure

```text
ExpenseTracker/
│
├── src/
│   ├── Main.java
│   ├── ExpenseTrackerGUI.java
│   │
│   ├── model/
│   │   ├── Expense.java
│   │   └── Income.java
│   │
│   └── manager/
│       ├── ExpenseManager.java
│       ├── IncomeManager.java
│       ├── BudgetManager.java
│       └── SavingsGoalManager.java
│
├── expenses.txt
├── incomes.txt
├── budgets.txt
├── savings_goal.txt
├── README.md
└── .gitignore
```

## How to Run

1. Install JDK 17 or later.
2. Clone or download this repository.
3. Open the project in IntelliJ IDEA.
4. Allow IntelliJ to configure the JDK.
5. Open `ExpenseTrackerGUI.java`.
6. Run the `main()` method.
7. Start managing your income and expenses.

## Data Storage

The application uses local text files to store information:

- `expenses.txt` – Expense records
- `incomes.txt` – Income records
- `budgets.txt` – Monthly budget records
- `savings_goal.txt` – Savings target

Existing records are loaded when the application starts, and changes are saved to the corresponding files.

## Project Objective

The objective of this project is to develop a simple and user-friendly personal finance management application that helps users monitor their spending habits, manage income, control monthly expenses, and work towards financial goals.

## Future Enhancements

- Graphical charts for financial analysis
- Export reports to PDF or Excel
- User authentication
- Database integration using MySQL
- Advanced financial analytics

## Author

**Pillilla Vasavilatha**

B.Tech – Computer Science and Engineering  
Mohan Babu University

GitHub: https://github.com/pillillavasavilatha-wq
