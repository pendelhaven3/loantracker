package com.pj.loantracker.controller;

import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;

@Controller
public class MainMenuController extends AbstractController {

	@Override
	public void updateDisplay() {
		stageController.setTitle("Loan Tracker");
	}

	@FXML public void goToClientListScreen() {
		stageController.showClientListScreen();
	}

	@FXML public void goToLoanListScreen() {
		stageController.showLoanListScreen();
	}

}
