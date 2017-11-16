package king.zach.pynny.activities.single;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import king.zach.pynny.R;
import king.zach.pynny.activities.all.AllCategoriesActivity;
import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.models.Category;

public class OneCategoryActivity extends AppCompatActivity {

    private Category category;
    private Switch swCategoryIsIncome;

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

        swCategoryIsIncome = (Switch) findViewById(R.id.swOneCategoryIsIncome);
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
    }
}
