package com.pj.loantracker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;

import com.pj.loantracker.util.DateUtil;

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
	
	@Column(columnDefinition = "boolean default false")
	private boolean cancelled;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(100) default 'NON_ADVANCE_INTEREST'")
	private InterestType interestType;
	
	@PrePersist
	private void preInsert() {
		cancelled = false;
	}
	
	public void computePaymentCalculatedFields() {
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

	public String getStatus() {
		if (cancelled) {
			return "Cancelled";
		} else {
			return "Ongoing";
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

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public List<LoanRunningBalanceHistoryItem> generateRunningBalanceHistory() {
		List<LoanRunningBalanceHistoryItem> items = new ArrayList<>();
		
		for (LoanPayment payment : payments) {
			LoanRunningBalanceHistoryItem item = new LoanRunningBalanceHistoryItem();
			item.setType("Payment");
			item.setDate(payment.getPaymentDate());
			item.setPrincipalPaid(payment.getAmount());
			items.add(item);
		}
		
		for (Date interestDate : DateUtil.generateMonthlyDates(loanDate, new Date())) {
			LoanRunningBalanceHistoryItem item = new LoanRunningBalanceHistoryItem();
			item.setType("Interest");
			item.setDate(interestDate);
			items.add(item);
		}
		
		Collections.sort(items, (o1, o2) -> {
			int result = o1.getDate().compareTo(o2.getDate());
			if (result == 0) {
				if (o1.getType().equals("Interest")) {
					return -1;
				} else if (o2.getType().equals("Interest")) {
					return 1;
				}
			}
			return result;
		});
		
		BigDecimal principal = amount;
		BigDecimal rate = interestRate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
		for (LoanRunningBalanceHistoryItem item : items) {
			switch (item.getType()) {
			case "Payment":
				if (DateUtil.compareTo(item.getDate(), new Date()) <= 0) {
					item.setPrincipalRemaining(principal.subtract(item.getPrincipalPaid()));
				}
				break;
			case "Interest":
				item.setInterest(principal.multiply(rate).setScale(2, RoundingMode.HALF_UP));
				item.setPrincipalRemaining(principal.add(item.getInterest()));
				break;
			}
			principal = item.getPrincipalRemaining();
		}
		
		return items;
	}

	public InterestType getInterestType() {
		return interestType;
	}

	public void setInterestType(InterestType interestType) {
		this.interestType = interestType;
	}
	
}
