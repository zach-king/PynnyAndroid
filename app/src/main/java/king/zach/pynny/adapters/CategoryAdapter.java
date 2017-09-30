package king.zach.pynny.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import king.zach.pynny.R;
import king.zach.pynny.database.models.Category;

/**
 * Created by Zach on 9/24/2017.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {


    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data for this position
        Category category = getItem(position);

        // Check if an existing view is being reused; otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_list_item, parent, false);
        }

        // Lookup view for data population
        TextView tvCategoryId = (TextView) convertView.findViewById(R.id.tvCategoryId);
        TextView tvCategoryName = (TextView) convertView.findViewById(R.id.tvCategoryName);

        // Populate the data into the template view using the data object
        tvCategoryId.setText(String.valueOf(category.getId()));
        tvCategoryName.setText(category.getName());

        // Return the completed view to render on screen
        return convertView;
    }

    //    private Activity activity;
//    private ArrayList<Category> categories;
//    private static LayoutInflater inflater = null;
//
//    public CategoryAdapter(Activity activity, int textViewResourceId, ArrayList<Category> categories) {
//        super(activity, textViewResourceId, categories);
//        try {
//            this.activity = activity;
//            this.categories = categories;
//
//            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public int getCount() {
//        return categories.size();
//    }
//
//    public Category getItem(Category category) {
//        return category;
//    }
//
//    public int getItemId(Category category) {
//        return category.getId();
//    }
//
//    public static class ViewHolder {
//        public TextView displayName;
//        public TextView displayIsIncome;
//    }
//
//    @NonNull
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        Log.i(getClass().getName(), "CategoryAdapter:getView(...)");
//        View vi = convertView;
//        final ViewHolder holder;
//
//        try {
//            if (convertView == null) {
//                vi = inflater.inflate(R.layout.category_list_item, null);
//                holder = new ViewHolder();
//
//                holder.displayName = (TextView) vi.findViewById(R.id.displayName);
//                holder.displayIsIncome = (TextView) vi.findViewById(R.id.displayIsIncome);
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return vi;
//    }

}
