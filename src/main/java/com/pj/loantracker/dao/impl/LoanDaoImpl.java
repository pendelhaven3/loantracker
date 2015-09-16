package com.pj.loantracker.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.pj.loantracker.dao.LoanDao;
import com.pj.loantracker.model.Loan;

@Repository
public class LoanDaoImpl implements LoanDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(Loan loan) {
		if (loan.getId() == null) {
			entityManager.persist(loan);
		} else {
			entityManager.merge(loan);
		}
	}

	@Override
	public List<Loan> getAll() {
        return entityManager
        		.createQuery("SELECT l FROM Loan l order by l.loanDate desc", Loan.class)
        		.getResultList();
	}

	@Override
	public Loan get(long id) {
		return entityManager.find(Loan.class, id);
	}

}
