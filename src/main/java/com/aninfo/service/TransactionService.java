package com.aninfo.service;

import com.aninfo.model.Transaction;
import com.aninfo.model.Account;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.exceptions.InvalidTransactionTypeException;

import com.aninfo.repository.TransactionRepository;
import com.aninfo.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    public Transaction createTransaction(Long cbu, Double sum, String type) {

        if (type.equalsIgnoreCase("deposit")) {
            accountService.deposit(cbu, sum);
        } else if (type.equalsIgnoreCase("withdraw")) {
            accountService.withdraw(cbu, sum);
        } else {
            throw new InvalidTransactionTypeException("Invalid transaction type");
        }
        Transaction transaction = new Transaction(cbu, sum, type);
        return transactionRepository.save(transaction);

    }

    public Collection<Transaction> getTransactionsByCbu(Long cbu) {

        List<Transaction> transactionsByCbu = new ArrayList<Transaction>();
        List<Transaction> allTransactions = transactionRepository.findAll();

        for (Transaction t: allTransactions) {
            if (t.getCbu() == cbu) {
                transactionsByCbu.add(t);
            }
        }
        return  transactionsByCbu;

    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

}