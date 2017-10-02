package king.zach.pynny;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import king.zach.pynny.database.models.Category;
import king.zach.pynny.database.models.PynnyDBHandler;

public class CreateCategoryActivity extends AppCompatActivity {

    private static final String TAG = "CreateCategoryView";

    private PynnyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        dbHandler = PynnyDBHandler.getInstance(this);

        // Bind the submit button to create the new data and add it to the database
        findViewById(R.id.btnCreateCategorySubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndSaveCategory(view);
                finish();
            }
        });
    }

    private void createAndSaveCategory(View view) {
        // Get data from view
        String name = ((EditText) findViewById(R.id.etCreateCategoryName)).getText().toString();
        boolean isIncome = ((Switch) findViewById(R.id.swCreateCategoryIsIncome)).isChecked();

        // Add to database
        dbHandler.addCategory(new Category(name, isIncome));

        // End activity
        setResult(RESULT_OK);
        finishActivity(RequestsManager.REQUEST_NEW_CATEGORY);
    }
}
