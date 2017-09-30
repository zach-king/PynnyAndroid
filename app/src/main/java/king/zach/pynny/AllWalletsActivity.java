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


import king.zach.pynny.adapters.WalletCursorAdapter;
import king.zach.pynny.database.models.PynnyDBHandler;
import king.zach.pynny.database.models.Wallet;

public class AllWalletsActivity extends AppCompatActivity {

    public static final String EXTRA_WALLET = "king.zach.pynny.AllWalletsActivity.wallet";

    private static final String TAG = "AllWalletsView";

    private ListView walletsListView;
    private FloatingActionButton addWalletBtn;
    private WalletCursorAdapter adapter;
    private PynnyDBHandler dbHandler;
    private Cursor cursor;

    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_wallets);

        // Get the user's wallets
        dbHandler = new PynnyDBHandler(this, null, null, 1);
        cursor = dbHandler.getAllWalletsCursor();

        // Attach the adapter to a ListView
        walletsListView = (ListView) findViewById(R.id.lvAllWalletsList);
        adapter = new WalletCursorAdapter(this, cursor);
        walletsListView.setAdapter(adapter);

        walletsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, "Clicked Item in Wallets ListView, at position " + position + ", with id " + id);

                Wallet wallet = dbHandler.getWallet(id);
                Toast.makeText(getApplicationContext(),
                        wallet.getName(), Toast.LENGTH_SHORT)
                        .show();

                viewWallet(view, wallet);
            }
        });

        this.addWalletBtn = (FloatingActionButton) findViewById(R.id.btnAllWalletsAddWallet);
        this.addWalletBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createWallet(view);
                    }
                }
        );
    }

    public void createWallet(View view) {
        // TODO
        Log.i(this.getClass().getName(), "creating new wallet");
        Toast.makeText(
                getApplicationContext(),
                "Creating new wallet",
                Toast.LENGTH_SHORT).show();

        Wallet wallet = new Wallet("Wallet " + count);
        dbHandler.addWallet(wallet);
        count++;

        cursor.close();
        cursor = dbHandler.getAllWalletsCursor();
        adapter.changeCursor(cursor);
    }

    public void viewWallet(View view, Wallet wallet) {
        Toast.makeText(getApplicationContext(), wallet.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OneWalletActivity.class);
        intent.putExtra(EXTRA_WALLET, wallet);

        startActivity(intent);
    }
}
