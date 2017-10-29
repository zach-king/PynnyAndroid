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
        // TODO - Create the new Transaction and save it to the database
        Cursor cursor = (Cursor) spWallets.getSelectedItem();
        Wallet wallet = null;
        Category category = null;

        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(PynnyDBHandler.COLUMN_WALLET_ID));
            wallet = dbHandler.getWallet(id);
        }

        cursor.close();
        cursor = (Cursor) spCategories.getSelectedItem();
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(PynnyDBHandler.COLUMN_CATEGORY_ID));
            category = dbHandler.getCategory(id);
        }
        cursor.close();

        if (wallet == null || category == null) {
            Log.e(TAG, "Failed to retrieve the wallet or category for the new transaction");
            Toast.makeText(getApplicationContext(), "Invalid Wallet or Category", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        double amount = Double.valueOf(((EditText) findViewById(R.id.etCreateTransactionAmount)).getText().toString());
        DatePicker dp = (DatePicker) findViewById(R.id.dpCreateTransactionCreatedAt);
        int createdMonth = dp.getMonth();
        int createdDay = dp.getDayOfMonth();
        int createdYear = dp.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(createdYear, createdMonth, createdDay);
        String createdAt = calendar.getTime().toString();

        String description = ((EditText) findViewById(R.id.etCreateTransactionDescription)).getText().toString();

        Log.i(TAG, "Creating new transaction using the " + wallet.getName() + " wallet and the " + category.getName() + " category");
        Transaction transaction = new Transaction(amount, category, description, createdAt, wallet);
        dbHandler.addTransaction(transaction);

        setResult(RESULT_OK);
        finishActivity(RequestsManager.REQUEST_NEW_TRANSACTION);
    }
}
