/**
 *this class was implemented to simplify the initialization and the setting of TableView.
 *The Class is of course an open source to be improved as desired. that is the reason why it implements a Interface.
 *please enter other functions in the interface that you mint they could be useful for the customizing or setting of
 *TableView.
 *In Order to initialize and render your table using TableViewManager, two main steps are supposed to be done first.
 *1. You first have to initialize a TableViewManager Object and assign to its construct the defined table.
 *2. then you have to call the initColumnSetValueFactory() function to initialize the columns of the table
 *Just after this two steps you can start using oder functions.
 *Some function are not stable that is the reason why they are annotated as deprecate. Some other are just meant to be use for
 *special case that is why please first read the description before using the function.
 *
 *@author ca.leumaleu
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
import javafx.scene.Node;
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
	 * convert a list of data into an observableList in order to set the
	 * tableViews Items
	 *
	 * @param data
	 */
	public void addData(List<T> data) {
		ObservableList<T> ol = FXCollections.observableArrayList(data);
		this.tableView.setItems(ol);
	}

	/**
	 * This function initialize table columns of a POJO. Here the columns name
	 * are represented by the name of the attributes of the POJO
	 *
	 * @param entityClazz:
	 *            the class name of the PoJo
	 */
	public void initColumnSetValueFactory(Class entityClazz) {
		listOfColumns = new ArrayList<TableColumnHelper>();
		Field[] attributes = entityClazz.getDeclaredFields();
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
	 * @deprecated: the method is not completely implemented Assign an Event
	 *              handler to the given Component depending on they type.
	 *              button --> setOnAction Event Combo-, Radio-, CheckBox -->
	 *              setOnChange Event TextField --> onKeyReleased Event
	 *
	 * @param component:
	 *            component on which the event is applied
	 * @param serviceClass:
	 *            Class where the method is implement in
	 * @param method:
	 *            The function to be execute by event-handling
	 * @author ca.leumaleu
	 */
	private void assignEvent(Object component, Class serviceClass, String method) {
		if (component instanceof Button) {
			((Button) component).setOnAction((event) -> {
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
		} else if (component instanceof ComboBox) {
			((ComboBox) component).setOnAction((event) -> {
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
		} else if (component instanceof Label) {
			((Label) component).setOnMouseReleased((event) -> {
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
	 * to assign a graphic component to a cell
	 *
	 * @deprecated: this method is not completely implemented
	 * @param column
	 * @param componentClass:
	 *            entity Class
	 * @param serviceClass:
	 *            to get via reflexion the service Class where the method is
	 *            implemented in
	 * @param method:
	 *            to be call by the Event handler
	 * @param itemListForListComponent
	 *            for example if the component is a Combobox
	 */
	private void applyGraphic(TableColumnHelper column, Node node, String name) {
		column.setCellFactory(new Callback<TableColumn, TableCell>() {
			@Override
			public TableCell call(TableColumn param) {
				TableCell cell = new TableCell() {
					@Override
					public void updateItem(Object item, boolean empty) {
						if (item != null) {
							setGraphic(node);
							setText(name);
						}
					}
				};
				return cell;
			}
		});
	}

	/**
	 * render the given column by assigning a component which Class correspond
	 * to the given class.
	 *
	 * @deprecated: method not completely implemented
	 * @param columnsName:
	 *            array containing the columns name. If "ALL" is given then
	 *            Graphic will be assign on all column.
	 * @param componentClass:
	 *            the class of the components category you want to display in
	 *            the table
	 * @param serviceClass:
	 *            use to access via reflexion the method insides which will be
	 *            execute when the Event will be fired.
	 * @param method
	 * @param itemListForListComponent:
	 *            i.e. combobox does not have a other Eventhandler as Button
	 */
	public void assignGraphicToColumn(Node node, String... columnsName) {
		List<String> columnsnameList = Arrays.asList(columnsName);
		int i = 0;
		for (TableColumnHelper column : listOfColumns) {
			if (columnsnameList.contains(column.getText())) {
				applyGraphic(column, node, "ss");
			}
		}
		tableView.getColumns().clear();
		tableView.getColumns().addAll(listOfColumns);
	}

	/**
	 * hide the given columns. Muss be call before the
	 * assignCellFactoryToSpecificColumn function.
	 *
	 * @param entityClass:
	 *            Object Class
	 * @param columnToBeExclude
	 */
	public void excludeColumns(Class entityClass, String... columnToBeExclude) {
		List<TableColumnHelper> newListOfColumns = new ArrayList<TableColumnHelper>();
		List<String> listeExclude = Arrays.asList(columnToBeExclude);
		StringBuilder sb = new StringBuilder();
		sb.append("tables columns are now [");
		for (TableColumnHelper tableColumnHelper : listOfColumns) {
			if (!listeExclude.contains(tableColumnHelper.getText())) {
				newListOfColumns.add(tableColumnHelper);
				sb.append(tableColumnHelper.getText() + " ");
			}
		}
		sb.append("]");
		sb.toString().trim();
		logger.log(Level.INFO, sb.toString().trim());
		this.tableView.getColumns().removeAll(listOfColumns);
		this.tableView.getColumns().setAll(newListOfColumns);
	}
	/*
	 *
	 */

	@Override
	public void addContextMenuToColumn(String column, Class<T> clazz) {
	}


	private void applyCellFactoryTo(TableColumnHelper tabColumn, CellFactoryTyp cellFactoryTyp,
			List<T> listItemsForCombobox, Class converterClazz) {
		if (cellFactoryTyp == CellFactoryTyp.CHECKBOXTABLECELL) {
			tabColumn.setCellFactory(column -> {
				return new CheckBoxTableCell();
			});
		} else if (cellFactoryTyp == CellFactoryTyp.COMBOBOXTABLECELL) {
			if (listItemsForCombobox != null && listItemsForCombobox.size() > 0) {
				ObservableList<T> ol = FXCollections.observableArrayList(listItemsForCombobox);
				tabColumn.setCellFactory(column -> {
					if (converterClazz.isAssignableFrom(StringConverter.class)) {
						return new ComboBoxTableCell(ol);
					} else if (converterClazz.isAssignableFrom(DateStringConverter.class)) {
						return new ComboBoxTableCell(new DateStringConverter(), ol);
					} else if (converterClazz.isAssignableFrom(LongStringConverter.class)) {
						return new ComboBoxTableCell(new LongStringConverter(), ol);
					} else if (converterClazz.isAssignableFrom(IntegerStringConverter.class)) {
						return new ComboBoxTableCell(new IntegerStringConverter(), ol);
					} else if (converterClazz.isAssignableFrom(DoubleStringConverter.class)) {
						return new ComboBoxTableCell(new DoubleStringConverter(), ol);
					} else if (converterClazz.isAssignableFrom(FloatStringConverter.class)) {
						return new ComboBoxTableCell(new FloatStringConverter(), ol);
					}
					return null;
				});
			}
		} else if (cellFactoryTyp == CellFactoryTyp.TEXTFIELDTABLECELL) {
			tabColumn.setCellFactory(column -> {
				if (converterClazz.isAssignableFrom(StringConverter.class)) {
					return new TextFieldTableCell(new StingConverterHelper());
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
	 * apply the desired factory on the given column
	 *
	 * @param tabColumn:
	 *            the column to apply the factory on
	 * @param converterClazz:
	 *            to find out the converter to be applied on the column
	 * @param listItemsForCombobox:
	 *            list of combobox item. has to be setted to null if the the
	 *            cellFactory type is not a combobox
	 * @param cellFactoryTyp:
	 *            the factoryTyp to be applied
	 * @author ca.leumaleu
	 */
	@Override
	public void applyCellFactoryTo(TableColumn tabColumn, CellFactoryTyp cellFactoryTyp, List<T> listItemsForCombobox,
			Class converterClazz) {
		if (cellFactoryTyp == CellFactoryTyp.CHECKBOXTABLECELL) {
			tabColumn.setCellFactory(column -> {
				return new CheckBoxTableCell();
			});
		} else if (cellFactoryTyp == CellFactoryTyp.COMBOBOXTABLECELL) {
			if (listItemsForCombobox != null && listItemsForCombobox.size() > 0) {
				ObservableList<T> ol = FXCollections.observableArrayList(listItemsForCombobox);
				tabColumn.setCellFactory(column -> {
					if (converterClazz.isAssignableFrom(StringConverter.class)) {
						return new ComboBoxTableCell(ol);
					} else if (converterClazz.isAssignableFrom(DateStringConverter.class)) {
						return new ComboBoxTableCell(new DateStringConverter(), ol);
					} else if (converterClazz.isAssignableFrom(LongStringConverter.class)) {
						return new ComboBoxTableCell(new LongStringConverter(), ol);
					} else if (converterClazz.isAssignableFrom(IntegerStringConverter.class)) {
						return new ComboBoxTableCell(new IntegerStringConverter(), ol);
					} else if (converterClazz.isAssignableFrom(DoubleStringConverter.class)) {
						return new ComboBoxTableCell(new DoubleStringConverter(), ol);
					} else if (converterClazz.isAssignableFrom(FloatStringConverter.class)) {
						return new ComboBoxTableCell(new FloatStringConverter(), ol);
					}
					return null;
				});
			}
		} else if (cellFactoryTyp == CellFactoryTyp.TEXTFIELDTABLECELL) {
			tabColumn.setCellFactory(column -> {
				if (converterClazz.isAssignableFrom(StringConverter.class)) {
					return new TextFieldTableCell(new StingConverterHelper());
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
	 *
	 * @param cellFactoryTyp:
	 *            the factoryTyp to be applied.
	 * @param listItemsForCombobox:
	 *            list of combobox item. has to be setted to null if the the
	 *            cellFactory type is not a combobox
	 * @param columns:
	 *            list of columns to apply the factory on
	 * @author ca.leumaleu
	 */
	@Override
	public void applyCellFactoryTo(CellFactoryTyp cellFactoryTyp, List<T> listItemsForCombobox, String... columns) {
		if (columns.length <= 0 || (columns.length == 1 && columns[0].equals("ALL"))) {
			for (TableColumnHelper tableColumnHelper : listOfColumns) {
				applyCellFactoryTo(tableColumnHelper, cellFactoryTyp, listItemsForCombobox,
						tableColumnHelper.getConverterClazz());
			}
		} else {
			List<String> colList = Arrays.asList(columns);
			for (TableColumnHelper tableColumnHelper : listOfColumns) {
				if (colList.contains(tableColumnHelper.getText())) {
					applyCellFactoryTo(tableColumnHelper, cellFactoryTyp, listItemsForCombobox,
							tableColumnHelper.getConverterClazz());
				}
			}
		}
	}

	/**
	 * the function call the given update/edit method located in the given
	 * Service class after an edit action is done
	 *
	 * @param serviceClazz:
	 *            to access the class where the method to be execute is declared
	 * @param method:
	 *            name of the method to be execute
	 * @author ca.leumaleu
	 */
	@Override
	public void performOnEditCommit(Class serviceClazz, String method) {
		for (TableColumnHelper tableColumnHelper : listOfColumns) {
			tableColumnHelper.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
				@Override
				public void handle(CellEditEvent event) {
					T en = ((T) event.getTableView().getItems().get(event.getTablePosition().getRow()));
					Object o = null;
					String newValue = event.getNewValue().toString();
					String fieldName = ((PropertyValueFactory) event.getTableColumn().getCellValueFactory())
							.getProperty();
					try {
						en.getClass()
								.getMethod("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1),
										String.class)
								.invoke(en, newValue);
						Object t = serviceClazz.newInstance();
						Method[] allMethods = serviceClazz.getDeclaredMethods();
						for (Method m : allMethods) {
							String mname = m.getName();
							if (!mname.startsWith(method) || (m.getGenericReturnType() != void.class)) {
								continue;
							}
							m.setAccessible(true);
							o = m.invoke(t, en);
						}
					} catch (SecurityException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | InstantiationException e) {
						logger.log(Level.SEVERE, "Commit action could not be perform " + e.getMessage());
					}
				}
			});
		}
	}

	private class StingConverterHelper extends StringConverter {

		@Override
		public String toString(Object object) {
			return object.toString();
		}

		@Override
		public Object fromString(String string) {
			return string;
		}

	}

	/**
	 * set a Button as graphic.
	 * @param serviceClazz
	 * @param method: performed when the button is clicked
	 * @param css: String containing the css to be apply on the column. For example
	 * 		.btn{
	 * 			-fx-width:300px;
	 * 			...
	 * 		}
	 * @param columns
	 */
	@Override
	public void applyButtonCellFactoryToColumns(Class serviceClazz, String method, String css, String... columns) {
		if (columns.length <= 0 || (columns.length == 1 && columns[0].equals("ALL"))) {
			for (TableColumnHelper tableColumnHelper : listOfColumns) {
				if (tableColumnHelper.equals(tableColumnHelper.getText())) {
					applyButtonFactory(tableColumnHelper, serviceClazz, method, css);
				}
			}
		} else {
			List<String> colList = Arrays.asList(columns);
			for (TableColumnHelper tableColumnHelper : listOfColumns) {
				if (colList.contains(tableColumnHelper.getText())) {
					applyButtonFactory(tableColumnHelper, serviceClazz, method, css);
				}
			}
		}
	}

	private void applyButtonFactory(TableColumnHelper tableColumnHelper, Class serviceClazz, String method, String css) {
		tableColumnHelper.setCellFactory(new Callback<TableColumn, TableCell>() {
			@Override
			public TableCell call(TableColumn param) {
				TableCell cell = new TableCell() {
					@Override
					public void updateItem(Object item, boolean empty) {
						if (item != null) {
							Button btn = new Button(item.toString());
							btn.setPrefWidth(tableColumnHelper.getPrefWidth());
							btn.setPrefHeight(40.0);
							assignEvent(btn, serviceClazz, method);
							if(css.length() > 0)
								btn.setStyle(css);
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		});
	}


	/**
	 * set a Button as graphic that will perform the given function. The Button
	 * Text is the text within the cell.
	 * @param tableColumnHelper
	 * @param serviceClazz
	 * @param method
	 * @param css
	 */
	@Override
	public void applyButtonFactory(TableColumn tableColumn, Class serviceClazz, String method, String css) {
		tableColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
			@Override
			public TableCell call(TableColumn param) {
				TableCell cell = new TableCell() {
					@Override
					public void updateItem(Object item, boolean empty) {
						if (item != null) {
							Button btn = new Button(item.toString());
							btn.setPrefWidth(tableColumn.getPrefWidth());
							btn.setPrefHeight(40.0);
							assignEvent(btn, serviceClazz, method);
							if(css.length() > 0)
								btn.setStyle(css);
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		});
	}
}
