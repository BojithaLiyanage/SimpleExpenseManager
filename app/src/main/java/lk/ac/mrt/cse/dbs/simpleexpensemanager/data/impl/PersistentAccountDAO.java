package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

//Implementing the PersistentAccountDAO by AccountDAO
public class PersistentAccountDAO implements AccountDAO {
    private final Helper helper;
    public PersistentAccountDAO(Helper helper)
    {
        this.helper=helper;
    }

    @Override
    // Get the account numbers and add them to the database
    public List<String> getAccountNumbersList() {
        List<String> acc_no_list = new ArrayList<>();

        // get the data
        String str =
                "SELECT " + helper.COL_ACCOUNT_NO +
                        " FROM " + helper.ACCOUNT_TABLE;

        // create a readable database
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(str, null);

        if (c.moveToFirst())
        {
            while(c.moveToNext())
            {
                String new_acc_no = c.getString(c.getColumnIndex(helper.COL_ACCOUNT_NO));
                // add acc_num to the List
                acc_no_list.add(new_acc_no);
            };
        }
        else
        {
            System.out.println("No account numbers in the list");
        }
        // close the cursor
        c.close();
        // close the database
        db.close();
        return acc_no_list;
    }

    @Override
    // Get the account list and add them to account table in the database
    public List<Account> getAccountsList() {

        List<Account> return_list = new ArrayList<>();

        // get the data
        String queryString =
                "SELECT * FROM " + helper.ACCOUNT_TABLE;

        // create a readable databse
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c1 = db.rawQuery(queryString, null);

        if (c1.moveToFirst()){
            while(c1.moveToNext()) {
                String acc_no = c1.getString(c1.getColumnIndex(helper.COL_ACCOUNT_NO));
                String bank_name = c1.getString(c1.getColumnIndex(helper.COL_BANK));
                String accHolder = c1.getString(c1.getColumnIndex(helper.COL_ACCOUNT_HOLDER));
                Double current_balance = c1.getDouble(c1.getColumnIndex(helper.COL_BALANCE));
                // create an new account
                Account new_acc = new Account(acc_no, bank_name, accHolder, current_balance);
                return_list.add(new_acc);
            }
        }
        else{
            System.out.println("No accounts added");
        }
        // close the cursor
        c1.close();
        //  close the database
        db.close();
        return return_list;

    }

    @Override
    // Get the account objects
    public Account getAccount(String acc_no) throws InvalidAccountException {
        // get required data
        String str1 =
                "SELECT * FROM " + helper.ACCOUNT_TABLE +
                        " WHERE " + helper.COL_ACCOUNT_NO + " = " +
                        acc_no;

        // create a database
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c2 = db.rawQuery(str1, null);

        if (!c2.moveToFirst()) {
            String message = "Invalid Account No.";
            throw new InvalidAccountException(message);
        }

        String bank_name = c2.getString(c2.getColumnIndex(helper.COL_BANK));
        String accHolder = c2.getString(c2.getColumnIndex(helper.COL_ACCOUNT_HOLDER));
        Double current_balance = c2.getDouble(c2.getColumnIndex(helper.COL_BALANCE));

        // create an account
        Account account= new Account(acc_no,bank_name,accHolder,current_balance);

        // close the cursor
        c2.close();
        // close the database
        db.close();
        return account;
    }

    @Override
    // Add a account to the
    public void addAccount(Account acc) {
        // writable databse
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues current_val = new ContentValues();

        // passing all account details
        current_val.put(helper.COL_ACCOUNT_NO, acc.getAccountNo());
        current_val.put(helper.COL_BANK, acc.getBankName());
        current_val.put(helper.COL_ACCOUNT_HOLDER, acc.getAccountHolderName());
        current_val.put(helper.COL_BALANCE, acc.getBalance());

        // details add to the database
        db.insert(helper.ACCOUNT_TABLE, null, current_val);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] para = {accountNo};
        // sql query for the deletion of the account
        String str2 = "DELETE FROM " + helper.ACCOUNT_TABLE + " WHERE " + helper.COL_ACCOUNT_NO + "= ?";
        Cursor cursor = db.rawQuery(str2, para);
        // close the database
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] para = {accountNo};
        // get data
        String str4 =
                "SELECT " + helper.COL_BALANCE +
                        " FROM " + helper.ACCOUNT_TABLE +
                        " WHERE " + helper.COL_ACCOUNT_NO + " = ?" ;
        Cursor c3 = db.rawQuery(str4,para);

        if (!c3.moveToFirst()) {
            String message = "Invalid Account No.";
            throw new InvalidAccountException(message);
        }

        double current_balance = c3.getDouble(c3.getColumnIndex(helper.COL_BALANCE));

        switch (expenseType){
            case EXPENSE:
                current_balance-=amount;
                break;
            case INCOME:
                current_balance+=amount;
                break;
        }

        ContentValues content_vals = new ContentValues();
        content_vals.put(helper.COL_BALANCE, current_balance);
        db.update(helper.ACCOUNT_TABLE,content_vals, helper.COL_ACCOUNT_NO + " = ?" , para);
        // close the cursor
        c3.close();
        // close the database
        db.close();
    }
}