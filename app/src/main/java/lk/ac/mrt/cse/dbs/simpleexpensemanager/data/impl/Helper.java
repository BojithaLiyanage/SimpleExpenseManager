package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

// extending the helper class by SQLiteOpenHelper
public class Helper extends SQLiteOpenHelper {
    public static final String ACCOUNT_TABLE = "Account_Table";
    public static final String COL_ACCOUNT_NO = "Account_No";
    public static final String COL_BANK = "Bank";
    public static final String COL_ACCOUNT_HOLDER = "Account_Holder";
    public static final String COL_BALANCE = "Balance";
    public static final String COL_TYPE = "Type";
    public static final String COL_AMOUNT = "Amount";
    public static final String COL_DATE = "Date";
    public static final String COL_TRANSACTION_ID = "Transaction_ID";
    public static final String TRANSACTION_TABLE = "Transaction_Table";

    public Helper(@Nullable Context context)
    {
        //Assigning the database
        super(context, "bojitha.db",null,1);
    }

    @Override
    // creating the data base
    public void onCreate(SQLiteDatabase b_db) {

        // SQL code for creating the account table
        String account_Table =
                "CREATE TABLE " +
                ACCOUNT_TABLE + "(" +
                COL_ACCOUNT_NO + " TEXT PRIMARY KEY," +
                COL_BANK + " TEXT NOT NULL, " +
                COL_ACCOUNT_HOLDER + " TEXT NOT NULL, " +
                COL_BALANCE + " REAL NOT NULL )";
        b_db.execSQL(account_Table);

        // SQL code for creating the transaction table
        String transaction_Table =
                "CREATE TABLE " + TRANSACTION_TABLE + " (" +
                COL_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_ACCOUNT_NO + " TEXT NOT NULL, " +
                COL_TYPE + " TEXT NOT NULL, " +
                COL_AMOUNT + " REAL NOT NULL, " +
                COL_DATE + " TEXT NOT NULL," +
                "FOREIGN KEY("+COL_ACCOUNT_NO + ")" +
                "REFERENCES "+ACCOUNT_TABLE+"(" + COL_ACCOUNT_NO +"))";

        b_db.execSQL(transaction_Table);
    }

    @Override
    // Upgrading the current database by creating anew on instead of the existing one
    public void onUpgrade(SQLiteDatabase b_db, int n1, int n2)
    {
        String drop_account_Table = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;
        String drop_transaction_Table = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;
        b_db.execSQL(drop_account_Table);
        b_db.execSQL(drop_transaction_Table);
        onCreate(b_db);
    }
}