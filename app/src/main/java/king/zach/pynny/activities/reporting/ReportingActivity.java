package king.zach.pynny.activities.reporting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import king.zach.pynny.R;
import king.zach.pynny.database.models.Report;
import king.zach.pynny.utils.ReportingUtil;

public class ReportingActivity extends AppCompatActivity {

    private static String TAG = "ReportingActivity";

    private ReportingUtil reportingUtil;
    private Report report;

    private TextView tvTotalExpenses;
    private TextView tvTotalIncome;

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


        // Get the report
        report = reportingUtil.generateReport();

        // Set widget values
        showReport();
    }

    private void showReport() {
        tvTotalExpenses.setText(getResources().getString(R.string.total_expenses, report.totalExpenses));
        tvTotalIncome.setText(getResources().getString(R.string.total_income, report.totalIncome));
    }
}
