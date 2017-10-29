package king.zach.pynny.database.models;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Zach on 9/29/2017.
 */

public class Transaction implements Serializable {

    private long id;
    private double amount;
    private Category category;
    private String description;
    private String created_at;
    private Wallet wallet;

    public Transaction(long id, double amount, Category category, String description, String created_at, Wallet wallet) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.wallet = wallet;
        this.created_at = created_at;
    }

    public Transaction(double amount, Category category, String description, String created_at, Wallet wallet) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.created_at = created_at;
        this.wallet = wallet;
    }

    public Transaction(long id, double amount, Category category, String description, Wallet wallet) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.wallet = wallet;
        this.created_at = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

}
