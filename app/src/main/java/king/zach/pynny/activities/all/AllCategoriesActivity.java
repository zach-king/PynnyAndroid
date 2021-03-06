package king.zach.pynny.activities.all;

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

import king.zach.pynny.activities.create.CreateCategoryActivity;
import king.zach.pynny.activities.single.OneCategoryActivity;
import king.zach.pynny.R;
import king.zach.pynny.utils.RequestsManager;
import king.zach.pynny.database.adapters.CategoryCursorAdapter;
import king.zach.pynny.database.models.Category;
import king.zach.pynny.database.PynnyDBHandler;

public class AllCategoriesActivity extends AppCompatActivity {

    private static final String TAG = "AllCategoriesView";
    public static final String EXTRA_ONE_CATEGORY = "king.zach.pynny.activities.all.AllCategoriesActivity.category";

    private ListView categoriesListbox;
    private FloatingActionButton addCategoryBtn;
    private CategoryCursorAdapter adapter;
    private PynnyDBHandler dbHandler;
    private Cursor cursor;

    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        // Get the user's categories
        this.dbHandler = PynnyDBHandler.getInstance(this);
        cursor = dbHandler.getAllCategoriesCursor();

        // Attach the adapter to a ListView
        this.categoriesListbox = (ListView) findViewById(R.id.categoriesList);
        this.adapter = new CategoryCursorAdapter(this, cursor);
        categoriesListbox.setAdapter(adapter);

        this.categoriesListbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, "Clicked Item in Categories ListView, at position " + position + ", with id " + id);

                Category category = dbHandler.getCategory(id);
                Toast.makeText(getApplicationContext(),
                        category.getName(), Toast.LENGTH_SHORT)
                        .show();

                viewCategory(view, category);
            }
        });

        this.addCategoryBtn = (FloatingActionButton) findViewById(R.id.addCategoryBtn);
        this.addCategoryBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createCategory(view);
                    }
                }
        );
    }

    public void createCategory(View view) {
        Log.i(this.getClass().getName(), "creating new category");

        Intent intent = new Intent(this, CreateCategoryActivity.class);
        startActivityForResult(intent, RequestsManager.REQUEST_NEW_CATEGORY);
    }

    public void viewCategory(View view, Category category) {
        Intent intent = new Intent(this, OneCategoryActivity.class);
        intent.putExtra(EXTRA_ONE_CATEGORY, category);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestsManager.REQUEST_NEW_CATEGORY) {
            if (resultCode == RESULT_OK) {
                // Update the cursor; updates the list of categories
                cursor.close();
                cursor = dbHandler.getAllCategoriesCursor();
                adapter.changeCursor(cursor);

                Toast.makeText(getApplicationContext(), "Category created successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
