package com.pj.loantracker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;

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
	
	@PostLoad
	private void computeLoanPaymentCalculatedFields() {
		BigDecimal principal = amount;
		for (LoanPayment payment : payments) {
			if (payment.getAmount() == null) {
				continue; // TODO: workaround - @PostLoad is called during update of LoanPayment
			}
			
			BigDecimal interest = principal.multiply(
					interestRate.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))
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
