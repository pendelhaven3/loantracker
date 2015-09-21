package com.pj.loantracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.loantracker.gui.component.DoubleClickEventHandler;
import com.pj.loantracker.gui.component.ShowDialog;
import com.pj.loantracker.model.Client;
import com.pj.loantracker.service.ClientService;
import com.pj.loantracker.service.LoanService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClientListController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(ClientListController.class);
	
	@Autowired private ClientService clientService;
	@Autowired private LoanService loanService;
	
	@FXML private TableView<Client> clientsTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Client List");
		clientsTable.setItems(FXCollections.observableList(clientService.getAllClients()));
		
		clientsTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				if (!clientsTable.getSelectionModel().isEmpty()) {
					stageController.showUpdateClientScreen(
							clientsTable.getSelectionModel().getSelectedItem());
				}
			}
		});
	}

	@FXML public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addClient() {
		stageController.showAddClientScreen();
	}

	@FXML public void deleteClient() {
		if (clientsTable.getSelectionModel().isEmpty()) {
			ShowDialog.error("No record selected");
			return;
		}
		
		Client client = clientsTable.getSelectionModel().getSelectedItem();
		
		if (isClientAlreadyReferenced(client)) {
			ShowDialog.error("Cannot delete client that is already referenced");
			return;
		}
		
		if (!ShowDialog.confirm("Delete client?")) {
			return;
		}
		
		try {
			clientService.delete(client);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Client deleted");
		updateDisplay();
	}

	private boolean isClientAlreadyReferenced(Client client) {
		return !loanService.findAllLoansByClient(client).isEmpty();
	}
	
}
