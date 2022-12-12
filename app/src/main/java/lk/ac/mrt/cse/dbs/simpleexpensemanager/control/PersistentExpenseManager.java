package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;


/**
 *
 */
public class PersistentExpenseManager extends ExpenseManager {
    DataBaseHelper db;
    public PersistentExpenseManager(Context context) {
        setup(context);
    }


    @Override
    public void setup(Context context) {
        /*** Begin generating dummy data for In-Memory implementation ***/
        db = new DataBaseHelper(context);
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(db);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(db);
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("200240M", "Yoda Bank", "Jathushan Raveendra", 10000.0);
        Account dummyAcct2 = new Account("200222A", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/

    }
}
