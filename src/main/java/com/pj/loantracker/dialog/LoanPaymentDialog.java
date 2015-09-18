package com.pj.loantracker.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.pj.loantracker.Parameter;
import com.pj.loantracker.gui.component.ShowDialog;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.model.LoanPayment;
import com.pj.loantracker.service.LoanService;
import com.pj.loantracker.util.DateUtil;
import com.pj.loantracker.util.FormatterUtil;
import com.pj.loantracker.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

@Component
public class LoanPaymentDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(LoanPaymentDialog.class);
	
	@Autowired private LoanService loanService;
	
	@FXML private DatePicker paymentDatePicker;
	@FXML private TextField bankField;
	@FXML private TextField checkNumberField;
	@FXML private TextField amountField;
	
	@Parameter private Loan loan;
	@Parameter private LoanPayment payment;
	
	@Override
	public void updateDisplay() {
		if (payment != null) {
			paymentDatePicker.setValue(DateUtil.toLocalDate(payment.getPaymentDate()));
			bankField.setText(payment.getBank());
			checkNumberField.setText(payment.getCheckNumber());
			amountField.setText(FormatterUtil.formatAmount(payment.getAmount()));
		}
	}
	
	@FXML public void saveLoanPayment() {
		if (!validateFields()) {
			return;
		}
		
		if (payment == null) {
			payment = new LoanPayment();
			payment.setParent(loan);
		}
		payment.setPaymentDate(DateUtil.toDate(paymentDatePicker.getValue()));
		if (!StringUtils.isEmpty(bankField.getText())) {
			payment.setBank(bankField.getText().trim());
		} else {
			payment.setBank(null);
		}
		if (!StringUtils.isEmpty(checkNumberField.getText())) {
			payment.setCheckNumber(checkNumberField.getText().trim());
		} else {
			payment.setCheckNumber(null);
		}
		payment.setAmount(NumberUtil.toBigDecimal(amountField.getText()));
		
		try {
			loanService.save(payment);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Loan Payment saved");
		hide();
	}
	
	private boolean validateFields() {
		if (paymentDatePicker.getValue() == null) {
			ShowDialog.error("Payment Date must be specified");
			paymentDatePicker.requestFocus();
			return false;
		}
		
		// TODO: Check if payment date already used
		
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
		
		return true;
	}

	@FXML public void cancel() {
		hide();
	}

	@Override
	protected String getDialogTitle() {
		if (loan != null) {
			return "Add Loan Payment";
		} else {
			return "Edit Loan Payment";
		}
	}

	@Override
	protected String getSceneName() {
		return "loanPayment";
	}

}