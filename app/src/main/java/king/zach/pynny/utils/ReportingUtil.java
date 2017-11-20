package king.zach.pynny.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.models.Report;
import king.zach.pynny.database.models.Wallet;

/**
 * Created by zachking on 11/13/17.
 *
 * Defines various helper functions for
 * reporting functionality.
 */

public class ReportingUtil {

    // Use the singleton design pattern
    private static ReportingUtil sInstance;
    private Context context;
    private PynnyDBHandler dbHandler;

    private ReportingUtil(Context context) {
        dbHandler = PynnyDBHandler.getInstance(context);
    }

    public static synchronized ReportingUtil getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ReportingUtil(context.getApplicationContext());
        }

        return sInstance;
    }

    public Report generateReport() {
        Report report = new Report();
        report.totalExpenses = dbHandler.getTotalExpenses();
        report.totalIncome = dbHandler.getTotalIncome();

        Cursor walletCursor = dbHandler.getAllWalletsCursor();
        while (walletCursor != null && walletCursor.moveToNext()) {
            long walletId = walletCursor.getLong(walletCursor.getColumnIndex(PynnyDBHandler.COLUMN_WALLET_ID));
            String walletName = walletCursor.getString(walletCursor.getColumnIndex(PynnyDBHandler.COLUMN_WALLET_NAME));
            double walletExpenses = dbHandler.getExpenseForWallet(walletId);
            double walletIncome = dbHandler.getExpenseForWallet(walletId);

            report.expenseByWallet.add(new Pair<String, Double>(walletName, walletExpenses));
            report.incomeByWallet.add(new Pair<String, Double>(walletName, walletIncome));
        }
        walletCursor.close();

        Cursor categoryCursor = dbHandler.getAllCategoriesCursor();
        while (categoryCursor != null && categoryCursor.moveToNext()) {
            long categoryId = categoryCursor.getLong(categoryCursor.getColumnIndex(PynnyDBHandler.COLUMN_CATEGORY_ID));
            String categoryName = categoryCursor.getString(categoryCursor.getColumnIndex(PynnyDBHandler.COLUMN_CATEGORY_NAME));
            double categorySpendings = dbHandler.getSpendingForCategory(categoryId);

            report.spendingByCategory.add(new Pair<String, Double>(categoryName, categorySpendings));
        }
        categoryCursor.close();

        return report;
    }

}
