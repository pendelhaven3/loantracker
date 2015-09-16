package com.pj.loantracker.gui.table;

import com.pj.loantracker.gui.component.StringCellValueFactory;
import com.pj.loantracker.model.LoanPayment;
import com.pj.loantracker.util.FormatterUtil;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LoanPaymentsTableView extends TableView<LoanPayment> {

	public LoanPaymentsTableView() {
		initializeColumns();
	}

	private void initializeColumns() {
		TableColumn<LoanPayment, String> paymentDateColumn = new TableColumn<>("Payment Date");
		paymentDateColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return FormatterUtil.formatDate(item.getPaymentDate());
			}
		});
		paymentDateColumn.getStyleClass().add("center");

		TableColumn<LoanPayment, String> amountColumn = new TableColumn<>("Amount");
		amountColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return FormatterUtil.formatAmount(item.getAmount());
			}
		});
		amountColumn.getStyleClass().add("right");

		TableColumn<LoanPayment, String> interestPaidColumn = new TableColumn<>("Interest Paid");
		interestPaidColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return "";
			}
		});
		interestPaidColumn.getStyleClass().add("right");

		TableColumn<LoanPayment, String> pricipalPaidColumn = new TableColumn<>("Principal Paid");
		pricipalPaidColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return "";
			}
		});
		pricipalPaidColumn.getStyleClass().add("right");

		TableColumn<LoanPayment, String> pricipalRemainingColumn = new TableColumn<>("Principal Pemaining");
		pricipalRemainingColumn.setCellValueFactory(new StringCellValueFactory<LoanPayment>() {

			@Override
			protected String getValue(LoanPayment item) {
				return "";
			}
		});
		pricipalRemainingColumn.getStyleClass().add("right");

		getColumns().add(paymentDateColumn);
		getColumns().add(amountColumn);
		getColumns().add(interestPaidColumn);
		getColumns().add(pricipalPaidColumn);
		getColumns().add(pricipalRemainingColumn);
	}
	
}
