package king.zach.pynny.activities.single;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import king.zach.pynny.R;
import king.zach.pynny.activities.all.AllWalletsActivity;
import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.adapters.TransactionCursorAdapter;
import king.zach.pynny.database.models.Wallet;

public class OneWalletActivity extends AppCompatActivity {

    private Wallet wallet;
    private TextView tvWalletBalance;
    private ListView lvTransactions;

    private Cursor transactionsCursor;
    private TransactionCursorAdapter transactionCursorAdapter;
    private PynnyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_wallet);

        if (tvWalletBalance == null)
            tvWalletBalance = (TextView) findViewById(R.id.tvOneWalletBalance);

        if (lvTransactions == null)
            lvTransactions = (ListView) findViewById(R.id.lvOneWalletTransactions);

        if (dbHandler == null)
            dbHandler = PynnyDBHandler.getInstance(this);

        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(AllWalletsActivity.EXTRA_WALLET);
        transactionsCursor = dbHandler.getTransactions(wallet.getId());
        transactionCursorAdapter = new TransactionCursorAdapter(this, transactionsCursor);
        lvTransactions.setAdapter(transactionCursorAdapter);

        setViewFields();
    }

    private void setViewFields() {
        setTitle(wallet.getName() + " (Wallet)");
        tvWalletBalance.setText(String.format(Locale.US, "$%.2f", wallet.getBalance()));
    }
}
