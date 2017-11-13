package king.zach.pynny.activities.create;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import king.zach.pynny.R;
import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.adapters.CategoryCursorAdapter;
import king.zach.pynny.database.adapters.WalletCursorAdapter;
import king.zach.pynny.database.models.Category;
import king.zach.pynny.database.models.Transaction;
import king.zach.pynny.database.models.Wallet;
import king.zach.pynny.utils.RequestsManager;
import king.zach.pynny.utils.TimeUtil;

public class CreateTransactionActivity extends AppCompatActivity {

    private static final String TAG = "CreateTransactionView";

    private PynnyDBHandler dbHandler;
    private WalletCursorAdapter walletsAdapter;
    private Cursor walletsCursor;
    private CategoryCursorAdapter categoryAdapter;
    private Cursor categoryCursor;

    private Spinner spWallets;
    private Spinner spCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);

        dbHandler = PynnyDBHandler.getInstance(this);
        walletsCursor = dbHandler.getAllWalletsCursor();
        walletsAdapter = new WalletCursorAdapter(this, walletsCursor);
        categoryCursor = dbHandler.getAllCategoriesCursor();
        categoryAdapter = new CategoryCursorAdapter(this, categoryCursor);

        spWallets = (Spinner) findViewById(R.id.spCreateTransactionWallet);
        spWallets.setAdapter(walletsAdapter);

        spCategories = (Spinner) findViewById(R.id.spCreateTransactionCategory);
        spCategories.setAdapter(categoryAdapter);

        findViewById(R.id.btnCreateTransactionSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndSaveTransaction();
                finish();
            }
        });
    }

    private void createAndSaveTransaction() {
        Wallet wallet = null;
        Category category = null;

        long id = spWallets.getSelectedItemId();
        wallet = dbHandler.getWallet(id);

        id = spCategories.getSelectedItemId();
        category = dbHandler.getCategory(id);

        if (wallet == null || category == null) {
            Log.e(TAG, "Failed to retrieve the wallet or category for the new transaction");
            Toast.makeText(getApplicationContext(), "Invalid Wallet or Category", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        double amount = Double.valueOf(((EditText) findViewById(R.id.etCreateTransactionAmount)).getText().toString());
        DatePicker dp = (DatePicker) findViewById(R.id.dpCreateTransactionCreatedAt);
        String createdAt = TimeUtil.getTimeStringFromDatePicker(dp);

        String description = ((EditText) findViewById(R.id.etCreateTransactionDescription)).getText().toString();

        Log.i(TAG, "Creating new transaction using the " + wallet.getName() + " wallet and the " + category.getName() + " category");
        Transaction transaction = new Transaction(amount, category, description, createdAt, wallet);
        if (!dbHandler.addTransaction(transaction)) {
            Log.e(TAG, "Error while trying to add transaction");
        }

        setResult(RESULT_OK);
        finishActivity(RequestsManager.REQUEST_NEW_TRANSACTION);
    }
}
