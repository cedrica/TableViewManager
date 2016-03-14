/**
 *
 */
package com.tableview.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class TableViewManager<T> {
	private TableView tableView;
	private TableColumn tableColumn;
	private List<TableColumn> listOfColumnsTitle;
	private boolean isUpdatable;
	private static Logger logger = null;
	private String[] listExcludedColumns = null;

	public TableViewManager(TableView tableView) {
		this.tableView = tableView;
		listOfColumnsTitle = new ArrayList<TableColumn>();
		logger = Logger.getLogger(this.getClass().getSimpleName());
	}

	/**
	 * convert List<T> data into an observableList in order to set Items tableView
	 *
	 * @param data
	 */
	public void addData(List<T> data) {
		ObservableList<T> olTasklist = FXCollections.observableArrayList(data);
		this.tableView.setItems(olTasklist);
	}

	/**
	 * This function initialize table columns which belong to the same Object
	 *
	 * @param columnsTitle
	 * @param entity
	 */
	@SuppressWarnings("all")
	public void initColumnTitleAndAssociateData(Class entity) {
		listOfColumnsTitle = new ArrayList<TableColumn>();
		Field[] attributes = entity.getDeclaredFields();
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (Field att : attributes) {
			tableColumn = new TableColumn(att.getName());
			if (att.getType().isAssignableFrom(String.class)) {
				// associate data to column using setCellValueFactory
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, String>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
				listOfColumnsTitle.add(tableColumn);
			} else if (att.getType().isAssignableFrom(int.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Integer>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.<T, SimpleIntegerProperty> forTableColumn(new IntegerConverter()));
				listOfColumnsTitle.add(tableColumn);
			} else if (att.getType().isAssignableFrom(long.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Long>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.<T, SimpleLongProperty> forTableColumn(new LongConverter()));
				listOfColumnsTitle.add(tableColumn);
			} else if (att.getType().isAssignableFrom(double.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Double>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.<T, Double> forTableColumn(new DoubleConverter()));
				listOfColumnsTitle.add(tableColumn);
			} else if (att.getType().isAssignableFrom(Date.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Date>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.<T, Date> forTableColumn(new DateConverter()));
				listOfColumnsTitle.add(tableColumn);
			}
			sb.append(tableColumn.getText() + " , ");
		}
		sb.toString().trim();
		logger.log(Level.INFO, "Columns initialised [" + sb.toString() + "]");
		this.tableView.getColumns().clear();
		this.tableView.getColumns().setAll(listOfColumnsTitle);
	}

	/**
	 * Assign Event handler to the given Component. The function to be execute by event-handling is method
	 *
	 * @param btn
	 * @param serviceClass
	 * @param method
	 * @author ca.leumaleu
	 */
	private void assignEvent(Object object, Class serviceClass, String method) {
		if (object instanceof Button) {
			((Button) object).setOnAction((event) -> {
				try {
					Object t = serviceClass.newInstance();
					Method[] allMethods = serviceClass.getDeclaredMethods();
					for (Method m : allMethods) {
						String mname = m.getName();
						if (!mname.startsWith(method) || (m.getGenericReturnType() != void.class)) {
							continue;
						}
						m.setAccessible(true);
						Object o = m.invoke(t);
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException x) {
					x.printStackTrace();
				}

			});
		} else if (object instanceof ComboBox) {
			((ComboBox) object).setOnAction((event) -> {
				try {
					Object t = serviceClass.newInstance();
					Method[] allMethods = serviceClass.getDeclaredMethods();
					for (Method m : allMethods) {
						String mname = m.getName();
						if (!mname.startsWith(method) || (m.getGenericReturnType() != void.class)) {
							continue;
						}
						m.setAccessible(true);
						Object o = m.invoke(t);
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException x) {
					x.printStackTrace();
				}

			});
		} else if (object instanceof Label) {
			((Label) object).setOnMouseReleased((event) -> {
				try {
					Object t = serviceClass.newInstance();
					Method[] allMethods = serviceClass.getDeclaredMethods();
					for (Method m : allMethods) {
						String mname = m.getName();
						if (!mname.startsWith(method) || (m.getGenericReturnType() != void.class)) {
							continue;
						}
						m.setAccessible(true);
						Object o = m.invoke(t);
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException x) {
					x.printStackTrace();
				}

			});
		}

	}

	/**
	 * to assign graphics elements to the cell and apply on it the given event method
	 *
	 * @param column
	 * @param componentClass
	 * @param serviceClass
	 * @param method
	 * @param itemListForListComponent
	 *        for example if the component is a Combobox
	 */
	private void assignGraphic(TableColumn column, Class componentClass, Class serviceClass, String method,
					List<String>... itemListForListComponent) {
		column.setCellFactory(new Callback<TableColumn, TableCell>() {
			@Override
			public TableCell call(TableColumn param) {
				TableCell cell = new TableCell() {
					@Override
					public void updateItem(Object item, boolean empty) {
						if (item != null) {
							if (componentClass.isAssignableFrom(Button.class)) {
								// Button has to be declared in the update function in order to be assign to each
								// desired cell
								Button btn = new Button(item.toString());
								btn.setPrefWidth(column.getPrefWidth());
								btn.setPrefHeight(40.0);
								assignEvent(btn, serviceClass, method);
								setGraphic(btn);
							} else if (componentClass.isAssignableFrom(ComboBox.class)) {
								ComboBox<String> cbb = new ComboBox<String>();
								for (List<String> list : itemListForListComponent) {
									cbb.getItems().addAll(list);
								}
								cbb.getSelectionModel().select(0);
								assignEvent(cbb, serviceClass, method);
								setGraphic(cbb);
							} else if (componentClass.isAssignableFrom(Label.class)) {
								Label lab = new Label();
								// assignEvent(lab, serviceClass, method);
								setGraphic(lab);
							} else if (componentClass.isAssignableFrom(CheckBox.class)) {
								CheckBox cb = new CheckBox();
								// assignEvent(cb, serviceClass, method);
								setGraphic(cb);
							} else if (componentClass.isAssignableFrom(TextField.class)) {
								TextField tf = new TextField();
								// assignEvent(tf, serviceClass, method);
								setGraphic(tf);
							} else if (componentClass.isAssignableFrom(RadioButton.class)) {
								RadioButton rb = new RadioButton();
								// assignEvent(rb, serviceClass, method);
								setGraphic(rb);
							}
						}
					}
				};
				return cell;
			}
		});
	}

	/**
	 * render the given column by assigning to its cells a component which Class correspond to the given class
	 * for Combobox Object you first have to
	 *
	 * @param columnName
	 * @param componentClass
	 * @param serviceClass
	 *        use by reflexion to read the Class Methods and execute the function having the same name as method
	 *        argument
	 * @param method
	 * @param itemListForListComponent
	 */
	public void assignCellFactoryToSpecificColumn(String columnName, Class componentClass, Class serviceClass, String method,
					List<String>... itemListForListComponent) {
		for (TableColumn column : listOfColumnsTitle) {
			if (column.getText().equals(columnName)) {
				assignGraphic(column, componentClass, serviceClass, method, itemListForListComponent);
			}
		}
		tableView.getColumns().clear();
		tableView.getColumns().addAll(listOfColumnsTitle);
	}

	public void excludeColumns(Class entityClass, String... columnToBeExclude) {
		listOfColumnsTitle = new ArrayList<TableColumn>();
		Field[] attributes = entityClass.getDeclaredFields();
		int i;
		StringBuilder sb = new StringBuilder();
		List<String> liste = Arrays.asList(columnToBeExclude);
		for (Field att : attributes) {

			i = 0;
				if(!liste.contains(att.getName())){
					tableColumn = new TableColumn(att.getName());

					if (att.getType().isAssignableFrom(String.class) || att.getType().isAssignableFrom(SimpleStringProperty.class)) {
						// associate data to column using setCellValueFactory
						tableColumn.setCellValueFactory(new PropertyValueFactory(attributes[i++].getName()));
//						tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
						listOfColumnsTitle.add(tableColumn);
					} else if (att.getType().isAssignableFrom(int.class) || att.getType().isAssignableFrom(SimpleIntegerProperty.class)) {
						tableColumn.setCellValueFactory(new PropertyValueFactory<T, Integer>(attributes[i++].getName()));
//						tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
						listOfColumnsTitle.add(tableColumn);
					} else if (att.getType().isAssignableFrom(long.class) || att.getType().isAssignableFrom(SimpleLongProperty.class)) {
						tableColumn.setCellValueFactory(new PropertyValueFactory<T, Long>(attributes[i++].getName()));
//						tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
						listOfColumnsTitle.add(tableColumn);
					} else if (att.getType().isAssignableFrom(double.class) || att.getType().isAssignableFrom(SimpleDoubleProperty.class)) {
						tableColumn.setCellValueFactory(new PropertyValueFactory<T, Double>(attributes[i++].getName()));
//						tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
						listOfColumnsTitle.add(tableColumn);
					} else if (att.getType().isAssignableFrom(Date.class) || att.getType().isAssignableFrom(ObjectProperty.class)) {
						tableColumn.setCellValueFactory(new PropertyValueFactory<T, Date>(attributes[i++].getName()));
//						tableColumn.setCellFactory(TextFieldTableCell.<T, Date> forTableColumn(new DateStringConverter()));
						listOfColumnsTitle.add(tableColumn);
					}
					sb.append(tableColumn.getText() + " , ");

				}

		}
		sb.toString().trim();
		logger.log(Level.INFO, "Columns initialised [" + sb.toString() + "]");
		this.tableView.getColumns().clear();
		this.tableView.getColumns().setAll(listOfColumnsTitle);
	}
	/***************************************************************************************************
	 * *************************************************************************************************
	 * Oberefunction sind lauffähig
	 */

	/**
	 * This function just initialize tables columns with given name in the columns name list
	 *
	 * @param columnsTitle
	 */
	public void initColumnTitle(List<String> columnsTitle) {
		int i = 0;
		for (String colname : columnsTitle) {
			tableColumn = new TableColumn(colname);
			listOfColumnsTitle.add(tableColumn);
		}
		this.tableView.getColumns().addAll(listOfColumnsTitle);
	}

	public void setEditable(Class busynessLogic, Class cl) {
		// tableView.getColumns().clear();
		for (TableColumn tableColumn : listOfColumnsTitle) {
			tableColumn.setOnEditCommit(new EventHandler<CellEditEvent>() {
				@Override
				public void handle(CellEditEvent cEE) {
					Object tt;
					try {
						// TO DO get id value using reflexion
						// get Selected Item
						// get Id od selected item using reflexion und das war´s!! oder
						long id = tableColumn.getTableView().getSelectionModel().getSelectedIndex();
						tt = busynessLogic.newInstance();
						Method[] allMethods = busynessLogic.getDeclaredMethods();
						for (Method m : allMethods) {
							String mname = m.getName();
							if (!mname.startsWith("update") || (m.getGenericReturnType() != boolean.class)) {
								continue;
							}
							m.setAccessible(true);
							Object o = m.invoke(tt, cEE.getOldValue(), cEE.getNewValue(), tableColumn.getText(), id);
							isUpdatable = true;
							break;
						}
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						logger.log(Level.SEVERE, e.getMessage());
					}

				}
			});
		}
		if (isUpdatable) {
			Object tt;
			Object o = null;
			try {
				tt = busynessLogic.newInstance();
				Method[] allMethods = busynessLogic.getDeclaredMethods();
				for (Method m : allMethods) {
					String mname = m.getName();
					if (!mname.startsWith("getAllTask") || (m.getGenericReturnType() != List.class)) {
						continue;
					}
					m.setAccessible(true);
					o = m.invoke(tt);
					break;
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, e.getMessage());
			}
			List<T> data = (List<T>) o;
			tableView.getItems().clear();
			addData(data);
		}

		// tableView.getColumns().addAll(listOfColumnsTitle);
	}

	protected class LongConverter extends StringConverter<SimpleLongProperty> {
		protected LongConverter() {

		}

		@Override
		public String toString(SimpleLongProperty object) {
			if (object == null)
				return "";
			return String.valueOf(object);
		}

		@Override
		public SimpleLongProperty fromString(String str_value) {
			if (str_value.trim().length() < 1)
				return null;
			return new SimpleLongProperty(Long.valueOf(str_value));
		}

	}

	protected class IntegerConverter extends StringConverter<SimpleIntegerProperty> {
		protected IntegerConverter() {

		}

		@Override
		public SimpleIntegerProperty fromString(String str_value) {
			if (str_value.trim().length() < 1)
				return null;
			return new SimpleIntegerProperty(Integer.valueOf(str_value));
		}

		@Override
		public String toString(SimpleIntegerProperty int_value) {
			if (int_value == null)
				return "";
			return String.valueOf(int_value);
		}

	}

	protected class DateConverter extends StringConverter<Date> {
		private SimpleDateFormat format;

		protected DateConverter() {
			format = new SimpleDateFormat();
		}

		@Override
		public Date fromString(String str_value) {
			if (str_value.trim().length() < 1)
				return null;
			try {
				return format.parse(str_value);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public String toString(Date date) {
			if (date == null)
				return "";
			return String.valueOf(date);
		}
	}

	protected class DoubleConverter extends StringConverter<Double> {

		protected DoubleConverter() {
		}

		@Override
		public Double fromString(String str_value) {
			if (str_value.trim().length() < 1)
				return null;
			return Double.valueOf(str_value);
		}

		@Override
		public String toString(Double double_value) {
			if (double_value == null)
				return "";
			return String.valueOf(double_value);
		}
	}
}
