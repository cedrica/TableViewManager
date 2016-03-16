/**
 *
 */
package com.tableview.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.javafx.scene.layout.region.Margins.Converter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

@SuppressWarnings("all")
public class TableViewManager<T> implements ITableViewManager<T> {
	private TableView tableView;
	private TableColumnHelper tableColumn;
	private List<TableColumnHelper> listOfColumns;
	private boolean isUpdatable;
	private static Logger logger = null;
	private String[] listExcludedColumns = null;

	public TableViewManager(TableView tableView) {
		this.tableView = tableView;
		listOfColumns = new ArrayList<TableColumnHelper>();
		logger = Logger.getLogger(this.getClass().getSimpleName());
	}

	/**
	 * convert List<T> data into an observableList in order to set Items
	 * tableView
	 *
	 * @param data
	 */
	public void addData(List<T> data) {
		ObservableList<T> ol = FXCollections.observableArrayList(data);
		this.tableView.setItems(ol);
	}

	/**
	 * This function initialize table columns of a POJO.
	 *
	 * @param columnsTitle
	 * @param entity
	 */
	public void initColumnSetValueFactory(Class entity) {
		listOfColumns = new ArrayList<TableColumnHelper>();
		Field[] attributes = entity.getDeclaredFields();
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (Field att : attributes) {
			tableColumn = new TableColumnHelper(att.getName());
			if (att.getType().isAssignableFrom(String.class)
					|| att.getType().isAssignableFrom(SimpleStringProperty.class)) {
				// associate data to column using setCellValueFactory
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, String>(attributes[i++].getName()));
				tableColumn.setConverterClazz(StringConverter.class);
				listOfColumns.add(tableColumn);
			} else if (att.getType().isAssignableFrom(Integer.class)
					|| att.getType().isAssignableFrom(SimpleIntegerProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Integer>(attributes[i++].getName()));
				tableColumn.setConverterClazz(IntegerStringConverter.class);
				listOfColumns.add(tableColumn);
			} else if (att.getType().isAssignableFrom(Long.class)
					|| att.getType().isAssignableFrom(SimpleLongProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Long>(attributes[i++].getName()));
				tableColumn.setConverterClazz(LongStringConverter.class);
				listOfColumns.add(tableColumn);
			} else if (att.getType().isAssignableFrom(Double.class)
					|| att.getType().isAssignableFrom(SimpleDoubleProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Double>(attributes[i++].getName()));
				tableColumn.setConverterClazz(DoubleStringConverter.class);
				listOfColumns.add(tableColumn);
			} else if (att.getType().isAssignableFrom(Date.class)
					|| att.getType().isAssignableFrom(ObjectProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Date>(attributes[i++].getName()));
				tableColumn.setConverterClazz(DateStringConverter.class);
				listOfColumns.add(tableColumn);
			} else if (att.getType().isAssignableFrom(Float.class)
					|| att.getType().isAssignableFrom(SimpleFloatProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Float>(attributes[i++].getName()));
				tableColumn.setConverterClazz(FloatStringConverter.class);
				listOfColumns.add(tableColumn);
			}
			sb.append(tableColumn.getText() + " , ");
		}
		sb.toString().trim();
		logger.log(Level.INFO, "Columns initialised [" + sb.toString() + "]");
		this.tableView.getColumns().clear();
		this.tableView.getColumns().setAll(listOfColumns);
	}

	/**
	 * Assign Event handler to the given Component. The function to be execute
	 * by event-handling is method
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
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException x) {
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
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException x) {
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
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException x) {
					x.printStackTrace();
				}

			});
		}

	}

	/**
	 * to assign graphics elements to the cell and apply on it the given event
	 * method
	 *
	 * @param column
	 * @param componentClass
	 * @param serviceClass
	 * @param method
	 * @param itemListForListComponent
	 *            for example if the component is a Combobox
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
								// Button has to be declared in the update
								// function in order to be assign to each
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
	 * render the given column by assigning to its cells a component which Class
	 * correspond to the given class for Combobox Object you first have to
	 *
	 * @param columnName
	 * @param componentClass:
	 *            the class of the components category you want to display in
	 *            the table i.e. Button.class
	 * @param serviceClass:
	 *            use by reflexion to execute the function having the same name
	 *            as method argument
	 * @param method
	 * @param itemListForListComponent:
	 *            i.e. combobox does not have a other Eventhandler as Button
	 */
	public void assignCellFactoryToSpecificColumn(String columnName, Class componentClass, Class serviceClass,
			String method, List<String>... itemListForListComponent) {
		for (TableColumn column : listOfColumns) {
			if (column.getText().equals(columnName)) {
				assignGraphic(column, componentClass, serviceClass, method, itemListForListComponent);
			}
		}
		tableView.getColumns().clear();
		tableView.getColumns().addAll(listOfColumns);
	}

	/**
	 * hide the given columns
	 *
	 * @param entityClass:
	 *            Object Class
	 * @param columnToBeExclude
	 */
	public void excludeColumns(Class entityClass, String... columnToBeExclude) {
		listOfColumns = new ArrayList<TableColumnHelper>();
		Field[] attributes = entityClass.getDeclaredFields();
		int i;
		StringBuilder sb = new StringBuilder();
		List<String> listeExclude = Arrays.asList(columnToBeExclude);
		i = 0;
		for (Field att : attributes) {
			tableColumn = new TableColumnHelper(att.getName());
			if (att.getType().isAssignableFrom(String.class)
					|| att.getType().isAssignableFrom(SimpleStringProperty.class)) {
				// associate data to column using setCellValueFactory
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, String>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
				if (!listeExclude.contains(att.getName())) {
					listOfColumns.add(tableColumn);
				}
			} else if (att.getType().isAssignableFrom(int.class)
					|| att.getType().isAssignableFrom(SimpleIntegerProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Integer>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
				if (!listeExclude.contains(att.getName())) {
					listOfColumns.add(tableColumn);
				}
			} else if (att.getType().isAssignableFrom(long.class)
					|| att.getType().isAssignableFrom(SimpleLongProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Long>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
				if (!listeExclude.contains(att.getName())) {
					listOfColumns.add(tableColumn);
				}
			} else if (att.getType().isAssignableFrom(double.class)
					|| att.getType().isAssignableFrom(SimpleDoubleProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Double>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
				if (!listeExclude.contains(att.getName())) {
					listOfColumns.add(tableColumn);
				}
			} else if (att.getType().isAssignableFrom(float.class)
					|| att.getType().isAssignableFrom(SimpleFloatProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Float>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
				if (!listeExclude.contains(att.getName())) {
					listOfColumns.add(tableColumn);
				}
			} else if (att.getType().isAssignableFrom(Date.class)
					|| att.getType().isAssignableFrom(ObjectProperty.class)) {
				tableColumn.setCellValueFactory(new PropertyValueFactory<T, Date>(attributes[i++].getName()));
				tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
				if (!listeExclude.contains(att.getName())) {
					listOfColumns.add(tableColumn);
				}
			}
			sb.append(tableColumn.getText() + " , ");
			System.out.println("attr " + i + " = " + att.getName());
		}
		sb.toString().trim();
		logger.log(Level.INFO, "Columns initialised [" + sb.toString() + "]");
		this.tableView.getColumns().clear();
		this.tableView.getColumns().setAll(listOfColumns);
	}
	/*
	 *
	 */

	/**
	 * the function call the given update/edit method located in the given
	 * busynesslogic class after an edit action is done
	 *
	 * @param busynessLogic
	 * @param clazz
	 * @author ca.leumaleu
	 */
	public void performEditActionOnCell(Class busynessLogic, Class clazz, String method) {

	}

	@Override
	public void addContextMenuToColumn(String column, Class<T> clazz) {
	}

	/**
	 *
	 * @author ca.leumaleu
	 */
	@Override
	public void applyCellFactoryTo(TableColumnHelper tabColumn, CellFactoryTyp cellFactoryTyp, Class converterClazz) {
		if (cellFactoryTyp == CellFactoryTyp.CHECKBOXTABLECELL) {
			tabColumn.setCellFactory(column -> {
				return new CheckBoxTableCell();
			});
		} else if (cellFactoryTyp == CellFactoryTyp.COMBOBOXTABLECELL) {
			tabColumn.setCellFactory(column -> {
				if (converterClazz.isAssignableFrom(StringConverter.class)) {
					return new ComboBoxTableCell();
				} else if (converterClazz.isAssignableFrom(DateStringConverter.class)) {
					return new ComboBoxTableCell(new DateStringConverter());
				} else if (converterClazz.isAssignableFrom(LongStringConverter.class)) {
					return new ComboBoxTableCell(new LongStringConverter());
				} else if (converterClazz.isAssignableFrom(IntegerStringConverter.class)) {
					return new ComboBoxTableCell(new IntegerStringConverter());
				} else if (converterClazz.isAssignableFrom(DoubleStringConverter.class)) {
					return new ComboBoxTableCell(new DoubleStringConverter());
				} else if (converterClazz.isAssignableFrom(FloatStringConverter.class)) {
					return new ComboBoxTableCell(new FloatStringConverter());
				}
				return null;
			});
		} else if (cellFactoryTyp == CellFactoryTyp.TEXTFIELDTABLECELL) {
			tabColumn.setCellFactory(column -> {
				if (converterClazz.isAssignableFrom(StringConverter.class)) {
					return new TextFieldTableCell();
				} else if (converterClazz.isAssignableFrom(DateStringConverter.class)) {
					return new TextFieldTableCell(new DateStringConverter());
				} else if (converterClazz.isAssignableFrom(LongStringConverter.class)) {
					return new TextFieldTableCell(new LongStringConverter());
				} else if (converterClazz.isAssignableFrom(IntegerStringConverter.class)) {
					return new TextFieldTableCell(new IntegerStringConverter());
				} else if (converterClazz.isAssignableFrom(DoubleStringConverter.class)) {
					return new TextFieldTableCell(new DoubleStringConverter());
				} else if (converterClazz.isAssignableFrom(FloatStringConverter.class)) {
					return new TextFieldTableCell(new FloatStringConverter());
				}
				return null;
			});
		}
	}

	/**
	 * apply the desired factory on the given columns
	 * @param cellFactoryTyp: enum mentioning which kind of component will be set as Factory
	 * there are just tree components category in javafx which can be assign as factory to TableCell.
	 * CheckboxTableCell, TextFieldTableCell and ComboboxTableCell
	 * @param columns: list of columns to apply the factory on
	 * @author ca.leumaleu
	 */
	@Override
	public void applyCellFactoryTo(CellFactoryTyp cellFactoryTyp, String... columns) {
		if (columns.length <= 0 || (columns.length == 1 && columns[0].equals("ALL"))) {
			for (TableColumnHelper tableColumnHelper : listOfColumns) {
				applyCellFactoryTo(tableColumnHelper, cellFactoryTyp, tableColumnHelper.getConverterClazz());
			}
		} else if (columns.length >= 1) {
			List<String> colList = Arrays.asList(columns);
			for (TableColumnHelper tableColumnHelper : listOfColumns) {
				if (colList.contains(tableColumnHelper.getText())) {
					applyCellFactoryTo(tableColumnHelper, cellFactoryTyp, tableColumnHelper.getConverterClazz());
				}
			}
		}
	}

}
