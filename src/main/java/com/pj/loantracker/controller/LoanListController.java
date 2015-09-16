package com.pj.loantracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.loantracker.gui.component.DoubleClickEventHandler;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.service.LoanService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoanListController extends AbstractController {

	@Autowired private LoanService loanService;
	
	@FXML private TableView<Loan> loansTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Loan List");
		loansTable.setItems(FXCollections.observableList(loanService.getAllLoans()));
		
		loansTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				if (!loansTable.getSelectionModel().isEmpty()) {
					stageController.showUpdateLoanScreen(
							loansTable.getSelectionModel().getSelectedItem());
				}
			}
		});
	}

	@FXML public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addLoan() {
		stageController.showAddLoanScreen();
	}

}
