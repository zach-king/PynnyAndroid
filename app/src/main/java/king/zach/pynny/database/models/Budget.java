package king.zach.pynny.database.models;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Zach on 9/30/2017.
 */

public class Budget implements Serializable {

    private long id;
    private double goal;
    private double balance;
    private String month;
    private Category category;
    private Wallet wallet;

    public Budget(long id, double goal, double balance, Category category, Wallet wallet, String month) {
        this.id = id;
        this.goal = goal;
        this.balance = balance;
        this.category = category;
        this.wallet = wallet;
        this.month = month;
    }

    public Budget(long id, double goal, double balance, Category category, Wallet wallet) {
        this.id = id;
        this.goal = goal;
        this.balance = balance;
        this.category = category;
        this.wallet = wallet;
        this.month = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString();
    }

    public Budget(long id, double goal, double balance, Category category, String month) {
        this.id = id;
        this.goal = goal;
        this.balance = balance;
        this.category = category;
        this.wallet = null;
        this.month = month;
    }

    public Budget(long id, double goal, double balance, Category category) {
        this.id = id;
        this.goal = goal;
        this.balance = balance;
        this.category = category;
        this.wallet = null;
        this.month = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString();
    }

    public Budget(long id, double goal, Category category, String month) {
        this.id = id;
        this.goal = goal;
        this.balance = 0.0;
        this.category = category;
        this.wallet = null;
        this.month = month;
    }

    public Budget(long id, double goal, Category category) {
        this.id = id;
        this.goal = goal;
        this.balance = 0.0;
        this.category = category;
        this.wallet = null;
        this.month = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

}
