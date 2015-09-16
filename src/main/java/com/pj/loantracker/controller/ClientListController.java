package com.pj.loantracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.loantracker.gui.component.DoubleClickEventHandler;
import com.pj.loantracker.model.Client;
import com.pj.loantracker.service.ClientService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClientListController extends AbstractController {

	@Autowired private ClientService clientService;
	
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

}
