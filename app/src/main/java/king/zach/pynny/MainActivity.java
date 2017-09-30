package king.zach.pynny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* Called when the user presses on the 'DisplayAllCategories' button */
    public void viewAllCategories(View view) {
        Intent intent = new Intent(this, AllCategoriesActivity.class);
        startActivity(intent);
    }

    public void viewAllWallets(View view) {
        Intent intent = new Intent(this, AllWalletsActivity.class);
        startActivity(intent);
    }

    public void viewAllTransactions(View view) {
        Intent intent = new Intent(this, AllTransactionsActivity.class);
        startActivity(intent);
    }

    public void viewAllBudgets(View view) {
        Intent intent = new Intent(this, AllBudgetsActivity.class);
        startActivity(intent);
    }
}
