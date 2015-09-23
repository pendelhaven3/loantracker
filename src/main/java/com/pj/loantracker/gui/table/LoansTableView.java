package com.pj.loantracker.gui.table;

import com.pj.loantracker.gui.component.StringCellValueFactory;
import com.pj.loantracker.model.Loan;
import com.pj.loantracker.util.FormatterUtil;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LoansTableView extends TableView<Loan> {

	public LoansTableView() {
		initializeColumns();
	}

	private void initializeColumns() {
		TableColumn<Loan, String> clientColumn = new TableColumn<>("Client");
		clientColumn.setCellValueFactory(new StringCellValueFactory<Loan>() {

			@Override
			protected String getValue(Loan item) {
				return item.getClient().getName();
			}
		});

		TableColumn<Loan, String> amountColumn = new TableColumn<>("Amount");
		amountColumn.setCellValueFactory(new StringCellValueFactory<Loan>() {

			@Override
			protected String getValue(Loan item) {
				return FormatterUtil.formatAmount(item.getAmount());
			}
		});
		amountColumn.getStyleClass().add("right");

		TableColumn<Loan, String> interestRateColumn = new TableColumn<>("Interest Rate (%)");
		interestRateColumn.setCellValueFactory(new StringCellValueFactory<Loan>() {

			@Override
			protected String getValue(Loan item) {
				return FormatterUtil.formatAmount(item.getInterestRate());
			}
		});
		interestRateColumn.getStyleClass().add("right");

		TableColumn<Loan, String> loanDateColumn = new TableColumn<>("Loan Date");
		loanDateColumn.setCellValueFactory(new StringCellValueFactory<Loan>() {

			@Override
			protected String getValue(Loan item) {
				return FormatterUtil.formatDate(item.getLoanDate());
			}
		});
		loanDateColumn.getStyleClass().add("center");
		
		TableColumn<Loan, String> typeColumn = new TableColumn<>("Type");
		typeColumn.setCellValueFactory(new StringCellValueFactory<Loan>() {

			@Override
			protected String getValue(Loan item) {
				return item.getType().toString();
			}
		});
		typeColumn.getStyleClass().add("center");

		TableColumn<Loan, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new StringCellValueFactory<Loan>() {

			@Override
			protected String getValue(Loan item) {
				return item.getStatus();
			}
		});
		statusColumn.getStyleClass().add("center");

		getColumns().add(clientColumn);
		getColumns().add(amountColumn);
		getColumns().add(interestRateColumn);
		getColumns().add(loanDateColumn);
		getColumns().add(typeColumn);
		getColumns().add(statusColumn);
	}
	
}
