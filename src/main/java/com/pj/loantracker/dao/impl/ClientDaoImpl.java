package com.pj.loantracker.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.pj.loantracker.dao.ClientDao;
import com.pj.loantracker.model.Client;

@Repository
public class ClientDaoImpl implements ClientDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(Client client) {
		if (client.getId() == null) {
			entityManager.persist(client);
		} else {
			entityManager.merge(client);
		}
	}

	@Override
	public List<Client> getAll() {
        return entityManager
        		.createQuery("SELECT c FROM Client c order by c.name", Client.class)
        		.getResultList();
	}

	@Override
	public Client get(long id) {
		return entityManager.find(Client.class, id);
	}

	@Override
	public void delete(Client client) {
		entityManager.remove(get(client.getId()));
	}

	@Override
	public Client findByName(String name) {
		CriteriaQuery<Client> criteria = entityManager.getCriteriaBuilder().createQuery(Client.class);
		Root<Client> client = criteria.from(Client.class);
		criteria.where(client.get("name").in(name));
		
		try {
			return entityManager.createQuery(criteria).getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			return null;
		}
	}

}
