package king.zach.pynny;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import king.zach.pynny.database.models.Category;

public class OneCategoryActivity extends AppCompatActivity {

    private Category category;
    private Switch swCategoryIsIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_category);

        category = (Category) getIntent().getSerializableExtra(AllCategoriesActivity.EXTRA_ONE_CATEGORY);
        setTitle(category.getName() + " (Category)");

        swCategoryIsIncome = (Switch) findViewById(R.id.swOneCategoryIsIncome);
        swCategoryIsIncome.setChecked(category.getIsIncome());
    }
}
