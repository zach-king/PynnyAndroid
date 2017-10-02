package king.zach.pynny.database.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Locale;

import king.zach.pynny.R;
import king.zach.pynny.database.PynnyDBHandler;

/**
 * Created by Zach on 9/30/2017.
 */

public class BudgetCursorAdapter extends CursorAdapter {

    private PynnyDBHandler dbHandler;

    public BudgetCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.dbHandler = PynnyDBHandler.getInstance(context);
    }

    // The newView() is used to inflate a new view and return it;
    // you don't bind any data to the view at this point
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.budget_list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fileds to populate in inflated template
        TextView tvBudgetHeader = (TextView) view.findViewById(R.id.tvBudgetListItemHeader);
        TextView tvBudgetSubheader = (TextView) view.findViewById(R.id.tvBudgetListItemSubHeader);

        // Extract properties from cursor
        double goal = cursor.getDouble(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_BUDGET_GOAL));
        double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_BUDGET_BALANCE));
        String categoryName = dbHandler.getCategory(cursor.getLong(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_BUDGET_CATEGORY))).getName();
        String walletName = dbHandler.getWallet(cursor.getLong(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_BUDGET_WALLET))).getName();

        // Populate the fields in the view
        tvBudgetHeader.setText(categoryName + " (" + walletName + ")");
        tvBudgetSubheader.setText(String.format(Locale.US, "$%.2f/%.2f", balance, goal));
    }
}
