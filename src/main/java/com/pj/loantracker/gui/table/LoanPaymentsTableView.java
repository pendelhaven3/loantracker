package com.pj.loantracker.gui.table;

import com.pj.loantracker.gui.component.StringCellValueFactory;
import com.pj.loantracker.model.LoanPayment;
import com.pj.loantracker.util.FormatterUtil;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LoanPaymentsTableView extends TableView<LoanPayment> {

	private boolean showCheckColumn = true;
	
	public LoanPaymentsTableView() {
		initializeColumns();
	}

	public void initializeColumns() {
		getColumns().clear();
		
		TableColumn<LoanPayment, String> paymentDateColumn = new TableColumn<>("Payment Date");
		paymentDateColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return FormatterUtil.formatDate(item.getPaymentDate());
			}
		});
		paymentDateColumn.getStyleClass().add("center");

		TableColumn<LoanPayment, String> bankColumn = new TableColumn<>("Bank");
		bankColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return item.getBank();
			}
		});
		bankColumn.getStyleClass().add("center");

		TableColumn<LoanPayment, String> checkNumberColumn = new TableColumn<>("Check Number");
		checkNumberColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return item.getCheckNumber();
			}
		});

		TableColumn<LoanPayment, String> amountColumn = new TableColumn<>("Amount");
		amountColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return FormatterUtil.formatAmount(item.getAmount());
			}
		});
		amountColumn.getStyleClass().add("right");

		TableColumn<LoanPayment, String> interestColumn = new TableColumn<>("Interest");
		interestColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return FormatterUtil.formatAmount(item.getInterest());
			}
		});
		interestColumn.getStyleClass().add("right");

		TableColumn<LoanPayment, String> interestPaidColumn = new TableColumn<>("Interest Paid");
		interestPaidColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return FormatterUtil.formatAmount(item.getInterestPaid());
			}
		});
		interestPaidColumn.getStyleClass().add("right");

		TableColumn<LoanPayment, String> pricipalPaidColumn = new TableColumn<>("Principal Paid");
		pricipalPaidColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return FormatterUtil.formatAmount(item.getPrincipalPaid());
			}
		});
		pricipalPaidColumn.getStyleClass().add("right");

		TableColumn<LoanPayment, String> pricipalRemainingColumn = new TableColumn<>("Principal Remaining");
		pricipalRemainingColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return FormatterUtil.formatAmount(item.getPrincipalRemaining());
			}
		});
		pricipalRemainingColumn.getStyleClass().add("right");

		getColumns().add(paymentDateColumn);
		if (showCheckColumn) {
			getColumns().add(bankColumn);
			getColumns().add(checkNumberColumn);
		}
		getColumns().add(amountColumn);
		getColumns().add(interestColumn);
		getColumns().add(interestPaidColumn);
		getColumns().add(pricipalPaidColumn);
		getColumns().add(pricipalRemainingColumn);
	}
	
	public void setShowCheckColumn(boolean showCheckColumn) {
		this.showCheckColumn = showCheckColumn;
	}
	
}
