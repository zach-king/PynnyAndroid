package king.zach.pynny.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Locale;

import king.zach.pynny.R;
import king.zach.pynny.database.models.PynnyDBHandler;

/**
 * Created by Zach on 9/30/2017.
 */

public class TransactionCursorAdapter extends CursorAdapter {


    private PynnyDBHandler dbHandler;

    public TransactionCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.dbHandler = new PynnyDBHandler(context, null, null, 1);
    }

    // The newView() is used to inflate a new view and return it;
    // you don't bind any data to the view at this point
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.transaction_list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fileds to populate in inflated template
        TextView tvTransactionDescription = (TextView) view.findViewById(R.id.tvTransactionListItemDescription);
        TextView tvTransactionAmount = (TextView) view.findViewById(R.id.tvTransactionListItemAmount);
        TextView tvTransactionSubheading = (TextView) view.findViewById(R.id.tvTransactionListItemSubHeading);

        // Extract properties from cursor
        String transDesc = cursor.getString(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_TRANSACTION_DESCRIPTION));
        double transAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_TRANSACTION_AMOUNT));
        String transCategoryName = dbHandler.getCategory(cursor.getLong(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_TRANSACTION_CATEGORY))).getName();
        String transWalletName = dbHandler.getWallet(cursor.getLong(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_TRANSACTION_WALLET))).getName();

        // Populate the fields in the view
        tvTransactionDescription.setText(transDesc);
        tvTransactionAmount.setText(String.format(Locale.US, "$%.2f", transAmount));
        tvTransactionSubheading.setText(transWalletName + " - " + transCategoryName);
    }

}
