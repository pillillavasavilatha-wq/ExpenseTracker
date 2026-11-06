
package manager;

import model.Income;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class IncomeManager {

    private final ArrayList<Income> incomes = new ArrayList<>();
    private final String FILE_NAME = "incomes.txt";

    public IncomeManager() {
        loadIncomes();
    }

    // 1. Add Income
    public void addIncome(Income income) {
        if (searchIncome(income.getId()) != null) {
            System.out.println("Income ID already exists.");
            return;
        }

        incomes.add(income);
        System.out.println("Income added successfully.");
    }

    // 2. View Incomes
    public void viewIncomes() {
        if (incomes.isEmpty()) {
            System.out.println("No income records found.");
            return;
        }

        System.out.println("\n---------- INCOME DETAILS ----------");

        for (Income income : incomes) {
            System.out.println("ID: " + income.getId());
            System.out.println("Source: " + income.getSource());
            System.out.println("Amount: Rs. " + income.getAmount());
            System.out.println("Date: " + income.getDate());
            System.out.println("------------------------------------");
        }
    }

    // 3. Search Income by ID
    public Income searchIncome(int id) {
        for (Income income : incomes) {
            if (income.getId() == id) {
                return income;
            }
        }

        return null;
    }

    // 4. Delete Income
    public void deleteIncome(int id) {
        Income income = searchIncome(id);

        if (income == null) {
            System.out.println("Income not found.");
            return;
        }

        incomes.remove(income);
        System.out.println("Income deleted successfully.");
    }

    // 5. Calculate Total Income
    public double getTotalIncome() {
        double total = 0;

        for (Income income : incomes) {
            total += income.getAmount();
        }

        return total;
    }

    // 6. Save Income Records
    public void saveIncomes() {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("V1");

        for (Income income : incomes) {
            String encodedSource = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(
                            income.getSource().getBytes(StandardCharsets.UTF_8)
                    );

            String encodedDate = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(
                            income.getDate().getBytes(StandardCharsets.UTF_8)
                    );

            String line = "V1|"
                    + income.getId() + "|"
                    + encodedSource + "|"
                    + income.getAmount() + "|"
                    + encodedDate;

            lines.add(line);
        }

        try {
            Files.write(
                    Path.of(FILE_NAME),
                    lines,
                    StandardCharsets.UTF_8
            );

        } catch (IOException e) {
            System.out.println("Error saving income records: " + e.getMessage());
        }
    }

    // 7. Load Income Records
    public void loadIncomes() {
        incomes.clear();

        Path path = Path.of(FILE_NAME);

        if (!Files.exists(path)) {
            return;
        }

        try {
            ArrayList<String> lines = new ArrayList<>(
                    Files.readAllLines(path, StandardCharsets.UTF_8)
            );

            for (String line : lines) {
                if (line == null || line.trim().isEmpty()
                        || line.trim().equals("V1")) {
                    continue;
                }

                try {
                    String[] data = line.split("\\|", -1);

                    if (data.length == 5 && data[0].equals("V1")) {
                        int id = Integer.parseInt(data[1]);

                        String source = new String(
                                Base64.getUrlDecoder().decode(data[2]),
                                StandardCharsets.UTF_8
                        );

                        double amount = Double.parseDouble(data[3]);

                        String date = new String(
                                Base64.getUrlDecoder().decode(data[4]),
                                StandardCharsets.UTF_8
                        );

                        if (searchIncome(id) == null) {
                            incomes.add(new Income(id, source, amount, date));
                        }

                    } else {
                        // Support older comma-separated records.
                        String[] oldData = line.split(",", -1);

                        if (oldData.length == 4) {
                            int id = Integer.parseInt(oldData[0].trim());
                            String source = oldData[1].trim();
                            double amount = Double.parseDouble(oldData[2].trim());
                            String date = oldData[3].trim();

                            if (searchIncome(id) == null) {
                                incomes.add(
                                        new Income(id, source, amount, date)
                                );
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Skipping invalid income record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading income records: " + e.getMessage());
        }
    }

    // 8. Update Income
    public void updateIncome(int id, String source, double amount, String date) {
        Income income = searchIncome(id);

        if (income == null) {
            System.out.println("Income not found.");
            return;
        }

        income.setSource(source);
        income.setAmount(amount);
        income.setDate(date);

        System.out.println("Income updated successfully.");
    }

    // 9. Monthly Income Report
    public void showMonthlyIncomeReport(String month) {
        double total = 0;
        boolean found = false;

        System.out.println("\n---------- MONTHLY INCOME REPORT ----------");
        System.out.println("Month: " + month);

        try {
            YearMonth selectedMonth = YearMonth.parse(month);

            for (Income income : incomes) {
                LocalDate date = LocalDate.parse(income.getDate());

                if (YearMonth.from(date).equals(selectedMonth)) {
                    System.out.println(
                            income.getSource() + " - Rs. " + income.getAmount()
                    );

                    total += income.getAmount();
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No income records found for this month.");
            }

            System.out.println("Total Monthly Income: Rs. " + total);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid month. Use YYYY-MM format.");
        }
    }

    // 10. Income Statistics Dashboard
    public void showIncomeStatistics() {
        if (incomes.isEmpty()) {
            System.out.println("No income records available.");
            return;
        }

        double total = getTotalIncome();
        double highest = incomes.get(0).getAmount();
        double lowest = incomes.get(0).getAmount();

        Map<String, Double> sourceTotals = new LinkedHashMap<>();

        for (Income income : incomes) {
            double amount = income.getAmount();

            if (amount > highest) {
                highest = amount;
            }

            if (amount < lowest) {
                lowest = amount;
            }

            sourceTotals.put(
                    income.getSource(),
                    sourceTotals.getOrDefault(income.getSource(), 0.0) + amount
            );
        }

        double average = total / incomes.size();

        System.out.println("\n---------- INCOME STATISTICS ----------");
        System.out.println("Number of Income Records: " + incomes.size());
        System.out.println("Total Income: Rs. " + total);
        System.out.println("Highest Income: Rs. " + highest);
        System.out.println("Lowest Income: Rs. " + lowest);
        System.out.println("Average Income: Rs. " + average);

        System.out.println("\nSource-wise Income:");

        for (Map.Entry<String, Double> entry : sourceTotals.entrySet()) {
            System.out.println(
                    entry.getKey() + ": Rs. " + entry.getValue()
            );
        }
    }

    // 11. Get All Incomes for GUI
    public ArrayList<Income> getAllIncomes() {
        return new ArrayList<>(incomes);
    }
}
