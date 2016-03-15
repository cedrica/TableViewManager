package com.tableview.manager;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import javafx.util.converter.NumberStringConverter;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;


import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

@SuppressWarnings("all")
public class FxTableViewExample2 extends Application {

	private TableView tableBook;
	private TableView tableTask;
	private ObservableList<Book> dataB;
	private ObservableList<Task> dataT;
	private Text actionStatus;
	private List<TableColumn> listOfColumnsTitle;

	public static void main(String[] args) {

		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
//		AppControl.initApplicationStartUpControl();
//
//		try {
//
//			AppControl.getInst().initialize(AppControl.getInst(), a -> {
//				 try {
//					a.initDataBase("");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//
//		} catch (Exception ex) {
//
//		}
		primaryStage.setTitle("Table View Example 2");
		listOfColumnsTitle = new ArrayList<TableColumn>();
		// Books label
		Label label = new Label("Books");
		label.setTextFill(Color.DARKBLUE);
		label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
		HBox labelHb = new HBox();
		labelHb.setAlignment(Pos.CENTER);
		labelHb.getChildren().add(label);

		// Table view, data, columns and properties
		tableBook = new TableView<>();
		dataB = getInitialTableBookData();
		tableBook.setItems(dataB);
		tableBook.setEditable(true);

		TableColumn titleCol = new TableColumn("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
		titleCol.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
			@Override
			public void handle(CellEditEvent<Book, String> t) {

				((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTitle(t.getNewValue());
			}
		});

		TableColumn authorCol = new TableColumn("Author");
		authorCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("author"));
		authorCol.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		authorCol.setOnEditCommit(new EventHandler<CellEditEvent<Book, Integer>>() {
			@Override
			public void handle(CellEditEvent<Book, Integer> t) {

				((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAuthor(t.getNewValue());
			}
		});
		TableColumn me = new TableColumn("Id");
		me.setCellValueFactory(new PropertyValueFactory<Book, Long>("id"));
		me.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		me.setOnEditCommit(new EventHandler<CellEditEvent<Book, Long>>() {
			@Override
			public void handle(CellEditEvent<Book, Long> t) {

				((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setId(t.getNewValue());
			}
		});
		// listOfColumnsTitle.add(me);
		// listOfColumnsTitle.add(titleCol);
		// listOfColumnsTitle.add(authorCol);

		tableBook.getColumns().setAll(listOfColumnsTitle);

		// Table view, data, columns and properties
		tableTask = new TableView<>();
		dataT = getInitialTableTaskData();
		// tableTask.setItems(dataT);
		tableTask.setEditable(true);

		Field[] attributes = Task.class.getDeclaredFields();
		int i;
		StringBuilder sb = new StringBuilder();
		i = 0;
		TableColumn tableColumn = null;
		for (Field att : attributes) {
			tableColumn = new TableColumn(att.getName());
			if (att.getType().isAssignableFrom(String.class)
					|| att.getType().isAssignableFrom(SimpleStringProperty.class)) {
				// associate data to column using setCellValueFactory
				tableColumn.setCellValueFactory(new PropertyValueFactory<Task, String>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
				listOfColumnsTitle.add(tableColumn);
			} else if (att.getType().isAssignableFrom(int.class)
					|| att.getType().isAssignableFrom(SimpleIntegerProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<Task, Integer>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
				listOfColumnsTitle.add(tableColumn);
			} else if (att.getType().isAssignableFrom(long.class)
					|| att.getType().isAssignableFrom(SimpleLongProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<Task, Long>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
				// listOfColumnsTitle.add(tableColumn);
			} else if (att.getType().isAssignableFrom(double.class)
					|| att.getType().isAssignableFrom(SimpleDoubleProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<Task, Double>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
				// listOfColumnsTitle.add(tableColumn);
			} else if (att.getType().isAssignableFrom(Date.class)
					|| att.getType().isAssignableFrom(ObjectProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<Task, Date>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
				// listOfColumnsTitle.add(tableColumn);
			}
			sb.append(tableColumn.getText() + " , ");
			System.out.println("attr " + i + " = " + att.getName());
		}

		tableTask.getColumns().clear();
		// tableTask.getColumns().setAll(listOfColumnsTitle);
		tableTask.setPrefWidth(450);
		tableTask.setPrefHeight(300);
		tableTask.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableViewManager<Task> tableViewManager = new TableViewManager<Task>(tableTask);
//		tableViewManager.excludeColumns(Task.class, "id", "name");
		tableViewManager.initColumnTitleAndAssociateData(Task.class);
		tableViewManager.assignCellFactoryToSpecificColumn("name", ComboBox.class, Service.class, "print");
		tableViewManager.addData(dataT);
		// table.getSelectionModel().selectedIndexProperty().addListener(
		// new RowSelectChangeListener());

		// Add and delete buttons
		Button addbtn = new Button("Add");
		// addbtn.setOnAction(new AddButtonListener());
		Button delbtn = new Button("Delete");
		// delbtn.setOnAction(new DeleteButtonListener());
		HBox buttonHb = new HBox(10);
		buttonHb.setAlignment(Pos.CENTER);
		buttonHb.getChildren().addAll(addbtn, delbtn);

		// Status message text
		actionStatus = new Text();
		actionStatus.setFill(Color.FIREBRICK);

		// Vbox
		VBox vbox = new VBox(20);
		vbox.setPadding(new Insets(25, 25, 25, 25));
		;
		vbox.getChildren().addAll(labelHb, tableTask, buttonHb, actionStatus);

		// Scene
		Scene scene = new Scene(vbox, 500, 550); // w x h
		primaryStage.setScene(scene);
		primaryStage.show();

		// Select the first row
		tableBook.getSelectionModel().select(0);
		// Book book = table.getSelectionModel().getSelectedItem();
		// actionStatus.setText(book.toString());

	} // start()

	// private class RowSelectChangeListener implements ChangeListener<Number> {
	//
	// @Override
	// public void changed(ObservableValue<? extends Number> ov,
	// Number oldVal, Number newVal) {
	//
	// int ix = newVal.intValue();
	//
	// if ((ix < 0) || (ix >= data.size())) {
	//
	// return; // invalid data
	// }
	//
	// Book book = data.get(ix);
	// actionStatus.setText(book.toString());
	// }
	// }

	private ObservableList<Book> getInitialTableBookData() {
		List<Book> list = new ArrayList<Book>();
		list.add(new Book(2, "Bondage", 22));
		list.add(new Book(2, "Bondage", 22));
		ObservableList<Book> data = FXCollections.observableList(list);
		for (Book task : data) {
			System.out.print(task.toString() + "\n");
		}
		return data;
	}

	private ObservableList<Task> getInitialTableTaskData() {

		List<String> al = new ArrayList<String>();
		// al.add("start_date");
		// al.add("end_date");
		// List<Task> list = TaskService.exclude(al);//new ArrayList<>();
		List<Task> list = new ArrayList<Task>();

		list.add(new Task(1, "name1", "1", new Date(), new Date(), 1, 1, 1));
		list.add(new Task(2, "name2", "2", new Date(), new Date(), 2, 2, 2));
		list.add(new Task(3, "name3", "3", new Date(), new Date(), 3, 3, 3));

		ObservableList<Task> data = FXCollections.observableList(list);
		for (Task task : data) {
			System.out.print(task.toString() + "\n");
		}
		return data;
	}

	// private class AddButtonListener implements EventHandler<ActionEvent> {
	//
	// @Override
	// public void handle(ActionEvent e) {
	//
	// // Create a new row after last row
	// Book book = new Book("...", 3);
	// data.add(book);
	// int row = data.size() - 1;
	//
	// // Select the new row
	// table.requestFocus();
	// table.getSelectionModel().select(row);
	// table.getFocusModel().focus(row);
	//
	// actionStatus.setText("New book: Enter title and author. Press <Enter>.");
	// }
	// }

	// private class DeleteButtonListener implements EventHandler<ActionEvent> {
	//
	// @Override
	// public void handle(ActionEvent e) {
	//
	// // Get selected row and delete
	// int ix = table.getSelectionModel().getSelectedIndex();
	// Book book = (Book) table.getSelectionModel().getSelectedItem();
	// data.remove(ix);
	// actionStatus.setText("Deleted: " + book.toString());
	//
	// // Select a row
	//
	// if (table.getItems().size() == 0) {
	//
	// actionStatus.setText("No data in table !");
	// return;
	// }
	//
	// if (ix != 0) {
	//
	// ix = ix -1;
	// }
	//
	// table.requestFocus();
	// table.getSelectionModel().select(ix);
	// table.getFocusModel().focus(ix);
	// }
	// }
	//

}
