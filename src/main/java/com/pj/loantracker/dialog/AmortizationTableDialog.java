package com.pj.loantracker.dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pj.loantracker.Parameter;
import com.pj.loantracker.gui.component.ShowDialog;
import com.pj.loantracker.gui.table.LoanPaymentsTableView;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.model.LoanPayment;
import com.pj.loantracker.util.FormatterUtil;
import com.pj.loantracker.util.NumberUtil;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Component
public class AmortizationTableDialog extends AbstractDialog {

	@FXML private TextField monthlyPaymentField;
	@FXML private LoanPaymentsTableView paymentsTable;
	@FXML private Label totalPaymentsLabel;
	
	@Parameter private Loan loan;
	
	public AmortizationTableDialog() {
		setSceneWidth(800d);
		setSceneHeight(500d);
	}
	
	@Override
	protected String getDialogTitle() {
		return "Amortization Table";
	}

	@Override
	protected void updateDisplay() {
		paymentsTable.setShowCheckColumn(false);
		paymentsTable.setShowInterestColumn(false);
		paymentsTable.initializeColumns();
		
		monthlyPaymentField.requestFocus();
	}

	@Override
	protected String getSceneName() {
		return "amortizationTable";
	}

	@FXML public void generateAmortizationTable() {
		if (!validateFields()) {
			return;
		}
		
		Loan loan = new Loan();
		loan.setAmount(this.loan.getAmount());
		loan.setLoanDate(this.loan.getLoanDate());
		loan.setInterestRate(this.loan.getInterestRate());
		
		BigDecimal amortizationAmount = NumberUtil.toBigDecimal(monthlyPaymentField.getText());
		Calendar paymentDateCalendar = DateUtils.toCalendar(loan.getLoanDate());
		List<LoanPayment> payments = new ArrayList<>();
		
		while (loan.getAmount().compareTo(BigDecimal.ZERO) > 0) {
			LoanPayment payment = new LoanPayment();
			
			if (loan.getAmount().compareTo(amortizationAmount) >= 0) {
				payment.setAmount(amortizationAmount);
			} else {
				BigDecimal rate = loan.getInterestRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
				payment.setAmount(loan.getAmount().add(
						loan.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP)));
				break;
			}
			
			paymentDateCalendar.add(Calendar.MONTH, 1);
			payment.setPaymentDate(paymentDateCalendar.getTime());
			
			loan.setPayments(Arrays.asList(payment));
			loan.computePaymentCalculatedFields();
			
			payments.add(payment);
			loan.setAmount(payment.getPrincipalRemaining());
		}
		
		paymentsTable.getItems().clear();
		paymentsTable.getItems().addAll(payments);
		totalPaymentsLabel.setText(String.valueOf(payments.size()));
	}

	private boolean validateFields() {
		if (StringUtils.isEmpty(monthlyPaymentField.getText())) {
			ShowDialog.error("Monthly Payment must be specified");
			monthlyPaymentField.requestFocus();
			return false;
		}
		
		if (!NumberUtil.isAmount(monthlyPaymentField.getText())) {
			ShowDialog.error("Monthly Payment must be a valid amount");
			monthlyPaymentField.requestFocus();
			return false;
		}
		
		if (!isMonthlyPaymentGreaterThanInitialInterest()) {
			ShowDialog.error("Monthly Payment must be greater than initial interest");
			monthlyPaymentField.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isMonthlyPaymentGreaterThanInitialInterest() {
		Loan loan = new Loan();
		loan.setAmount(this.loan.getAmount());
		loan.setLoanDate(this.loan.getLoanDate());
		loan.setInterestRate(this.loan.getInterestRate());
		
		BigDecimal amortizationAmount = NumberUtil.toBigDecimal(monthlyPaymentField.getText());
		LoanPayment payment = new LoanPayment();
		
		if (loan.getAmount().compareTo(amortizationAmount) >= 0) {
			payment.setAmount(amortizationAmount);
		} else {
			return true;
		}
		
		loan.setPayments(Arrays.asList(payment));
		loan.computePaymentCalculatedFields();
		
		return payment.getAmount().compareTo(payment.getInterest()) > 0;
	}
	
	@FXML public void generateAmortizationTableAsPdf() {
		if (!validateFields()) {
			return;
		}
		
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "Desktop").toFile());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF files", "*.pdf"));
        File file = fileChooser.showSaveDialog(this);
        if (file == null) {
        	return;
        }
        
		generateAmortizationTable();
		
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			document.add(createLoanAmountLine());
			document.add(createInterestRateLine());
			document.add(createPdfTables());
			document.add(createTotalPaymentsField());
		} catch (Exception e) {
			ShowDialog.unexpectedError();
		} finally {
			document.close();
		}
	}

	private Element createLoanAmountLine() {
		Paragraph paragraph = new Paragraph("Loan Amount: " + FormatterUtil.formatAmount(loan.getAmount()), 
				createHeaderFont());
		paragraph.setIndentationLeft(50);
		return paragraph;
	}

	private Element createInterestRateLine() {
		Paragraph paragraph = new Paragraph("Interest: " + FormatterUtil.formatAmount(loan.getInterestRate()) + "%", 
				createHeaderFont());
		paragraph.setIndentationLeft(50);
		paragraph.setSpacingAfter(10);
		return paragraph;
	}

	private Element createTotalPaymentsField() {
		return createCenteredText("Total Payments: " + paymentsTable.getItems().size());
	}

	private Element createCenteredText(String text) {
		Paragraph paragraph = new Paragraph(text, createHeaderFont());
		paragraph.setAlignment(Element.ALIGN_CENTER);
		return paragraph;
	}
	
	private PdfPTable createPdfTables() {
		ObservableList<LoanPayment> payments = paymentsTable.getItems();
		
		PdfPTable table = new PdfPTable(5);
		PdfPCell cell = null;
		Font fontBold = createHeaderFont();
		Font fontNormal = createNormalFont();
		
		cell = new PdfPCell(new Phrase("Payment Date", fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Amount", fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Interest Paid", fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Principal Paid", fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Principal Remaining", fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		for (LoanPayment payment : payments) {
			cell = new PdfPCell(new Phrase(FormatterUtil.formatDate(payment.getPaymentDate()), fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(FormatterUtil.formatAmount(payment.getAmount()), fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(FormatterUtil.formatAmount(payment.getInterestPaid()), fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(FormatterUtil.formatAmount(payment.getPrincipalPaid()), fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(FormatterUtil.formatAmount(payment.getPrincipalRemaining()), fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			table.completeRow();
		}
		return table;
	}

	private Font createNormalFont() {
		try {
			BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, 
					BaseFont.WINANSI, BaseFont.EMBEDDED);
			return new Font(baseFont, 10);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Font createHeaderFont() {
		try {
			BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, 
					BaseFont.WINANSI, BaseFont.EMBEDDED);
			return new Font(baseFont, 10);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
