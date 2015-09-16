package com.pj.loantracker.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.loantracker.dao.LoanDao;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.service.LoanService;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired private LoanDao loanDao;
	
	@Transactional
	@Override
	public void save(Loan loan) {
		loanDao.save(loan);
	}

	@Override
	public List<Loan> getAllLoans() {
		return loanDao.getAll();
	}

	@Override
	public Loan getLoan(long id) {
		return loanDao.get(id);
	}

}
