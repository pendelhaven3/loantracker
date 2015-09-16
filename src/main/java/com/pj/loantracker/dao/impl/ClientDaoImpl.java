package com.pj.loantracker.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
