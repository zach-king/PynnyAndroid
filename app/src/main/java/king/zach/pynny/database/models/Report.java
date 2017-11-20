package king.zach.pynny.database.models;

import android.util.Pair;

import java.util.LinkedList;
import java.util.UUID;

import king.zach.pynny.utils.TimeUtil;

/**
 * Created by zachking on 11/13/17.
 */

public class Report {

    private static long sReportCount = 0;

    public long id;
    public String createdAt;

    public double totalExpenses;
    public double totalIncome;

    public LinkedList<Pair<String, Double>> expenseByWallet;
    public LinkedList<Pair<String, Double>> incomeByWallet;

    public LinkedList<Pair<String, Double>> spendingByCategory;

    public long transactionsCount;

    public Report() {
        this.id = sReportCount;
        this.createdAt = TimeUtil.getCurrentTimeString();
        this.expenseByWallet = new LinkedList<>();
        this.incomeByWallet = new LinkedList<>();
        this.spendingByCategory = new LinkedList<>();

        sReportCount++;
    }
}
