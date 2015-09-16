package com.pj.loantracker.dao;

import java.util.List;

import com.pj.loantracker.model.Loan;

public interface LoanDao {

	void save(Loan loan);
	
	List<Loan> getAll();
	
	Loan get(long id);
	
}
