package king.zach.pynny.activities.reporting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import king.zach.pynny.R;
import king.zach.pynny.database.models.Report;
import king.zach.pynny.utils.ReportingUtil;

public class ReportingActivity extends AppCompatActivity {

    private static String TAG = "ReportingActivity";

    private ReportingUtil reportingUtil;
    private Report report;

    private TextView tvTotalExpenses;
    private TextView tvTotalIncome;
    private TextView tvExpenseByWallet;
    private TextView tvIncomeByWallet;
    private TextView tvReportDate;
    private TextView tvSpendingByCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);

        if (reportingUtil == null) {
            reportingUtil = ReportingUtil.getInstance(this.getApplicationContext());
        }

        if (tvTotalExpenses == null) {
            tvTotalExpenses = (TextView) findViewById(R.id.tvTotalExpenses);
        }

        if (tvTotalIncome == null) {
            tvTotalIncome = (TextView) findViewById(R.id.tvTotalIncome);
        }

        if (tvExpenseByWallet == null) {
            tvExpenseByWallet = (TextView) findViewById(R.id.tvExpenseByWallet);
        }

        if (tvIncomeByWallet == null) {
            tvIncomeByWallet = (TextView) findViewById(R.id.tvIncomeByWallet);
        }

        if (tvReportDate == null) {
            tvReportDate = (TextView) findViewById(R.id.tvReportDate);
        }

        if (tvSpendingByCategory == null) {
            tvSpendingByCategory = (TextView) findViewById(R.id.tvSpendingByCategory);
        }


        // Get the report
        report = reportingUtil.generateReport();

        // Set widget values
        showReport();

        ((Button) findViewById(R.id.btnReportExport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportingUtil.saveReport(report);
            }
        });
    }

    private void showReport() {
        tvTotalExpenses.setText(getResources().getString(R.string.total_expenses, report.totalExpenses));
        tvTotalIncome.setText(getResources().getString(R.string.total_income, report.totalIncome));
        tvReportDate.setText(report.createdAt);

        String walletExpensesString = "Expense Per Wallet:\n";
        Log.d(TAG, "Wallet expense pair count: " + report.expenseByWallet.size());
        for (Pair<String, Double> pair : report.expenseByWallet) {
            walletExpensesString += pair.first + " - " + String.format(Locale.US, "$%.2f\n", pair.second);
        }

        String walletIncomeString = "Income Per Wallet:\n";
        for (Pair<String, Double> pair : report.incomeByWallet) {
            walletIncomeString += pair.first + " - " + String.format(Locale.US, "$%.2f\n", pair.second);
        }

        String categorySpendingString = "Spending Per Category:\n";
        for (Pair<String, Double> pair : report.spendingByCategory) {
            categorySpendingString += pair.first + " - " + String.format(Locale.US, "$%.2f\n", pair.second);
        }

        tvExpenseByWallet.setText(walletExpensesString);
        tvIncomeByWallet.setText(walletIncomeString);
        tvSpendingByCategory.setText(categorySpendingString);
    }
}
