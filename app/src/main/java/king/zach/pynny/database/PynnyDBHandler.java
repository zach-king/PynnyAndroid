package king.zach.pynny.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import king.zach.pynny.database.adapters.TransactionCursorAdapter;
import king.zach.pynny.database.models.Budget;
import king.zach.pynny.database.models.Category;
import king.zach.pynny.database.models.Transaction;
import king.zach.pynny.database.models.Wallet;

/**
 * Created by Zach on 9/11/2017.
 */

public class PynnyDBHandler extends SQLiteOpenHelper {

    // Using the Singleton design pattern
    private static PynnyDBHandler sInstance;

    private static final String TAG = "PynnyDBHandler";

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

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private PynnyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized PynnyDBHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PynnyDBHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    // Called when the database connection is being configured
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

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

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLETS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);
            onCreate(db);
        }
    }

    public void addCategory(Category category) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap the insert in a transaction.
        // This helps with performance and ensures consistency of the database
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, category.getName());
            values.put(COLUMN_CATEGORY_IS_INCOME, category.getIsIncome());

            // Don't specify the primary key. SQLite auto increments the primary key
            db.insertOrThrow(TABLE_CATEGORIES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add category to database", e.getCause());
        } finally {
            db.endTransaction();
        }
    }

    public void addWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_WALLET_NAME, wallet.getName());
            values.put(COLUMN_WALLET_BALANCE, wallet.getBalance());
            values.put(COLUMN_WALLET_CREATED_AT, wallet.getCreated_at());

            db.insertOrThrow(TABLE_WALLETS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying add wallet to database", e.getCause());
        } finally {
            db.endTransaction();
        }
    }

    public boolean addTransaction(Transaction transaction) {
        boolean successful = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TRANSACTION_AMOUNT, transaction.getAmount());
            values.put(COLUMN_TRANSACTION_DESCRIPTION, transaction.getDescription());
            values.put(COLUMN_TRANSACTION_CATEGORY, transaction.getCategory().getId());
            values.put(COLUMN_TRANSACTION_WALLET, transaction.getWallet().getId());
            values.put(COLUMN_TRANSACTION_CREATED_AT, transaction.getCreated_at());

            db.insertOrThrow(TABLE_TRANSACTIONS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add transaction to database", e.getCause());
        } finally {
            db.endTransaction();
        }

        // Propagate the effects of the transaction to the corresponding wallet
        Log.v(TAG, "Deducting transaction amount from wallet balance");
        if (transaction.getCategory().getIsIncome()) {
            transaction.getWallet().setBalance(transaction.getWallet().getBalance() + transaction.getAmount());
        } else {
            transaction.getWallet().setBalance(transaction.getWallet().getBalance() - transaction.getAmount());
        }

        successful = this.updateWallet(transaction.getWallet());
        if (!successful)
            return false;

        Budget budget = findBudget(transaction.getWallet().getId(), transaction.getCategory().getId());
        if (budget != null) {
            budget.setBalance(budget.getBalance() + transaction.getAmount());
            successful = updateBudget(budget);
        }

        return successful;
    }

    public void addBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BUDGET_GOAL, budget.getGoal());
            values.put(COLUMN_BUDGET_BALANCE, budget.getBalance());
            values.put(COLUMN_BUDGET_MONTH, budget.getMonth());
            values.put(COLUMN_BUDGET_CATEGORY, budget.getCategory().getId());

            Wallet wallet = budget.getWallet();
            if (wallet != null)
                values.put(COLUMN_BUDGET_WALLET, wallet.getId());

            db.insertOrThrow(TABLE_BUDGETS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error trying to add budget to database", e.getCause());
        } finally {
            db.endTransaction();
        }
    }

    public Category getCategory(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_CATEGORIES,
                CATEGORY_COLUMNS,
                COLUMN_CATEGORY_ID + " = ?",
                new String[] { String.valueOf(id) },
                null, null, null
        );

        Category category = null;

        if (cursor.moveToFirst()) {
            long cId = cursor.getLong(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
            boolean isIncome = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_IS_INCOME)) != 0;

            category = new Category(cId, name, isIncome);
        }

        cursor.close();
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

    public Budget findBudget(long walletId, long categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = { String.valueOf(walletId), String.valueOf(categoryId) };

        Cursor cursor = db.query(
                TABLE_BUDGETS,
                BUDGET_COLUMNS,
                COLUMN_BUDGET_WALLET + " = ? AND " + COLUMN_BUDGET_CATEGORY + " = ?",
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

    public Budget findBudget(long categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = { String.valueOf(categoryId) };

        Cursor cursor = db.query(
                TABLE_BUDGETS,
                BUDGET_COLUMNS,
                COLUMN_BUDGET_CATEGORY + " = ?",
                args, null, null, COLUMN_BUDGET_MONTH + " DESC"
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

    public List<Transaction> findTransactions(long categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = { String.valueOf(categoryId) };

        Cursor cursor = db.query(
                TABLE_TRANSACTIONS,
                TRANSACTION_COLUMNS,
                COLUMN_TRANSACTION_CATEGORY + " = ?",
                args,
                null, null, COLUMN_TRANSACTION_CREATED_AT + " DESC"
        );

        ArrayList<Transaction> transactions = new ArrayList<>();
        while (cursor.moveToNext()) {
            long tId = cursor.getLong(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
            double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_TRANSACTION_AMOUNT));
            long cId = cursor.getLong(cursor.getColumnIndex(COLUMN_TRANSACTION_CATEGORY));
            Category category = getCategory(cId);
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSACTION_DESCRIPTION));
            Wallet wallet = getWallet(cursor.getLong(cursor.getColumnIndex(COLUMN_TRANSACTION_WALLET)));
            String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSACTION_CREATED_AT));

            transactions.add(new Transaction(tId, amount, category, description, createdAt, wallet));
        }

        cursor.close();
        db.close();

        return transactions;
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

    public Cursor getTransactions(long walletId) {
        return getReadableDatabase().query(
                TABLE_TRANSACTIONS, TRANSACTION_COLUMNS, COLUMN_TRANSACTION_WALLET + " = " + String.valueOf(walletId),
                null, null, null, COLUMN_TRANSACTION_CREATED_AT + " DESC"
        );
    }

    public Cursor getTransactionsForBudget(long budgetId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " JOIN " + TABLE_BUDGETS + " ON " +
        TABLE_TRANSACTIONS + "." + COLUMN_TRANSACTION_CATEGORY + " = " + TABLE_BUDGETS + "." + COLUMN_BUDGET_CATEGORY +
        " WHERE " + TABLE_BUDGETS + "." + COLUMN_BUDGET_ID + " = " + String.valueOf(budgetId) + " ORDER BY " + TABLE_TRANSACTIONS +
                "." + COLUMN_TRANSACTION_CREATED_AT + " DESC", null);
    }

    public Cursor getTransactionsForCategory(long categoryId) {
        return getReadableDatabase().query(
                TABLE_TRANSACTIONS, TRANSACTION_COLUMNS, COLUMN_TRANSACTION_CATEGORY + " = " + String.valueOf(categoryId),
                null, null, null, COLUMN_TRANSACTION_CREATED_AT + " DESC"
        );
    }

    public Cursor getAllBudgetsCursor() {
        return getReadableDatabase().query(
                TABLE_BUDGETS, BUDGET_COLUMNS, null, null, null, null, COLUMN_BUDGET_MONTH + " DESC"
        );
    }

    public boolean updateWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = false;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_WALLET_BALANCE, wallet.getBalance());
            values.put(COLUMN_WALLET_NAME, wallet.getName());

            db.update(TABLE_WALLETS, values, COLUMN_WALLET_ID + " = " + wallet.getId(), null);
            db.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to udpate wallet in database", e.getCause());
        } finally {
            db.endTransaction();
        }

        return result;
    }

    public boolean updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = false;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TRANSACTION_AMOUNT, transaction.getAmount());
            values.put(COLUMN_TRANSACTION_CATEGORY, transaction.getCategory().getId());
            values.put(COLUMN_TRANSACTION_WALLET, transaction.getWallet().getId());
            values.put(COLUMN_TRANSACTION_DESCRIPTION, transaction.getDescription());
            values.put(COLUMN_TRANSACTION_CREATED_AT, transaction.getCreated_at());

            db.update(TABLE_TRANSACTIONS, values, COLUMN_TRANSACTION_ID + " = " + transaction.getId(), null);
            db.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to update transaction in database", e.getCause());
        } finally {
            db.endTransaction();
        }

        db.close();

        return result;
    }

    public boolean updateBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = false;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BUDGET_WALLET, budget.getWallet().getId());
            values.put(COLUMN_BUDGET_CATEGORY, budget.getCategory().getId());
            values.put(COLUMN_BUDGET_BALANCE, budget.getBalance());
            values.put(COLUMN_BUDGET_GOAL, budget.getGoal());
            values.put(COLUMN_BUDGET_MONTH, budget.getMonth());

            db.update(
                    TABLE_BUDGETS,
                    values,
                    COLUMN_BUDGET_ID + " = " + budget.getId(),
                    null
            );
            db.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            Log.e(TAG, "Error trying to update budget in database", e.getCause());
        } finally {
            db.endTransaction();
        }

        db.close();

        return result;
    }

    public boolean updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = false;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, category.getName());
            int isIncome = 0; if (category.getIsIncome()) { isIncome = 1; }
            values.put(COLUMN_CATEGORY_IS_INCOME, isIncome);

            db.update(
                    TABLE_CATEGORIES,
                    values,
                    COLUMN_CATEGORY_ID + " = " + category.getId(),
                    null
            );
            result = true;
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error trying to update category in database", e.getCause());
        } finally {
            db.endTransaction();
        }

        db.close();

        return result;
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
            result = true;
        }

        cursor.close();
        db.close();
        return result;
    }

    public boolean deleteWallet(long id) {
        boolean result = false;
        Wallet wallet = this.getWallet(id);
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_BUDGETS, COLUMN_BUDGET_WALLET + " = ?", new String[] { String.valueOf(id) });
        db.delete(TABLE_TRANSACTIONS, COLUMN_TRANSACTION_WALLET + " = ?", new String[] { String.valueOf(id) });
        result = db.delete(TABLE_WALLETS, COLUMN_WALLET_ID + " = ?",
                new String[] { String.valueOf(id) }) == 1;

        db.close();
        return result;
    }

    public boolean deleteTransaction(long id) {
        boolean result = false;
        Transaction transaction = this.getTransaction(id);
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TRANSACTIONS, COLUMN_TRANSACTION_ID + " = ?",
                new String[] { String.valueOf(id) });
        result = true;

        db.close();

        // Update wallet balance (undo transaction effects)
        Wallet newWallet = transaction.getWallet();
        newWallet.setBalance(newWallet.getBalance() + transaction.getAmount());
        result = this.updateWallet(newWallet);
        if (!result)
            return false;

        // If a budget exists for this wallet/category pair, update it too
        Budget budget = findBudget(newWallet.getId(), transaction.getCategory().getId());
        if (budget != null) {
            budget.setBalance(budget.getBalance() - transaction.getAmount());
            result = updateBudget(budget);
        }

        return result;
    }

    public boolean deleteBudget(long id) {
        boolean result = false;
        Transaction transaction = this.getTransaction(id);
        SQLiteDatabase db = this.getWritableDatabase();

        result = db.delete(TABLE_BUDGETS, COLUMN_BUDGET_ID + " = ?",
                new String[] { String.valueOf(id) }) == 1;

        db.close();
        return result;
    }

    public boolean invertCategory(Category category) {
        List<Transaction> effectedTransactions = findTransactions(category.getId());

        // Propagate the effects of the inversion to the affected wallets
        boolean result = true;
        for (int i = 0; i < effectedTransactions.size(); i++) {
            Transaction transaction = effectedTransactions.get(i);
            Wallet wallet = transaction.getWallet();

            if (category.getIsIncome()) {
                wallet.setBalance(wallet.getBalance() - (2.0 * transaction.getAmount()));
            } else {
                wallet.setBalance(wallet.getBalance() + (2.0 * transaction.getAmount()));
            }

            result = result && updateWallet(wallet);
        }

        return result;
    }

    public double getTotalExpenses() {
        double total = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_TRANSACTION_AMOUNT + ") FROM (SELECT " + COLUMN_TRANSACTION_AMOUNT + " FROM " + TABLE_TRANSACTIONS + " JOIN " + TABLE_CATEGORIES +
                " ON " + TABLE_TRANSACTIONS + "." + COLUMN_TRANSACTION_CATEGORY + " = " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_ID +
                " WHERE " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_IS_INCOME + " = 0)";

        Log.i(TAG, "Running query: " + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            total = cursor.getDouble(0);
        }

        cursor.close();

        return total;
    }

    public double getExpensesForCategory(long categoryId) {
        double total = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_TRANSACTION_AMOUNT + ") FROM (SELECT " + COLUMN_TRANSACTION_AMOUNT + " FROM " + TABLE_TRANSACTIONS + " JOIN " + TABLE_CATEGORIES +
                " ON " + TABLE_TRANSACTIONS + "." + COLUMN_TRANSACTION_CATEGORY + " = " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_ID +
                " WHERE " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_ID + " = " + categoryId + " AND " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_IS_INCOME + " = 0)";

        Log.i(TAG, "Running query: " + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            total = cursor.getDouble(0);
        }

        cursor.close();

        return total;
    }

    public double getTotalIncome() {
        double total = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_TRANSACTION_AMOUNT + ") FROM (SELECT " + COLUMN_TRANSACTION_AMOUNT + " FROM " + TABLE_TRANSACTIONS + " JOIN " + TABLE_CATEGORIES +
                " ON " + TABLE_TRANSACTIONS + "." + COLUMN_TRANSACTION_CATEGORY + " = " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_ID +
                " WHERE " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_IS_INCOME + " = 1)";

        Log.i(TAG, "Running query: " + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            total = cursor.getDouble(0);
        }

        cursor.close();

        return total;
    }

    public double getIncomeForCategory(long categoryId) {
        double total = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_TRANSACTION_AMOUNT + ") FROM (SELECT " + COLUMN_TRANSACTION_AMOUNT + " FROM " + TABLE_TRANSACTIONS + " JOIN " + TABLE_CATEGORIES +
                " ON " + TABLE_TRANSACTIONS + "." + COLUMN_TRANSACTION_CATEGORY + " = " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_ID +
                " WHERE " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_ID + " = " + categoryId + " AND " + TABLE_CATEGORIES + "." + COLUMN_CATEGORY_IS_INCOME + " = 1)";

        Log.i(TAG, "Running query: " + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            total = cursor.getDouble(0);
        }

        cursor.close();

        return total;
    }

}
