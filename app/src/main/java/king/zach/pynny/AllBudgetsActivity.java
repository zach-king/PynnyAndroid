package king.zach.pynny;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import king.zach.pynny.adapters.BudgetCursorAdapter;
import king.zach.pynny.adapters.TransactionCursorAdapter;
import king.zach.pynny.database.models.Budget;
import king.zach.pynny.database.models.PynnyDBHandler;
import king.zach.pynny.database.models.Transaction;

public class AllBudgetsActivity extends AppCompatActivity {

    public static final String EXTRA_BUDGET = "king.zach.pynny.AllBudgetsActivity.budget";

    private static final String TAG = "AllBudgetsView";

    private ListView lvBudgets;
    private FloatingActionButton addBudgetBtn;
    private BudgetCursorAdapter adapter;
    private PynnyDBHandler dbHandler;
    private Cursor cursor;

    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_budgets);

        // Get the user's budgets
        dbHandler = PynnyDBHandler.getInstance(this);
        cursor = dbHandler.getAllBudgetsCursor();

        // Attach the adapter to a ListView
        lvBudgets = (ListView) findViewById(R.id.lvAllBudgetsList);
        adapter = new BudgetCursorAdapter(this, cursor);
        lvBudgets.setAdapter(adapter);

        lvBudgets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, "Clicked Item in Budgets ListView, at position " + position + ", with id " + id);

                Budget budget = dbHandler.getBudget(id);
                viewBudget(view, budget);
            }
        });

        this.addBudgetBtn = (FloatingActionButton) findViewById(R.id.btnAllBudgetsAddBudget);
        this.addBudgetBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createBudget(view);
                    }
                }
        );
    }

    public void createBudget(View view) {
        // TODO
        Log.i(this.getClass().getName(), "creating new budget");
        Toast.makeText(
                getApplicationContext(),
                "Creating new budget",
                Toast.LENGTH_SHORT).show();
    }

    public void viewBudget(View view, Budget budget) {
        Intent intent = new Intent(this, OneBudgetActivity.class);
        intent.putExtra(EXTRA_BUDGET, budget);

        startActivity(intent);
    }
}
