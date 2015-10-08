package com.pj.loantracker.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.pj.loantracker.util.DateUtil;

public class LoanTest {

	private Loan loan;
	
	@Before
	public void setUp() {
		loan = new Loan();
	}
	
	@Test
	public void computeLoanPaymentCalculatedFields() {
		loan.setAmount(new BigDecimal("100"));
		loan.setInterestRate(new BigDecimal("1.25"));
		
		LoanPayment payment1 = new LoanPayment();
		payment1.setPaymentDate(DateUtil.toDate("09/01/2015"));
		payment1.setAmount(new BigDecimal("10"));

		LoanPayment payment2 = new LoanPayment();
		payment2.setPaymentDate(DateUtil.toDate("10/01/2015"));
		payment2.setAmount(new BigDecimal("1"));
		
		loan.setPayments(Arrays.asList(payment1, payment2));
		
		loan.computePaymentCalculatedFields();
		
		assertEquals(new BigDecimal("1.25"), payment1.getInterest());
		assertEquals(new BigDecimal("1.25"), payment1.getInterestPaid());
		assertEquals(new BigDecimal("8.75"), payment1.getPrincipalPaid());
		assertEquals(new BigDecimal("91.25"), payment1.getPrincipalRemaining());
		
		assertEquals(new BigDecimal("1.14"), payment2.getInterest());
		assertEquals(new BigDecimal("1.00"), payment2.getInterestPaid().setScale(2));
		assertEquals(new BigDecimal("0.00"), payment2.getPrincipalPaid().setScale(2));
		assertEquals(new BigDecimal("91.39"), payment2.getPrincipalRemaining());
	}
	
}
