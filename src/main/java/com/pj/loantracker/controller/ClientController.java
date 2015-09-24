package com.pj.loantracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.loantracker.Parameter;
import com.pj.loantracker.gui.component.ShowDialog;
import com.pj.loantracker.model.Client;
import com.pj.loantracker.service.ClientService;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClientController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	@Autowired private ClientService clientService;
	
	@FXML private TextField nameField;
	@FXML private TextArea remarksTextArea;
	
	@Parameter private Client client;
	
	@Override
	public void updateDisplay() {
		if (client != null) {
			stageController.setTitle("Update Client");
			client = clientService.getClient(client.getId());
			nameField.setText(client.getName());
			remarksTextArea.setText(client.getRemarks());
		} else {
			stageController.setTitle("Add Client");
		}
		nameField.requestFocus();
	}

	@FXML public void doOnBack() {
		stageController.showClientListScreen();
	}

	@FXML public void saveClient() {
		if (!validateFields()) {
			return;
		}
		
		if (client == null) {
			client = new Client();
		}
		client.setName(nameField.getText());
		client.setRemarks(remarksTextArea.getText());
		
		try {
			clientService.save(client);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Client saved");
		stageController.showClientListScreen();
	}

	private boolean validateFields() {
		if (nameField.getText().isEmpty()) {
			ShowDialog.error("Name must be specified");
			nameField.requestFocus();
			return false;
		}
		
		if (isNameAlreadyUsed(nameField.getText())) {
			ShowDialog.error("Name is already used by an existing record");
			nameField.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isNameAlreadyUsed(String name) {
		Client existing = clientService.findClientByName(name);
		if (existing != null) {
			if (client != null) {
				return !client.equals(existing);
			} else {
				return true;
			}
		}
		return false;
	}

}
