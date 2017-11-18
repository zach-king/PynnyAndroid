package king.zach.pynny.activities.single;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import king.zach.pynny.R;
import king.zach.pynny.activities.all.AllBudgetsActivity;
import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.adapters.TransactionCursorAdapter;
import king.zach.pynny.database.models.Budget;

public class OneBudgetActivity extends AppCompatActivity {

    private static String TAG = "OneBudgetActivity";

    private Budget budget;
    private TextView tvRatio;
    private TextView tvMonth;
    private ListView lvTransactions;

    private PynnyDBHandler dbHandler;
    private Cursor transactionsCursor;
    private TransactionCursorAdapter transactionCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_budget);

        if (tvRatio == null)
            tvRatio = (TextView) findViewById(R.id.tvOneBudgetRatio);

        if (tvMonth == null)
            tvMonth = (TextView) findViewById(R.id.tvOneBudgetMonth);

        if (lvTransactions == null)
            lvTransactions = (ListView) findViewById(R.id.lvOneBudgetTransactions);

        if (dbHandler == null)
            dbHandler = PynnyDBHandler.getInstance(this);

        Intent intent = getIntent();
        budget = (Budget) intent.getSerializableExtra(AllBudgetsActivity.EXTRA_BUDGET);

        transactionsCursor = dbHandler.getTransactionsForBudget(budget.getId());
        transactionCursorAdapter = new TransactionCursorAdapter(this, transactionsCursor);
        lvTransactions.setAdapter(transactionCursorAdapter);

        setViewFields();

        ((Button) findViewById(R.id.btnOneBudgetDelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteBudget(budget.getId());
            }
        });
    }

    private void setViewFields() {
        setTitle("Budget #" + String.valueOf(budget.getId()));
        tvRatio.setText(String.format(Locale.US, "$%.2f/%.2f", budget.getBalance(), budget.getGoal()));
        tvMonth.setText(budget.getMonth());
    }
}
