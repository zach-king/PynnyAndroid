package king.zach.pynny.database.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import king.zach.pynny.R;
import king.zach.pynny.database.PynnyDBHandler;


/**
 * Created by Zach on 9/28/2017.
 */

public class CategoryCursorAdapter extends CursorAdapter {

    public CategoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView() is used to inflate a new view and return it;
    // you don't bind any data to the view at this point
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fileds to populate in inflated template
        TextView tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);

        // Extract properties from cursor
        String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_CATEGORY_NAME));

        // Populate the fields in the view
        tvCategoryName.setText(categoryName);
    }
}
