package com.pj.loantracker.service.impl;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.pj.loantracker.model.LoanPayment;
import com.pj.loantracker.model.report.AmortizationTableReport;
import com.pj.loantracker.service.ExcelService;

@Service
public class ExcelServiceImpl implements ExcelService {

	private String[] columnNames = {"A", "B", "C", "D", "E"};
	
	private CellStyle createAmountCellStyle(XSSFWorkbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat((short)BuiltinFormats.getBuiltinFormat("#,##0.00"));
		return cellStyle;
	}

	private CellStyle createDateCellStyle(XSSFWorkbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat((short)BuiltinFormats.getBuiltinFormat("m/d/yy"));
		return cellStyle;
	}

	@Override
	public Workbook generate(AmortizationTableReport report) {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(getClass().getResourceAsStream("/excel/amortization-table.xlsx"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		CellStyle amountCellStyle = createAmountCellStyle(workbook);
		CellStyle dateCellStyle = createDateCellStyle(workbook);
		
		Sheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		Row row = null;
		
		cell = sheet.getRow(0).createCell(1);
		cell.setCellValue(report.getLoan().getClient().getName());

		cell = sheet.getRow(1).createCell(1);
		cell.setCellValue(report.getLoan().getLoanDate());
		cell.setCellStyle(dateCellStyle);
		
		cell = sheet.getRow(2).createCell(1);
		cell.setCellValue(report.getLoan().getAmount().doubleValue());
		cell.setCellStyle(amountCellStyle);
		
		cell = sheet.getRow(3).createCell(1);
		cell.setCellValue(report.getLoan().getInterestRate().doubleValue());
		cell.setCellStyle(amountCellStyle);
		
		LoanPayment payment = report.getPayments().get(0);
		row = sheet.getRow(6);
		
		cell = row.createCell(0);
		cell.setCellValue(payment.getPaymentDate());
		cell.setCellStyle(dateCellStyle);
		
		if (report.isFixedMonthlyPaymentToPrincipal()) {
			cell = row.createCell(1, XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(
					MessageFormat.format("{0}+{1}", 
							getCellName(row.getRowNum() + 1, 3), 
							getCellName(row.getRowNum() + 1, 4)));
		} else {
			cell = row.createCell(1);
			cell.setCellValue(payment.getAmount().doubleValue());
		}
		cell.setCellStyle(amountCellStyle);
		
		cell = row.createCell(2, XSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula("ROUND($B$3*$B$4/100,2)");
		cell.setCellStyle(amountCellStyle);

		if (report.isFixedMonthlyPaymentToPrincipal()) {
			cell = row.createCell(3);
			cell.setCellValue(payment.getPrincipalPaid().doubleValue());
		} else {
			cell = row.createCell(3, XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula("B7-C7");
		}
		cell.setCellStyle(amountCellStyle);
		
		cell = row.createCell(4, XSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula("$B$3-D7");
		cell.setCellStyle(amountCellStyle);
		
		for (int i = 1; i < report.getPayments().size(); i++) {
			payment = report.getPayments().get(i);
			row = sheet.createRow(6 + i);
			
			cell = row.createCell(0, XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(
					MessageFormat.format("EDATE({0},1)", getCellName(row.getRowNum(), 1)));
			cell.setCellStyle(dateCellStyle);
			
			if (report.isFixedMonthlyPaymentToPrincipal()) {
				cell = row.createCell(1, XSSFCell.CELL_TYPE_FORMULA);
				cell.setCellFormula(
						MessageFormat.format("{0}+{1}", 
								getCellName(row.getRowNum() + 1, 3), 
								getCellName(row.getRowNum() + 1, 4)));
			} else {
				cell = row.createCell(1);
				cell.setCellValue(payment.getAmount().doubleValue());
			}
			cell.setCellStyle(amountCellStyle);
			
			cell = row.createCell(2, XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(
					MessageFormat.format("ROUND({0}*$B$4/100,2)", getCellName(row.getRowNum(), 5)));
			cell.setCellStyle(amountCellStyle);

			if (report.isFixedMonthlyPaymentToPrincipal()) {
				cell = row.createCell(3);
				cell.setCellValue(payment.getPrincipalPaid().doubleValue());
			} else {
				cell = row.createCell(3, XSSFCell.CELL_TYPE_FORMULA);
				cell.setCellFormula(
						MessageFormat.format("{0}-{1}", 
								getCellName(row.getRowNum() + 1, 2), 
								getCellName(row.getRowNum() + 1, 3)));
			}
			cell.setCellStyle(amountCellStyle);
			
			cell = row.createCell(4, XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(
					MessageFormat.format("{0}-{1}", 
							getCellName(row.getRowNum(), 5), 
							getCellName(row.getRowNum() + 1, 4)));
			cell.setCellStyle(amountCellStyle);
		}
		
		XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
		return workbook;
	}

	private String getCellName(int row, int column) {
		return columnNames[column - 1] + row;
	}
	
}
