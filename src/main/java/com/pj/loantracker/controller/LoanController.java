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
import com.pj.loantracker.dialog.AmortizationTableDialog;
import com.pj.loantracker.dialog.LoanPaymentDialog;
import com.pj.loantracker.gui.component.DoubleClickEventHandler;
import com.pj.loantracker.gui.component.ShowDialog;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.model.LoanPayment;
import com.pj.loantracker.service.LoanService;
import com.pj.loantracker.util.FormatterUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoanController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
	
	@Autowired private LoanService loanService;
	@Autowired private LoanPaymentDialog loanPaymentDialog;
	@Autowired private AmortizationTableDialog amortizationTableDialog;
	
	@FXML private Label clientLabel;
	@FXML private Label amountLabel;
	@FXML private Label interestLabel;
	@FXML private Label loanDateLabel;
	@FXML private Label loanTypeLabel;
	@FXML private Button deleteButton;
	@FXML private TableView<LoanPayment> paymentsTable;
	
	@Parameter private Loan loan;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Loan");
		
		loan = loanService.getLoan(loan.getId());
		
		clientLabel.setText(loan.getClient().toString());
		amountLabel.setText(FormatterUtil.formatAmount(loan.getAmount()));
		interestLabel.setText(FormatterUtil.formatAmount(loan.getInterestRate()));
		loanDateLabel.setText(FormatterUtil.formatDate(loan.getLoanDate()));
		if (loan.getType() != null) {
			loanTypeLabel.setText(loan.getType().toString());
		}
		
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

	@FXML public void addLoanPayment() {
		Map<String, Object> model = new HashMap<>();
		model.put("loan", loan);
		
		loanPaymentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void updateLoan() {
		Map<String, Object> model = new HashMap<>();
		model.put("loan", loan);
		
		stageController.showUpdateLoanScreen(loan);
	}

	@FXML public void generateAmortizationTable() {
		Map<String, Object> model = new HashMap<>();
		model.put("loan", loan);
		
		amortizationTableDialog.showAndWait(model);
	}

}
