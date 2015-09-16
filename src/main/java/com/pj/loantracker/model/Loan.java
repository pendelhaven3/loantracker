package com.pj.loantracker.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity
public class Loan {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	private Client client;

	private BigDecimal amount;
	private BigDecimal interestRate;
	private Date loanDate;

	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	@OrderBy("paymentDate ASC")
	private List<LoanPayment> payments;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Date getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}

	public List<LoanPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<LoanPayment> payments) {
		this.payments = payments;
	}

}
