package com.pj.loantracker.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class LoanPayment {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Loan parent;
	
	private BigDecimal amount;
	private Date paymentDate;
	private String bank;
	private String checkNumber;

	@Transient
	private BigDecimal interest;
	
	@Transient
	private BigDecimal interestPaid;
	
	@Transient
	private BigDecimal principalPaid;
	
	@Transient
	private BigDecimal principalRemaining;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Loan getParent() {
		return parent;
	}

	public void setParent(Loan parent) {
		this.parent = parent;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getInterestPaid() {
		return interestPaid;
	}

	public void setInterestPaid(BigDecimal interestPaid) {
		this.interestPaid = interestPaid;
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

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

}
