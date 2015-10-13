package com.pj.loantracker.model.report;

import java.util.List;

import com.pj.loantracker.model.Loan;
import com.pj.loantracker.model.LoanPayment;

public class AmortizationTableReport {

	private Loan loan;
	private List<LoanPayment> payments;
	private boolean fixedMonthlyPaymentToPrincipal;

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	public List<LoanPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<LoanPayment> payments) {
		this.payments = payments;
	}

	public boolean isFixedMonthlyPaymentToPrincipal() {
		return fixedMonthlyPaymentToPrincipal;
	}

	public void setFixedMonthlyPaymentToPrincipal(boolean fixedMonthlyPaymentToPrincipal) {
		this.fixedMonthlyPaymentToPrincipal = fixedMonthlyPaymentToPrincipal;
	}

}
