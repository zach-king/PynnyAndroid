package king.zach.pynny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import king.zach.pynny.activities.all.AllBudgetsActivity;
import king.zach.pynny.activities.all.AllCategoriesActivity;
import king.zach.pynny.activities.all.AllTransactionsActivity;
import king.zach.pynny.activities.all.AllWalletsActivity;
import king.zach.pynny.activities.reporting.ReportingActivity;

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

    public void viewReport(View view) {
        Intent intent = new Intent(this, ReportingActivity.class);
        startActivity(intent);
    }
}
