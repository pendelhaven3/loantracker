package com.pj.loantracker.service;

import java.util.List;

import com.pj.loantracker.model.Client;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.model.LoanPayment;

public interface LoanService {

	void save(Loan loan);
	
	List<Loan> getAllLoans();
	
	Loan getLoan(long id);

	void save(LoanPayment payment);

	void delete(LoanPayment selectedItem);

	List<Loan> findAllLoansByClient(Client client);
	
}
