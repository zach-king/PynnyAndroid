package king.zach.pynny.activities.all;

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

import king.zach.pynny.activities.create.CreateTransactionActivity;
import king.zach.pynny.activities.single.OneTransactionActivity;
import king.zach.pynny.R;
import king.zach.pynny.database.adapters.TransactionCursorAdapter;
import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.models.Transaction;
import king.zach.pynny.utils.RequestsManager;

public class AllTransactionsActivity extends AppCompatActivity {

    public static final String EXTRA_TRANSACTION = "king.zach.pynny.activities.all.AllWalletsActivity.transaction";

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
        dbHandler = PynnyDBHandler.getInstance(this);
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
        Log.i(this.getClass().getName(), "creating new transaction");

        Intent intent = new Intent(this, CreateTransactionActivity.class);
        startActivityForResult(intent, RequestsManager.REQUEST_NEW_TRANSACTION);
    }

    public void viewTransaction(View view, Transaction transaction) {
        Toast.makeText(getApplicationContext(), transaction.getDescription(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OneTransactionActivity.class);
        intent.putExtra(EXTRA_TRANSACTION, transaction);

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestsManager.REQUEST_NEW_TRANSACTION) {
            if (resultCode == RESULT_OK) {
                cursor.close();
                cursor = dbHandler.getAllTransactionsCursor();
                adapter.changeCursor(cursor);

                Toast.makeText(getApplicationContext(), "Transaction created successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
