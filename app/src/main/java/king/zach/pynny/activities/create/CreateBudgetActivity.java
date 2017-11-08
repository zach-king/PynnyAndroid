package king.zach.pynny.activities.create;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import king.zach.pynny.R;
import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.adapters.CategoryCursorAdapter;
import king.zach.pynny.database.adapters.WalletCursorAdapter;
import king.zach.pynny.database.models.Budget;
import king.zach.pynny.database.models.Category;
import king.zach.pynny.database.models.Wallet;
import king.zach.pynny.utils.RequestsManager;
import king.zach.pynny.utils.TimeUtil;

public class CreateBudgetActivity extends AppCompatActivity {

    private static final String TAG = "CreateBudgetView";
    private PynnyDBHandler dbHandler;
    private WalletCursorAdapter walletsAdapter;
    private CategoryCursorAdapter categoryAdapter;
    private Cursor walletsCursor, categoryCursor;

    private Spinner spWallets;
    private Spinner spCategories;
    private EditText etGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);

        dbHandler = PynnyDBHandler.getInstance(this);
        walletsCursor = dbHandler.getAllWalletsCursor();
        walletsAdapter = new WalletCursorAdapter(this, walletsCursor);
        categoryCursor = dbHandler.getAllCategoriesCursor();
        categoryAdapter = new CategoryCursorAdapter(this, categoryCursor);

        spCategories = (Spinner) findViewById(R.id.spCreateBudgetCategory);
        spCategories.setAdapter(categoryAdapter);

        spWallets = (Spinner) findViewById(R.id.spCreateBudgetWallet);
        spWallets.setAdapter(walletsAdapter);

        etGoal = (EditText) findViewById(R.id.etCreateBudgetGoal);

        // Bind the submit button to create the new data and add it to the database
        findViewById(R.id.btnCreateBudgetSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndSaveCategory(view);
                finish();
            }
        });
    }


    private void createAndSaveCategory(View view) {
        // Get data from view
        Category category = null;
        Wallet wallet = null;

        long id = spWallets.getSelectedItemId();
        wallet = dbHandler.getWallet(id);

        id = spCategories.getSelectedItemId();
        category = dbHandler.getCategory(id);

        if (wallet == null || category == null) {
            Log.e(TAG, "Failed to retrieve the wallet or category for the new budget");
            Toast.makeText(getApplicationContext(), "Invalid Wallet or Category", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        double goal = Double.valueOf(etGoal.getText().toString());
        String month = TimeUtil.getCurrentTimeString();

        // Add to database
        dbHandler.addBudget(new Budget(goal, month, category, wallet));

        // End activity
        setResult(RESULT_OK);
        finishActivity(RequestsManager.REQUEST_NEW_BUDGET);
    }
}
