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

import king.zach.pynny.adapters.CategoryCursorAdapter;
import king.zach.pynny.database.models.Category;
import king.zach.pynny.database.models.PynnyDBHandler;

public class AllCategoriesActivity extends AppCompatActivity {

    private static final String TAG = "AllCategoriesView";

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
        this.dbHandler = new PynnyDBHandler(this, null, null, 1);
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
        // TODO
        Log.i(this.getClass().getName(), "creating new category");
        Toast.makeText(
                getApplicationContext(),
                "Creating new category",
                Toast.LENGTH_SHORT).show();

        Category cat = new Category(count, "Category " + count, false);
        dbHandler.addCategory(cat);
        count++;

        cursor.close();
        cursor = dbHandler.getAllCategoriesCursor();
        adapter.changeCursor(cursor);
    }

    public void viewCategory(View view, Category category) {
        Intent intent = new Intent(this, OneCategoryActivity.class);
        startActivity(intent);
    }
}
