package com.pj.loantracker.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.loantracker.dao.ClientDao;
import com.pj.loantracker.model.Client;
import com.pj.loantracker.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired private ClientDao clientDao;

	@Transactional
	@Override
	public void save(Client client) {
		clientDao.save(client);
	}

	@Override
	public List<Client> getAllClients() {
		return clientDao.getAll();
	}

	@Override
	public Client getClient(long id) {
		return clientDao.get(id);
	}

	@Transactional
	@Override
	public void delete(Client client) {
		clientDao.delete(client);
	}

	@Override
	public Client findClientByName(String name) {
		return clientDao.findByName(name);
	}

}
