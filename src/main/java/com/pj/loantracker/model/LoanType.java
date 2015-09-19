package com.pj.loantracker.model;

public enum LoanType {

	STANDARD("Standard"), ADVANCE_INTEREST("Advance Interest");

	private String description;
	
	private LoanType(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
