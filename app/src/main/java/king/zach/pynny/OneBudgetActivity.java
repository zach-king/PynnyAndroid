package king.zach.pynny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import king.zach.pynny.database.models.Budget;

public class OneBudgetActivity extends AppCompatActivity {

    private Budget budget;
    private TextView tvRatio;
    private TextView tvMonth;
    private ListView lvTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_budget);

        tvRatio = (TextView) findViewById(R.id.tvOneBudgetRatio);
        tvMonth = (TextView) findViewById(R.id.tvOneBudgetMonth);
        lvTransactions = (ListView) findViewById(R.id.lvOneBudgetTransactions);

        Intent intent = getIntent();
        budget = (Budget) intent.getSerializableExtra(AllBudgetsActivity.EXTRA_BUDGET);

        setViewFields();
    }

    private void setViewFields() {
        setTitle("Budget #" + String.valueOf(budget.getId()));
        tvRatio.setText(String.format(Locale.US, "$%.2f/%.2f", budget.getBalance(), budget.getGoal()));
        tvMonth.setText(budget.getMonth());
    }
}
