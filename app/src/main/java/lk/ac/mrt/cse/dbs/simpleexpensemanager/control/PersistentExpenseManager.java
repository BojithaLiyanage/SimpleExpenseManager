package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.Helper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        setup(context);
    }
    @Override
    public void setup(Context context) throws ExpenseManagerException {
        Helper helper = new Helper(context);
        AccountDAO p_AccountDAO = new PersistentAccountDAO(helper);
        setAccountsDAO(p_AccountDAO);

        TransactionDAO p_TransactionDAO = new PersistentTransactionDAO(helper);
        setTransactionsDAO(p_TransactionDAO);

    }
}
