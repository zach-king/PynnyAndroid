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

import king.zach.pynny.adapters.TransactionCursorAdapter;
import king.zach.pynny.database.models.Category;
import king.zach.pynny.database.models.PynnyDBHandler;
import king.zach.pynny.database.models.Transaction;
import king.zach.pynny.database.models.Wallet;

public class AllTransactionsActivity extends AppCompatActivity {

    public static final String EXTRA_TRANSACTION = "king.zach.pynny.AllWalletsActivity.transaction";

    private static final String TAG = "AllTransactionsView";

    private ListView lvTransactions;
    private FloatingActionButton addTransactionBtn;
    private TransactionCursorAdapter adapter;
    private PynnyDBHandler dbHandler;
    private Cursor cursor;

    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);

        // Get the user's wallets
        dbHandler = new PynnyDBHandler(this, null, null, 1);
        cursor = dbHandler.getAllTransactionsCursor();

        // Attach the adapter to a ListView
        lvTransactions = (ListView) findViewById(R.id.lvAllTransactionsList);
        adapter = new TransactionCursorAdapter(this, cursor);
        lvTransactions.setAdapter(adapter);

        lvTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, "Clicked Item in Transactions ListView, at position " + position + ", with id " + id);

                Transaction transaction = dbHandler.getTransaction(id);
                Toast.makeText(getApplicationContext(),
                        transaction.getDescription(), Toast.LENGTH_SHORT)
                        .show();

                viewTransaction(view, transaction);
            }
        });

        this.addTransactionBtn = (FloatingActionButton) findViewById(R.id.btnAllTransactionsAddTransaction);
        this.addTransactionBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createTransaction(view);
                    }
                }
        );
    }

    public void createTransaction(View view) {
        // TODO
        Log.i(this.getClass().getName(), "creating new transaction");
        Toast.makeText(
                getApplicationContext(),
                "Creating new transaction",
                Toast.LENGTH_SHORT).show();
    }

    public void viewTransaction(View view, Transaction transaction) {
        Toast.makeText(getApplicationContext(), transaction.getDescription(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OneTransactionActivity.class);
        intent.putExtra(EXTRA_TRANSACTION, transaction);

        startActivity(intent);
    }
}
