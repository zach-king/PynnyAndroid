package king.zach.pynny.activities.create;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import king.zach.pynny.R;
import king.zach.pynny.utils.RequestsManager;
import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.models.Wallet;

public class CreateWalletActivity extends AppCompatActivity {

    public static final String TAG = "CreateWalletView";

    private PynnyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        dbHandler = PynnyDBHandler.getInstance(this);

        findViewById(R.id.btnCreateWalletSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndSaveWallet();
                finish();
            }
        });
    }

    private void createAndSaveWallet() {
        String name = ((EditText) findViewById(R.id.etCreateWalletName)).getText().toString();
        String sBalance = ((EditText) findViewById(R.id.etCreateWalletStartBalance)).getText().toString();

        double startBalance;
        if (sBalance.isEmpty() || sBalance.equals("0"))
            startBalance = 0.0;
        else
            startBalance = Double.valueOf (sBalance);

        dbHandler.addWallet(new Wallet(name, startBalance));
        setResult(RESULT_OK);
        finishActivity(RequestsManager.REQUEST_NEW_WALLET);
    }
}
