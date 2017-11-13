package king.zach.pynny.activities.single;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import king.zach.pynny.R;
import king.zach.pynny.activities.all.AllTransactionsActivity;
import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.models.Transaction;
import king.zach.pynny.database.models.Wallet;
import king.zach.pynny.utils.RequestsManager;

public class OneTransactionActivity extends AppCompatActivity {

    static final String TAG = "OneTransaction";

    private PynnyDBHandler dbHandler;

    private Transaction transaction;
    private TextView tvTransactionAmount;
    private TextView tvTransactionDescription;
    private TextView tvTransactionCategoryName;
    private TextView tvTransactionWalletName;
    private TextView tvTransactionCreatedAt;
    private Button btnDeleteTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_transaction);

        dbHandler = PynnyDBHandler.getInstance(this);

        tvTransactionAmount = (TextView) findViewById(R.id.tvOneTransactionAmount);
        tvTransactionDescription = (TextView) findViewById(R.id.tvOneTransactionDescription);
        tvTransactionCategoryName = (TextView) findViewById(R.id.tvOneTransactionCategoryName);
        tvTransactionWalletName = (TextView) findViewById(R.id.tvOneTransactionWalletName);
        tvTransactionCreatedAt = (TextView) findViewById(R.id.tvOneTransactionCreatedAt);

        Intent intent = getIntent();
        transaction = (Transaction) intent.getSerializableExtra(AllTransactionsActivity.EXTRA_TRANSACTION);

        btnDeleteTransaction = (Button) findViewById(R.id.btnOneTransactionDelete);
        btnDeleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTransaction();

                Toast.makeText(getApplicationContext(), "Deleted transaction", Toast.LENGTH_SHORT).show();

                btnDeleteTransaction.setVisibility(View.INVISIBLE);
                btnDeleteTransaction.setEnabled(false);
            }
        });

        setViewFields();
    }

    private void deleteTransaction() {
        Log.v(TAG, "Deleting transaction #" + transaction.getId());
        dbHandler.deleteTransaction(transaction.getId());
    }

    private void setViewFields() {
        setTitle("Transaction #" + String.valueOf(transaction.getId()));
        tvTransactionAmount.setText(String.format(Locale.US, "$%.2f", transaction.getAmount()));
        tvTransactionDescription.setText(transaction.getDescription());
        tvTransactionCategoryName.setText(transaction.getCategory().getName());
        tvTransactionWalletName.setText(transaction.getWallet().getName());
        tvTransactionCreatedAt.setText(transaction.getCreated_at());
    }
}
