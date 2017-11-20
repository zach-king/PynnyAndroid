package king.zach.pynny.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    private static String TAG = "ReportingUtil";

    // Use the singleton design pattern
    private static ReportingUtil sInstance;
    private Context context;
    private PynnyDBHandler dbHandler;

    private ReportingUtil(Context context) {
        this.context = context;
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
            double walletIncome = dbHandler.getIncomeForWallet(walletId);

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

        return report;
    }

    public void saveReport(Report report) {

        if (context == null) {
            Log.e(TAG, "Context is null");
            return;
        }
        try {
            Log.d(TAG, context.getFilesDir().getAbsolutePath());
            FileOutputStream fos = context.openFileOutput("pynny_report_" + report.createdAt, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(report);
            os.close();
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "Error while saving report: " + e.getMessage());
        }
    }

    public Report loadReport(String reportFilePath) {
        Report report = null;

        try {
            FileInputStream fis = context.openFileInput(reportFilePath);
            ObjectInputStream is = new ObjectInputStream(fis);
            report = (Report) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {

        }

        return report;
    }

}
