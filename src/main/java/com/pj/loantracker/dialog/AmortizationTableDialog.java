package com.pj.loantracker.dialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import com.pj.loantracker.Parameter;
import com.pj.loantracker.gui.component.ShowDialog;
import com.pj.loantracker.gui.table.LoanPaymentsTableView;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.model.LoanPayment;
import com.pj.loantracker.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Component
public class AmortizationTableDialog extends AbstractDialog {

	@FXML private TextField monthlyPaymentField;
	@FXML private LoanPaymentsTableView paymentsTable;
	@FXML private Label totalPaymentsLabel;
	
	@Parameter private Loan loan;
	
	public AmortizationTableDialog() {
		setSceneWidth(800d);
	}
	
	@Override
	protected String getDialogTitle() {
		return "Amortization Table";
	}

	@Override
	protected void updateDisplay() {
		paymentsTable.setShowCheckColumn(false);
		paymentsTable.setShowInterestColumn(false);
		paymentsTable.initializeColumns();
		
		monthlyPaymentField.requestFocus();
	}

	@Override
	protected String getSceneName() {
		return "amortizationTable";
	}

	@FXML public void generateAmortizationTable() {
		if (!validateFields()) {
			return;
		}
		
		Loan loan = new Loan();
		loan.setAmount(this.loan.getAmount());
		loan.setLoanDate(this.loan.getLoanDate());
		loan.setType(this.loan.getType());
		loan.setInterestRate(this.loan.getInterestRate());
		
		BigDecimal amortizationAmount = NumberUtil.toBigDecimal(monthlyPaymentField.getText());
		Calendar paymentDateCalendar = DateUtils.toCalendar(loan.getLoanDate());
		List<LoanPayment> payments = new ArrayList<>();
		
		while (loan.getAmount().compareTo(BigDecimal.ZERO) > 0) {
			LoanPayment payment = new LoanPayment();
			
			if (loan.getAmount().compareTo(amortizationAmount) >= 0) {
				payment.setAmount(amortizationAmount);
			} else {
				switch (loan.getType()) {
				case STANDARD:
					BigDecimal rate = loan.getInterestRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
					payment.setAmount(loan.getAmount().add(
							loan.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP)));
					break;
				case ADVANCE_INTEREST:
					payment.setAmount(loan.getAmount());
					break;
				}
			}
			
			paymentDateCalendar.add(Calendar.MONTH, 1);
			payment.setPaymentDate(paymentDateCalendar.getTime());
			
			loan.setPayments(Arrays.asList(payment));
			loan.computeLoanPaymentCalculatedFields();
			
			payments.add(payment);
			loan.setAmount(payment.getPrincipalRemaining());
		}
		
		paymentsTable.getItems().clear();
		paymentsTable.getItems().addAll(payments);
		totalPaymentsLabel.setText(String.valueOf(payments.size()));
	}

	private boolean validateFields() {
		if (StringUtils.isEmpty(monthlyPaymentField.getText())) {
			ShowDialog.error("Monthly Payment must be specified");
			monthlyPaymentField.requestFocus();
			return false;
		}
		
		if (!NumberUtil.isAmount(monthlyPaymentField.getText())) {
			ShowDialog.error("Monthly Payment must be a valid amount");
			monthlyPaymentField.requestFocus();
			return false;
		}
		
		if (!isMonthlyPaymentGreaterThanInitialInterest()) {
			ShowDialog.error("Monthly Payment must be greater than initial interest");
			monthlyPaymentField.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isMonthlyPaymentGreaterThanInitialInterest() {
		Loan loan = new Loan();
		loan.setAmount(this.loan.getAmount());
		loan.setLoanDate(this.loan.getLoanDate());
		loan.setType(this.loan.getType());
		loan.setInterestRate(this.loan.getInterestRate());
		
		BigDecimal amortizationAmount = NumberUtil.toBigDecimal(monthlyPaymentField.getText());
		LoanPayment payment = new LoanPayment();
		
		if (loan.getAmount().compareTo(amortizationAmount) >= 0) {
			payment.setAmount(amortizationAmount);
		} else {
			return true;
		}
		
		loan.setPayments(Arrays.asList(payment));
		loan.computeLoanPaymentCalculatedFields();
		
		return payment.getAmount().compareTo(payment.getInterest()) > 0;
	}
	
}
