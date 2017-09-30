package king.zach.pynny.database.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Zach on 9/11/2017.
 */

public class PynnyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pynny.db";

    // Categories
    private static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "_id";
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_IS_INCOME = "isIncome";
    public static final String[] CATEGORY_COLUMNS = {COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME, COLUMN_CATEGORY_IS_INCOME};

    // Wallets
    private static final String TABLE_WALLETS = "wallets";
    public static final String COLUMN_WALLET_ID = "_id";
    public static final String COLUMN_WALLET_NAME = "name";
    public static final String COLUMN_WALLET_BALANCE = "balance";
    public static final String COLUMN_WALLET_CREATED_AT = "created_at";
    public static final String[] WALLET_COLUMNS = {COLUMN_WALLET_ID, COLUMN_WALLET_NAME, COLUMN_WALLET_BALANCE, COLUMN_WALLET_CREATED_AT};

    // Transactions
    private static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_TRANSACTION_ID = "_id";
    public static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    public static final String COLUMN_TRANSACTION_DESCRIPTION = "description";
    public static final String COLUMN_TRANSACTION_CATEGORY = "category";
    public static final String COLUMN_TRANSACTION_WALLET = "wallet";
    public static final String COLUMN_TRANSACTION_CREATED_AT = "created_at";
    public static final String[] TRANSACTION_COLUMNS = {
            COLUMN_TRANSACTION_ID,
            COLUMN_TRANSACTION_AMOUNT,
            COLUMN_TRANSACTION_DESCRIPTION,
            COLUMN_TRANSACTION_CATEGORY,
            COLUMN_TRANSACTION_WALLET,
            COLUMN_TRANSACTION_CREATED_AT
    };

    // Budgets
    private static final String TABLE_BUDGETS = "budgets";
    public static final String COLUMN_BUDGET_ID = "_id";
    public static final String COLUMN_BUDGET_CATEGORY = "category";
    public static final String COLUMN_BUDGET_WALLET = "wallet";
    public static final String COLUMN_BUDGET_GOAL = "goal";
    public static final String COLUMN_BUDGET_BALANCE = "balance";
    public static final String COLUMN_BUDGET_MONTH = "month";
    public static final String[] BUDGET_COLUMNS = {
            COLUMN_BUDGET_ID,
            COLUMN_BUDGET_GOAL,
            COLUMN_BUDGET_BALANCE,
            COLUMN_BUDGET_WALLET,
            COLUMN_BUDGET_CATEGORY,
            COLUMN_BUDGET_MONTH
    };

    public PynnyDBHandler(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create initial tables
        db.execSQL("PRAGMA foreign_keys = ON");

        // Categories
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "(" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY," +
                COLUMN_CATEGORY_NAME + " TEXT NOT NULL," +
                COLUMN_CATEGORY_IS_INCOME + " INTEGER NOT NULL" +
                ")";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        // Wallets
        String createWalletsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_WALLETS + "(" +
                COLUMN_WALLET_ID + " INTEGER PRIMARY KEY," +
                COLUMN_WALLET_NAME + " TEXT NOT NULL," +
                COLUMN_WALLET_BALANCE + " REAL NOT NULL," +
                COLUMN_WALLET_CREATED_AT + " TEXT NOT NULL" +
                ")";
        db.execSQL(createWalletsTable);

        // Transactions
        String createTransactionTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS + "(" +
                COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TRANSACTION_AMOUNT + " REAL NOT NULL," +
                COLUMN_TRANSACTION_DESCRIPTION + " TEXT," +
                COLUMN_TRANSACTION_CATEGORY + " INTEGER," +
                COLUMN_TRANSACTION_WALLET + " INTEGER," +
                COLUMN_TRANSACTION_CREATED_AT + " TEXT NOT NULL," +
                "FOREIGN KEY(" + COLUMN_TRANSACTION_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES +
                "(" + COLUMN_CATEGORY_ID + ") ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY(" + COLUMN_TRANSACTION_WALLET + ") REFERENCES " + TABLE_WALLETS +
                "(" + COLUMN_WALLET_ID + ") ON UPDATE CASCADE ON DELETE CASCADE)";
        db.execSQL(createTransactionTable);

        // Budgets
        String createBudgetsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_BUDGETS + "(" +
                COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY," +
                COLUMN_BUDGET_GOAL + " REAL NOT NULL," +
                COLUMN_BUDGET_BALANCE + " REAL NOT NULL," +
                COLUMN_BUDGET_WALLET + " INTEGER," +
                COLUMN_BUDGET_CATEGORY + " INTEGER NOT NULL," +
                COLUMN_BUDGET_MONTH + " TEXT NOT NULL," +
                "FOREIGN KEY(" + COLUMN_BUDGET_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES +
                "(" + COLUMN_CATEGORY_ID + ") ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY(" + COLUMN_BUDGET_WALLET + ") REFERENCES " + TABLE_WALLETS +
                "(" + COLUMN_WALLET_ID + ") ON UPDATE CASCADE ON DELETE CASCADE)";
        db.execSQL(createBudgetsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);
        onCreate(db);
    }

    public void addCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_IS_INCOME, category.getIsIncome());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public void addWallet(Wallet wallet) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLET_NAME, wallet.getName());
        values.put(COLUMN_WALLET_BALANCE, wallet.getBalance());
        values.put(COLUMN_WALLET_CREATED_AT, wallet.getCreated_at());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_WALLETS, null, values);
        db.close();
    }

    public void addTransaction(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(COLUMN_TRANSACTION_DESCRIPTION, transaction.getDescription());
        values.put(COLUMN_TRANSACTION_CATEGORY, transaction.getCategory().getId());
        values.put(COLUMN_TRANSACTION_WALLET, transaction.getWallet().getId());
        values.put(COLUMN_TRANSACTION_CREATED_AT, transaction.getCreated_at());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }

    public void addBudget(Budget budget) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUDGET_GOAL, budget.getGoal());
        values.put(COLUMN_BUDGET_BALANCE, budget.getBalance());
        values.put(COLUMN_BUDGET_MONTH, budget.getMonth());
        values.put(COLUMN_BUDGET_CATEGORY, budget.getCategory().getId());

        Wallet wallet = budget.getWallet();
        if (wallet != null)
            values.put(COLUMN_BUDGET_WALLET, wallet.getId());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_BUDGETS, null, values);
        db.close();
    }

    public Category getCategory(long id) {
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_ID  + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Category category = new Category();

        if (cursor.moveToFirst()) {
            category.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
            category.setIsIncome(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_IS_INCOME)) != 0);
            cursor.close();
        } else {
            category = null;
        }
        db.close();
        return category;
    }

    public Wallet getWallet(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = { String.valueOf(id) };

        Cursor cursor = db.query(
                TABLE_WALLETS,
                WALLET_COLUMNS,
                COLUMN_WALLET_ID + " = ?",
                args, null, null, null
        );

        Wallet wallet = null;
        if (cursor.moveToFirst()) {
            long wId = cursor.getLong(cursor.getColumnIndex(COLUMN_WALLET_ID));
            String wName = cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_NAME));
            double wBalance = cursor.getDouble(cursor.getColumnIndex(COLUMN_WALLET_BALANCE));
            String wCreatedAt = cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_CREATED_AT));

            wallet = new Wallet(wId, wName, wBalance, wCreatedAt);
        }

        cursor.close();
        db.close();
        return wallet;
    }

    public Transaction getTransaction(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = { String.valueOf(id) };

        Cursor cursor = db.query(
                TABLE_TRANSACTIONS,
                TRANSACTION_COLUMNS,
                COLUMN_TRANSACTION_ID + " = ?",
                args,
                null, null, null
        );

        Transaction transaction = null;
        if (cursor.moveToFirst()) {
            long transId = cursor.getLong(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
            double transAmount = cursor.getDouble(cursor.getColumnIndex(COLUMN_TRANSACTION_AMOUNT));
            String transDesc = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSACTION_DESCRIPTION));
            long transCategory = cursor.getLong(cursor.getColumnIndex(COLUMN_TRANSACTION_CATEGORY));
            long transWallet = cursor.getLong(cursor.getColumnIndex(COLUMN_TRANSACTION_WALLET));
            String transCreatedAt = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSACTION_CREATED_AT));

            Category category = getCategory(transCategory);
            Wallet wallet = getWallet(transWallet);

            transaction = new Transaction(transId, transAmount, category, transDesc, transCreatedAt, wallet);
        }

        cursor.close();
        db.close();
        return transaction;
    }

    public Budget getBudget(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = { String.valueOf(id) };

        Cursor cursor = db.query(
                TABLE_BUDGETS,
                BUDGET_COLUMNS,
                COLUMN_BUDGET_ID + " = ?",
                args,
                null, null, COLUMN_BUDGET_MONTH + " DESC"
        );

        Budget budget = null;
        if (cursor.moveToFirst()) {
            long bId = cursor.getLong(cursor.getColumnIndex(COLUMN_BUDGET_ID));
            double goal = cursor.getDouble(cursor.getColumnIndex(COLUMN_BUDGET_GOAL));
            double balance = cursor.getDouble(cursor.getColumnIndex(COLUMN_BUDGET_BALANCE));
            long catId = cursor.getLong(cursor.getColumnIndex(COLUMN_BUDGET_CATEGORY));
            long walId = cursor.getLong(cursor.getColumnIndex(COLUMN_BUDGET_WALLET));
            String month = cursor.getString(cursor.getColumnIndex(COLUMN_BUDGET_MONTH));

            Category category = getCategory(catId);
            Wallet wallet = getWallet(walId);

            budget = new Budget(bId, goal, balance, category, wallet, month);
        }

        cursor.close();
        db.close();
        return budget;
    }

    public Cursor getAllWalletsCursor() {
        return getReadableDatabase().query(
                TABLE_WALLETS, WALLET_COLUMNS, null, null, null, null, COLUMN_WALLET_CREATED_AT + " DESC"
        );
    }

    public Cursor getAllCategoriesCursor() {
        return getReadableDatabase().query(
                TABLE_CATEGORIES, CATEGORY_COLUMNS, null, null, null, null, null
        );
    }

    public Cursor getAllTransactionsCursor() {
        return getReadableDatabase().query(
                TABLE_TRANSACTIONS, TRANSACTION_COLUMNS, null, null, null, null, COLUMN_TRANSACTION_CREATED_AT + " DESC"
        );
    }

    public Cursor getAllBudgetsCursor() {
        return getReadableDatabase().query(
                TABLE_BUDGETS, BUDGET_COLUMNS, null, null, null, null, COLUMN_BUDGET_MONTH + " DESC"
        );
    }

    public boolean deleteCategory(long id) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        Category category = new Category();

        if (cursor.moveToFirst()) {
            category.setId(cursor.getLong(0));
            db.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_ID + " = ?",
                    new String[] { String.valueOf(category.getId()) });
            cursor.close();
            result = true;
        }

        db.close();
        return result;
    }

    public boolean deleteWallet(long id) {
        // TODO
        return false;
    }

    public boolean deleteTransaction(long id) {
        // TODO
        return false;
    }

    public boolean deleteBudget(long id) {
        // TODO
        return false;
    }

}
