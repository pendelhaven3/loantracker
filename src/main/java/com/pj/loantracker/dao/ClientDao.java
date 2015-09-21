package com.pj.loantracker.dao;

import java.util.List;

import com.pj.loantracker.model.Client;

public interface ClientDao {

	void save(Client client);
	
	List<Client> getAll();

	Client get(long id);

	void delete(Client client);
	
}
