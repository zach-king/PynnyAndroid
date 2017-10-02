package king.zach.pynny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import king.zach.pynny.database.models.Wallet;

public class OneWalletActivity extends AppCompatActivity {

    private Wallet wallet;
    private TextView tvWalletBalance;
    private ListView lvTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_wallet);

        tvWalletBalance = (TextView) findViewById(R.id.tvOneWalletBalance);
        lvTransactions = (ListView) findViewById(R.id.lvOneWalletTransactions);

        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(AllWalletsActivity.EXTRA_WALLET);

        setViewFields();
    }

    private void setViewFields() {
        setTitle(wallet.getName() + " (Wallet)");
        tvWalletBalance.setText(String.format(Locale.US, "$%.2f", wallet.getBalance()));
    }
}
