package com.pj.loantracker.service;

import org.apache.poi.ss.usermodel.Workbook;

import com.pj.loantracker.model.report.AmortizationTableReport;

public interface ExcelService {

	Workbook generate(AmortizationTableReport report);
	
}
