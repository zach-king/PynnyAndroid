package king.zach.pynny.database.models;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Zach on 9/29/2017.
 */

public class Wallet implements Serializable {

    private long id;
    private String name;
    private double balance;
    private String created_at;

    private static long NUM_WALLETS = 0;

    public Wallet(long id, String name, double balance, String created_at) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.created_at = created_at;
    }

    public Wallet(long id, String name, double start_balance) {
        this.id = id;
        this.name = name;
        this.balance = start_balance;
        this.created_at = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString();

        NUM_WALLETS++;
    }

    public Wallet(String name, double start_balance) {
        this.id = NUM_WALLETS;
        this.name = name;
        this.balance = start_balance;
        this.created_at = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString();

        NUM_WALLETS++;
    }

    public Wallet(String name) {
        this.id = NUM_WALLETS;
        this.name = name;
        this.balance = 0.0;
        this.created_at = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString();

        NUM_WALLETS++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static long getNumWallets() {
        return NUM_WALLETS;
    }

}
