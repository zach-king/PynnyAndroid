package king.zach.pynny.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import king.zach.pynny.database.PynnyDBHandler;
import king.zach.pynny.database.models.Report;

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
        return report;
    }

}
