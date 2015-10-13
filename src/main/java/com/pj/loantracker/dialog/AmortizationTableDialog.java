package com.pj.loantracker.dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.pj.loantracker.Parameter;
import com.pj.loantracker.gui.component.ShowDialog;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.model.LoanPayment;
import com.pj.loantracker.model.report.AmortizationTableReport;
import com.pj.loantracker.service.ExcelService;
import com.pj.loantracker.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Component
public class AmortizationTableDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(AmortizationTableDialog.class);
	
	@Autowired private ExcelService excelService;
	
	@FXML private TextField fixedMonthlyPaymentField;
	@FXML private TextField fixedMonthlyPaymentToPrincipalField;
	@FXML private TableView<LoanPayment> paymentsTable;
	@FXML private Label totalPaymentsLabel;
	
	@Parameter private Loan loan;
	
	public AmortizationTableDialog() {
		setSceneWidth(1000d);
		setSceneHeight(600d);
	}
	
	@Override
	protected String getDialogTitle() {
		return "Amortization Table";
	}

	@Override
	protected void updateDisplay() {
		fixedMonthlyPaymentField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			fixedMonthlyPaymentToPrincipalField.setText(null);
		});
		fixedMonthlyPaymentToPrincipalField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			fixedMonthlyPaymentField.setText(null);
		});
		
		fixedMonthlyPaymentField.requestFocus();
	}

	@Override
	protected String getSceneName() {
		return "amortizationTable";
	}

	@FXML public void generateAmortizationTable() {
		if (!validateFields()) {
			return;
		}
		
		List<LoanPayment> payments = new ArrayList<>();
		if (isFixedMonthlyPaymentSpecified()) {
			payments = loan.generateFixedMonthlyPaymentAmortizationTable(
					NumberUtil.toBigDecimal(fixedMonthlyPaymentField.getText()));
		} else {
			payments = loan.generateFixedMonthlyPaymentToPrincipalAmortizationTable(
					NumberUtil.toBigDecimal(fixedMonthlyPaymentToPrincipalField.getText()));
		}
		
		paymentsTable.getItems().clear();
		paymentsTable.getItems().addAll(payments);
		totalPaymentsLabel.setText(String.valueOf(payments.size()));
	}

	private boolean isFixedMonthlyPaymentSpecified() {
		return !StringUtils.isEmpty(fixedMonthlyPaymentField.getText());
	}

	private boolean validateFields() {
		if (isNoMonthlyPaymentFieldSpecified()) {
			ShowDialog.error("Either Fixed Monthly Payment or Fixed Monthly Payment to Principal must be specified");
			fixedMonthlyPaymentField.requestFocus();
			return false;
		}
		
		if (isMonthlyPaymentFieldSpecified() && !NumberUtil.isAmount(fixedMonthlyPaymentField.getText())) {
			ShowDialog.error("Fixed Monthly Payment must be a valid amount");
			fixedMonthlyPaymentField.requestFocus();
			return false;
		}
		
		if (isMonthlyPaymentToPrincipalFieldSpecified() 
				&& !NumberUtil.isAmount(fixedMonthlyPaymentToPrincipalField.getText())) {
			ShowDialog.error("Fixed Monthly Payment to Principal must be a valid amount");
			fixedMonthlyPaymentToPrincipalField.requestFocus();
			return false;
		}
		
		// TODO: Check if monthly payment greater than interest
		
		return true;
	}

	private boolean isMonthlyPaymentFieldSpecified() {
		return !StringUtils.isEmpty(fixedMonthlyPaymentField.getText());
	}

	private boolean isMonthlyPaymentToPrincipalFieldSpecified() {
		return !StringUtils.isEmpty(fixedMonthlyPaymentToPrincipalField.getText());
	}

	private boolean isNoMonthlyPaymentFieldSpecified() {
		return !isMonthlyPaymentFieldSpecified() && !isMonthlyPaymentToPrincipalFieldSpecified();
	}

	@FXML public void generateAmortizationTableAsExcel() {
		if (!validateFields()) {
			return;
		}

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "Desktop").toFile());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel files", "*.xlsx"));
        fileChooser.setInitialFileName(getExcelFilename(loan));
        File file = fileChooser.showSaveDialog(this);
        if (file == null) {
        	return;
        }
		
		paymentsTable.getItems().clear();
		
		AmortizationTableReport report = new AmortizationTableReport();
		report.setLoan(loan);
		report.setPayments(generateLoanPayments());
		report.setFixedMonthlyPaymentToPrincipal(isMonthlyPaymentToPrincipalFieldSpecified());
		
		try (
			Workbook workbook = excelService.generate(report);
			FileOutputStream out = new FileOutputStream(file);
		) {
			workbook.write(out);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
		}
	}
	
	private String getExcelFilename(Loan loan) {
		return loan.getClient().getName() + " - amortization table.xlsx";
	}

	private List<LoanPayment> generateLoanPayments() {
		if (isFixedMonthlyPaymentSpecified()) {
			return loan.generateFixedMonthlyPaymentAmortizationTable(
					NumberUtil.toBigDecimal(fixedMonthlyPaymentField.getText()));
		} else {
			return loan.generateFixedMonthlyPaymentToPrincipalAmortizationTable(
					NumberUtil.toBigDecimal(fixedMonthlyPaymentToPrincipalField.getText()));
		}
	}
	
}
