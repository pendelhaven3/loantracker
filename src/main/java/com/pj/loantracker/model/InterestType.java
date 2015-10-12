package com.pj.loantracker.model;

public enum InterestType {

	NON_ADVANCE_INTEREST("Not Advance Interest"), 
	ADVANCE_INTEREST("Advance Interest");

	private String description;
	
	private InterestType(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
