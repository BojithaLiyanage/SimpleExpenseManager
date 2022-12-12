package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final Helper helper;
    private final SimpleDateFormat simple_data_format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));

    public PersistentTransactionDAO(Helper helper)
    {
        this.helper =helper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        // put the data

        ContentValues content_vals = new ContentValues();
        content_vals.put(helper.COL_DATE, date.toString());
        content_vals.put(helper.COL_ACCOUNT_NO, accountNo);
        content_vals.put(helper.COL_TYPE, expenseType.toString());
        content_vals.put(helper.COL_AMOUNT, amount);

        // insert in to the transaction table
        db.insert(helper.TRANSACTION_TABLE, null, content_vals);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> return_list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        // get all the transaction data
        String queryString = "SELECT * FROM " + helper.TRANSACTION_TABLE;
        Cursor c = db.rawQuery(queryString,null);
        if (c.moveToFirst()){
           do {
                String acc_No = c.getString(c.getColumnIndex(helper.COL_ACCOUNT_NO));
                String type = c.getString(c.getColumnIndex(helper.COL_TYPE));
                ExpenseType expense_type;
                if (type.equals("EXPENSE")) {
                    expense_type = ExpenseType.EXPENSE;
                } else {
                    expense_type = ExpenseType.INCOME;
                }
                Date date = null;
                try {
                    String date_string = c.getString(c.getColumnIndex(helper.COL_DATE));
                    date = simple_data_format.parse(date_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                double amount = c.getDouble(c.getColumnIndex(helper.COL_AMOUNT));

                return_list.add(new Transaction(date, acc_No, expense_type, amount));
            } while(c.moveToNext());
        }

        // close the cursor
        c.close();
        // close the database
        db.close();
        return return_list;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> return_list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        String str4 =
                "SELECT * " +
                        "FROM " + helper.TRANSACTION_TABLE +
                        " ORDER BY " + helper.COL_TRANSACTION_ID +
                        " DESC LIMIT " + limit;
        Cursor c = db.rawQuery(str4,null);
        if (c.moveToFirst()){
           do {
                String acc_no = c.getString(c.getColumnIndex(helper.COL_ACCOUNT_NO));
                String type = c.getString(c.getColumnIndex(helper.COL_TYPE));
                ExpenseType expenseType;
                if (type.equals("EXPENSE")) {
                    expenseType = ExpenseType.EXPENSE;
                } else {
                    expenseType = ExpenseType.INCOME;
                }

                Date date = null;
                try {
                    String data_string = c.getString(c.getColumnIndex(helper.COL_DATE));
                    date = simple_data_format.parse(data_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                double amt = c.getDouble(c.getColumnIndex(helper.COL_AMOUNT));

                return_list.add(new Transaction(date, acc_no, expenseType, amt));

            } while(c.moveToNext());
        }
        // close the cursor
        c.close();
        // close the database
        db.close();
        return return_list;


    }
}