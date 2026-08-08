public class Expense {

    private int id;
    private String title;
    private double amount;
    private String category;
    private String date;

    public Expense(int id, String title, double amount,
                   String category, String date) {

        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
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
        this.title = title;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
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