package com.pj.loantracker.model;

import java.math.BigDecimal;
import java.util.Date;

public class LoanRunningBalanceHistoryItem {

	private String type;
	private Date date;
	private BigDecimal amountPaid;
	private BigDecimal interest;
	private BigDecimal principalPaid;
	private BigDecimal principalRemaining;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getPrincipalPaid() {
		return principalPaid;
	}

	public void setPrincipalPaid(BigDecimal principalPaid) {
		this.principalPaid = principalPaid;
	}

	public BigDecimal getPrincipalRemaining() {
		return principalRemaining;
	}

	public void setPrincipalRemaining(BigDecimal principalRemaining) {
		this.principalRemaining = principalRemaining;
	}

}
