package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is a persistent implementation of TransactionDAO interface.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private final DataBaseHelper db;

    public PersistentTransactionDAO(DataBaseHelper db) {
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        db.insertTransaction(strDate, accountNo, expenseType, amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        Cursor cursor = db.getTransactions();
        List<Transaction> transactions = new ArrayList<Transaction>();
        if(cursor.getCount()==0){
            return transactions;
        }
        while(cursor.moveToNext()){
            ExpenseType type;
            String acc_no=cursor.getString(1);
            String date_temp = cursor.getString(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date =formatter.parse(date_temp);
            double amount= cursor.getDouble(3);
            String type_temp = cursor.getString(4);

            type = ExpenseType.valueOf(type_temp);
            Transaction transaction = new Transaction(date,acc_no,type,amount);
            transactions.add(transaction);

        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        Cursor cursor = db.getLimitedTransactions(limit);
        List<Transaction> transactions = new ArrayList<>();
        if(cursor.getCount()==0){
            return transactions;
        }
        while(cursor.moveToNext()){
            ExpenseType type;
            String accountNo = cursor.getString(1);
            String dateString = cursor.getString(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formatter.parse(dateString);
            double amount = cursor.getDouble(3);
            String typeString = cursor.getString(4);
            type = ExpenseType.valueOf(typeString);
            Transaction transaction = new Transaction(date, accountNo, type, amount);
            transactions.add(transaction);
        }
        return transactions;
    }
}