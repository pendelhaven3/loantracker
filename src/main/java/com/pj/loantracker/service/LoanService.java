package com.pj.loantracker.service;

import java.util.List;

import com.pj.loantracker.model.Loan;

public interface LoanService {

	void save(Loan loan);
	
	List<Loan> getAllLoans();
	
	Loan getLoan(long id);
	
}
