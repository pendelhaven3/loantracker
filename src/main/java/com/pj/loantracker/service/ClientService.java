package com.pj.loantracker.service;

import java.util.List;

import com.pj.loantracker.model.Client;

public interface ClientService {

	void save(Client client);
	
	List<Client> getAllClients();

	Client getClient(long id);
	
}
