package com.pj.loantracker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	
	@Enumerated(EnumType.STRING)
	private LoanType type;
	
	public void computeLoanPaymentCalculatedFields() {
		switch (type) {
		case STANDARD:
			computeStandardPaymentCalculatedFields();
			break;
		case ADVANCE_INTEREST:
			computeAdvanceInterestPaymentCalculatedFields();
			break;
		}
	}
	
	private void computeStandardPaymentCalculatedFields() {
		BigDecimal principal = amount;
		for (LoanPayment payment : payments) {
			BigDecimal interest = principal.multiply(
					interestRate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP))
					.setScale(2, RoundingMode.HALF_UP);
			payment.setInterest(interest);
			
			if (payment.getAmount().compareTo(interest) >= 0) {
				payment.setInterestPaid(interest);
				payment.setPrincipalPaid(payment.getAmount().subtract(interest));
				payment.setPrincipalRemaining(principal.subtract(payment.getPrincipalPaid()));
			} else {
				payment.setInterestPaid(payment.getAmount());
				payment.setPrincipalPaid(BigDecimal.ZERO);
				payment.setPrincipalRemaining(principal.add(interest.subtract(payment.getAmount())));
			}
			principal = payment.getPrincipalRemaining();
		}
	}

	/*
	 * Variables:
	 * x = principal paid
	 * y = interest paid
	 * 
	 * Equation 1:
	 * x + y = payment
	 * 
	 * Equation 2:
	 * y = (principal - x) * rate
	 * 
	 * Solve for x and y!
	 * 
	 */
	private void computeAdvanceInterestPaymentCalculatedFields() {
		BigDecimal principal = amount;
		BigDecimal rate = interestRate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
		for (LoanPayment payment : payments) {
			payment.setPrincipalPaid(payment.getAmount().subtract(rate.multiply(principal))
					.divide(BigDecimal.ONE.subtract(rate), 2, RoundingMode.HALF_UP));
			payment.setInterestPaid(payment.getAmount().subtract(payment.getPrincipalPaid()));
			payment.setInterest(payment.getInterestPaid());
			payment.setPrincipalRemaining(principal.subtract(payment.getPrincipalPaid()));
			principal = payment.getPrincipalRemaining();
		}
	}
	
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

	public LoanType getType() {
		return type;
	}

	public void setType(LoanType type) {
		this.type = type;
	}

}
