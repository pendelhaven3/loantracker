package com.pj.loantracker.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.pj.loantracker.dao.LoanPaymentDao;
import com.pj.loantracker.model.LoanPayment;

@Repository
public class LoanPaymentDaoImpl implements LoanPaymentDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(LoanPayment payment) {
		if (payment.getId() == null) {
			entityManager.persist(payment);
		} else {
			entityManager.merge(payment);
		}
	}

	@Override
	public void delete(LoanPayment payment) {
		entityManager.remove(get(payment.getId()));
	}

	private LoanPayment get(Long id) {
		return entityManager.find(LoanPayment.class, id);
	}

}
