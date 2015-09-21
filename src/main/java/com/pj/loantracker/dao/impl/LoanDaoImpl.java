package com.pj.loantracker.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.pj.loantracker.dao.LoanDao;
import com.pj.loantracker.model.Client;
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

	@Override
	public List<Loan> findAllByClient(Client client) {
		CriteriaQuery<Loan> criteria = entityManager.getCriteriaBuilder().createQuery(Loan.class);
		Root<Loan> loan = criteria.from(Loan.class);
		criteria.where(loan.get("client").in(client));
		
		return entityManager.createQuery(criteria).getResultList();
	}

}
