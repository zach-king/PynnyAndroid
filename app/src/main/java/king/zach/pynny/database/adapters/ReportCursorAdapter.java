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
 * Created by zachking on 11/20/17.
 */

public class ReportCursorAdapter extends CursorAdapter {

    public ReportCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView() is used to inflate a new view and return it;
    // you don't bind any data to the view at this point
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.wallet_list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fileds to populate in inflated template (reusing the wallet list item resource/xml)
        TextView tvWalletName = (TextView) view.findViewById(R.id.tvWalletListItemName);
        TextView tvWalletBalance = (TextView) view.findViewById(R.id.tvWalletListItemBalance);

        // Extract properties from cursor
        String walletName = cursor.getString(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_WALLET_NAME));
        double walletBalance = cursor.getDouble(cursor.getColumnIndexOrThrow(PynnyDBHandler.COLUMN_WALLET_BALANCE));

        // Populate the fields in the view
        tvWalletName.setText(walletName);
        tvWalletBalance.setText(String.format(Locale.US, "$%.2f", walletBalance));
    }

}
