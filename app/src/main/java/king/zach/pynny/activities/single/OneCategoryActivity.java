package king.zach.pynny.activities.single;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import king.zach.pynny.R;
import king.zach.pynny.activities.all.AllCategoriesActivity;
import king.zach.pynny.activities.all.AllWalletsActivity;
import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.adapters.TransactionCursorAdapter;
import king.zach.pynny.database.models.Category;
import king.zach.pynny.database.models.Transaction;
import king.zach.pynny.database.models.Wallet;

public class OneCategoryActivity extends AppCompatActivity {

    private Category category;
    private Switch swCategoryIsIncome;
    private ListView lvTransactions;

    private TransactionCursorAdapter transactionCursorAdapter;
    private Cursor transactionsCursor;
    private PynnyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_category);

        if (dbHandler == null) {
            dbHandler = PynnyDBHandler.getInstance(this);
        }

        category = (Category) getIntent().getSerializableExtra(AllCategoriesActivity.EXTRA_ONE_CATEGORY);
        setTitle(category.getName() + " (Category)");

        if (swCategoryIsIncome == null)
            swCategoryIsIncome = (Switch) findViewById(R.id.swOneCategoryIsIncome);

        if (lvTransactions == null)
            lvTransactions = (ListView) findViewById(R.id.lvOneCategoryTransactions);

        swCategoryIsIncome.setChecked(category.getIsIncome());

        swCategoryIsIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swCategoryIsIncome.isChecked() != category.getIsIncome()) {
                    dbHandler.invertCategory(category);
                }

                category.setIsIncome(!category.getIsIncome());
                dbHandler.updateCategory(category);
            }
        });

        ((Button) findViewById(R.id.btnOneCategoryDelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteCategory(category.getId());
            }
        });

        transactionsCursor = dbHandler.getTransactionsForCategory(category.getId());
        transactionCursorAdapter = new TransactionCursorAdapter(this, transactionsCursor);
        lvTransactions.setAdapter(transactionCursorAdapter);
    }
}
