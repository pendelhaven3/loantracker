package com.pj.loantracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.loantracker.Parameter;
import com.pj.loantracker.dialog.LoanPaymentDialog;
import com.pj.loantracker.gui.component.DoubleClickEventHandler;
import com.pj.loantracker.gui.component.ShowDialog;
import com.pj.loantracker.model.Client;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.model.LoanPayment;
import com.pj.loantracker.service.ClientService;
import com.pj.loantracker.service.LoanService;
import com.pj.loantracker.util.DateUtil;
import com.pj.loantracker.util.FormatterUtil;
import com.pj.loantracker.util.NumberUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoanController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
	
	@Autowired private ClientService clientService;
	@Autowired private LoanService loanService;
	@Autowired private LoanPaymentDialog loanPaymentDialog;
	
	@FXML private ComboBox<Client> clientComboBox;
	@FXML private TextField amountField;
	@FXML private TextField interestField;
	@FXML private DatePicker loanDatePicker;
	@FXML private Button deleteButton;
	@FXML private TableView<LoanPayment> paymentsTable;
	
	@Parameter private Loan loan;
	
	@Override
	public void updateDisplay() {
		clientComboBox.setItems(FXCollections.observableList(clientService.getAllClients()));
		
		if (loan != null) {
			stageController.setTitle("Update Loan");
			
			loan = loanService.getLoan(loan.getId());
			
			clientComboBox.setValue(loan.getClient());
			amountField.setText(FormatterUtil.formatAmount(loan.getAmount()));
			interestField.setText(FormatterUtil.formatAmount(loan.getInterestRate()));
			loanDatePicker.setValue(DateUtil.toLocalDate(loan.getLoanDate()));
			
			paymentsTable.setItems(FXCollections.observableList(loan.getPayments()));
			paymentsTable.setOnMouseClicked(new DoubleClickEventHandler() {
				
				@Override
				protected void onDoubleClick(MouseEvent event) {
					if (!paymentsTable.getSelectionModel().isEmpty()) {
						updateLoanPayment();
					}
				}
			});
			
			deleteButton.setDisable(false);
		} else {
			stageController.setTitle("Add New Loan");
		}
		
		clientComboBox.requestFocus();
	}

	private void updateLoanPayment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payment", paymentsTable.getSelectionModel().getSelectedItem());
		
		loanPaymentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void doOnBack() {
		stageController.showLoanListScreen();
	}

	@FXML public void deleteLoan() {
		ShowDialog.info("Feature coming soon!");
		// TODO: Implement this
	}

	@FXML public void saveLoan() {
		if (!validateFields()) {
			return;
		}
		
		if (loan == null) {
			loan = new Loan();
		}
		loan.setClient(clientComboBox.getValue());
		loan.setAmount(NumberUtil.toBigDecimal(amountField.getText()));
		loan.setInterestRate(NumberUtil.toBigDecimal(interestField.getText()));
		loan.setLoanDate(DateUtil.toDate(loanDatePicker.getValue()));
		
		try {
			loanService.save(loan);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Loan saved");
		stageController.showLoanListScreen();
	}

	private boolean validateFields() {
		if (clientComboBox.getValue() == null) {
			ShowDialog.error("Client must be specified");
			clientComboBox.requestFocus();
			return false;
		}
		
		if (amountField.getText().isEmpty()) {
			ShowDialog.error("Amount must be specified");
			amountField.requestFocus();
			return false;
		}
		
		if (!NumberUtil.isAmount(amountField.getText())) {
			ShowDialog.error("Amount must be a valid amount");
			amountField.requestFocus();
			return false;
		}
		
		if (interestField.getText().isEmpty()) {
			ShowDialog.error("Interest must be specified");
			interestField.requestFocus();
			return false;
		}
		
		if (!NumberUtil.isAmount(interestField.getText())) {
			ShowDialog.error("Interest must be a valid number");
			interestField.requestFocus();
			return false;
		}
		
		if (loanDatePicker.getValue() == null) {
			ShowDialog.error("Loan Date must be specified");
			loanDatePicker.requestFocus();
			return false;
		}
		
		return true;
	}

	@FXML public void addLoanPayment() {
		Map<String, Object> model = new HashMap<>();
		model.put("loan", loan);
		
		loanPaymentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void deleteLoanPayment() {
		if (!isPaymentSelected()) {
			ShowDialog.error("No payment selected");
			return;
		}
		
		if (!ShowDialog.confirm("Delete payment?")) {
			return;
		}
		
		try {
			loanService.delete(paymentsTable.getSelectionModel().getSelectedItem());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payment deleted");
		updateDisplay();
	}

	private boolean isPaymentSelected() {
		return !paymentsTable.getSelectionModel().isEmpty();
	}

}
