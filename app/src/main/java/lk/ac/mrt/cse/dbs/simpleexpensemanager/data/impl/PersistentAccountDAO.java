package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * This is an Persistent implementation of the AccountDAO interface.
 */
public class PersistentAccountDAO implements AccountDAO {
    private final DataBaseHelper db;

    public PersistentAccountDAO(DataBaseHelper db) {
        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor cursor = db.getAccountNum();
        List<String> accountNumbers = new ArrayList<>();
        if(cursor.getCount() == 0){
            return accountNumbers;
        }
        while(cursor.moveToNext()){
            String accountNo = cursor.getString(0);
            accountNumbers.add(accountNo);
        }
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor cursor = db.getAccounts();
        List<Account> accounts = new ArrayList<>();
        if(cursor.getCount()==0){
            return accounts;
        }
        while(cursor.moveToNext()){
            String accountNo = cursor.getString(0);
            String bank = cursor.getString(1);
            String accountHolder = cursor.getString(2);
            double balance = cursor.getDouble(3);
            Account account = new Account(accountNo, bank, accountHolder, balance);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = db.getAccount(accountNo);
        if (cursor.getCount() > 0) {
            String accNo = cursor.getString(0);
            String bank=cursor.getString(1);
            String owner= cursor.getString(2);
            double balance = cursor.getDouble(3);
            return new Account(accNo,bank,owner,balance);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        String accountNo = account.getAccountNo();
        String bank = account.getBankName();
        String accHolder = account.getAccountHolderName();
        double balance = account.getBalance();
        db.insertAccount(accountNo, bank, accHolder, balance);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        boolean delete = db.deleteAccount(accountNo);
        if (!delete){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        PersistentAccountDAO accountDAO = new PersistentAccountDAO(db);
        Account account = accountDAO.getAccount(accountNo);
        if(account == null){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        double currBalance = account.getBalance();
        switch (expenseType) {
            case EXPENSE:
                currBalance = account.getBalance() - amount;
                account.setBalance(currBalance);
                break;
            case INCOME:
                currBalance = account.getBalance() + amount;
                account.setBalance(currBalance);
                break;
        }
        db.updateBalance(accountNo,currBalance);
    }
}