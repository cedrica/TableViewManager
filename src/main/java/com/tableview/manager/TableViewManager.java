/**
 * this class was implemented to simplify the initialization and the setting of TableView.
 * The Class is of course an open source to be improved as desired. that is the reason why it implements a Interface.
 * please enter other functions in the interface that you mint they could be useful for the customizing or setting of
 * TableView.
 * In Order to initialize and render your table using TableViewManager, two main steps are supposed to be done first.
 * 1. You first have to initialize a TableViewManager Object and assign to its construct the defined table.
 * 2. then you have to call the initColumnSetValueFactory() function to initialize the columns of the table
 * Just after this two steps you can start using oder functions.
 * Some function are not stable that is the reason why they are annotated as deprecate. Some other are just meant to be
 * use for
 * special case that is why please first read the description before using the function.
 *
 * @author ca.leumaleu
 */
package com.tableview.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import com.tableview.manager.annotation.AddGlyphIcon;
import com.tableview.manager.annotation.Column;
import com.tableview.manager.annotation.Condition;
import com.tableview.manager.annotation.SpecialCase;
import com.tableview.manager.annotation.Transient;
import com.tableview.manager.helper.Helper;
import com.tableview.manager.helper.TableColumnHelper;
import com.tableview.manager.helper.UserData;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

@SuppressWarnings("all")
public class TableViewManager<T> {

	private TableView			tableView;
	private List<TableColumn>	listOfColumns;
	private List<TableColumn>	excludedColumns;
	private boolean				isUpdatable;
	private static Logger		logger	= null;
	HashMap<String, Integer>	columnsOrderMap;

	public TableViewManager(TableView tableView) {
		this.tableView = tableView;
		listOfColumns = new ArrayList<TableColumn>();
		excludedColumns = new ArrayList<TableColumn>();
		columnsOrderMap = new HashMap<String, Integer>();
		logger = Logger.getLogger(this.getClass().getSimpleName());
	}

	public TableViewManager() {

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

	public void clear() {
		tableView.getItems().clear();
		tableView.setItems(null);
	}

	public void setItems(ObservableList<T> items) {
		this.tableView.setItems(items);
		if (items != null && !items.isEmpty())
			initColumnAndSetValueFactory(items.get(0).getClass());
	}

	public ObjectProperty<T> itemsProperty() {
		return tableView.itemsProperty();
	}

	public List<TableColumn> getListOfColumns() {
		return listOfColumns;
	}


	public void setListOfColumns(List<TableColumn> listOfColumns) {
		this.listOfColumns = listOfColumns;
	}

	/**
	 * This function initialize table columns of a POJO. Here the columns name
	 * are represented by the name of the attributes of the POJO
	 *
	 * @param entityClazz:
	 *        the class name of the PoJo
	 */
	public void initColumnAndSetValueFactory(Class entityClazz) {
		listOfColumns = new ArrayList<TableColumn>();
		Field[] attributes = entityClazz.getDeclaredFields();
		List<Field> annotatedFields = new ArrayList<Field>();
		List<Field> childAttributs = new ArrayList<Field>();
		HashMap<String, List<Field>> mapParentNameAttribut = new HashMap<String, List<Field>>();
		StringBuilder sb = new StringBuilder();
		for (Field att : attributes) {
			String colName = att.getName();
			Column colAnnotation = att.getAnnotation(Column.class);
			Transient transientAnnotation = att.getAnnotation(Transient.class);
			if (transientAnnotation != null) {
				continue;
			}
			double columnSize = 1;
			int[] bgColorRGB = null;
			int[] fgColorRGB = null;
			boolean isBold = false;
			boolean isItalic = false;
			boolean isEuroNumber = false;
			String fontFamily = null;
			int fontSize = 15;
			Condition[] bgForGivenConditions = null;
			Condition[] fgForGivenConditions = null;
			AddGlyphIcon glyphIcon = null;
			if (colAnnotation != null) {
				String customname = colAnnotation.customname();
				colName = (customname.length() <= 0) ? colName : customname;
				bgColorRGB = colAnnotation.bgColor();
				fgColorRGB = colAnnotation.fgColor();
				fontFamily = colAnnotation.fontFamily();
				fontSize = colAnnotation.fontSize();
				columnSize = colAnnotation.columnSize();
				bgForGivenConditions = colAnnotation.bgForGivenConditions();
				fgForGivenConditions = colAnnotation.fgForGivenConditions();
				String parentName = colAnnotation.parent();
				isBold = colAnnotation.isBold();
				isItalic = colAnnotation.isItalic();
				isEuroNumber = colAnnotation.isEuroNumber();
				glyphIcon = colAnnotation.addGlyphIcon();
				if (parentName.trim().length() > 0) {
					childAttributs.add(att);
					mapParentNameAttribut.put(parentName, childAttributs);
					continue;
				}

			}
			TableColumn tableColumn = createAndSetColumn(att, colName);
			tableColumn.setMinWidth(columnSize);
			tableColumn.setUserData(new UserData(	bgColorRGB, fgColorRGB, isBold, isItalic, fontFamily, fontSize, glyphIcon, isEuroNumber,
													bgForGivenConditions, fgForGivenConditions));
			formatForGivenCondition(tableColumn);
			listOfColumns.add(tableColumn);
		}
		if (childAttributs.size() == 1) {
			System.err.println("Für Spalteverschachteltung muss mindestens 2 Kinder das gleiche parent haben");
			return;
		}
		mergeColumns(mapParentNameAttribut);
		sb.toString().trim();
		logger.log(Level.INFO, "Columns initialised [" + sb.toString() + "]");
		this.tableView.getColumns().clear();
		this.tableView.getColumns().setAll(listOfColumns);
		initColumnsOrderMap();
	}


	private void initColumnsOrderMap() {
		for (TableColumn column : listOfColumns) {
			columnsOrderMap.put(column.getText(), listOfColumns.indexOf(column));
		}
	}


	private void formatForGivenCondition(TableColumn column) {
		column.setCellFactory(new Callback<TableColumn, TableCell>() {

			@Override
			public TableCell call(TableColumn param) {
				TableCell cell = new TableCell() {

					@Override
					public void updateItem(Object item, boolean empty) {
						if (item != null) {
							HBox hb = new HBox();
							Label label = new Label(item.toString());
							String fg = "", bg = "";
							UserData userData = (UserData) column.getUserData();
							Condition[] bgForGivenConditions = userData.getBgForGivenConditions();
							Condition[] fgForGivenConditions = userData.getFgForGivenConditions();
							boolean isEuroNumber = userData.isEuroNumber();
							if (isEuroNumber) {
								String itemStr = NumberFormat.getNumberInstance(Locale.GERMANY).format(item);
								label = new Label(itemStr);
							}
							bg = Helper.generateBgFont(item, hb, bg, userData, bgForGivenConditions);
							fg = Helper.generateFgFont(item, hb, fg, userData, fgForGivenConditions);
							AddGlyphIcon addGlyphIcon = userData.getIcon();
							hb = Helper.assignGlyphIcon(hb, label, addGlyphIcon);
							label.setStyle(fg);
							hb.setSpacing(5);
							hb.setStyle(bg);
							setGraphic(hb);
						} else {
							setText(null);
							setGraphic(null);
							setStyle(null);
						}
					}
				};
				return cell;
			}
		});
	}



	public void run(Class clazz, String method, String param) {
		try {
			Object t = clazz.newInstance();
			Method m;
			try {
				m = clazz.getDeclaredMethod(method, String.class);
				if (m == null)
					return; // no such a method found
				m.setAccessible(true);
				m.invoke(t, param);
			}
			catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setTextFieldFactory(TableColumnHelper column) {

		column.setCellFactory(new Callback<TableColumn, TableCell>() {

			@Override
			public TableCell call(TableColumn p) {
				TextFieldTableCell cell = new TextFieldTableCell(new StringConverter() {

					@Override
					public String toString(Object t) {
						return t.toString();
					}

					@Override
					public Object fromString(String string) {
						return string;
					}
				});

				return cell;
			}
		});
	}

	/**
	 * assign column children to they corresponding parent
	 * @param map: key is the parent name and values children´s one
	 */
	private void mergeColumns(HashMap<String, List<Field>> map) {

		for (Map.Entry<String, List<Field>> entry : map.entrySet()) {
			String parentName = entry.getKey();
			List<Field> childAttributs = entry.getValue();
			TableColumn parentCol = new TableColumn(parentName);
			int[] bgColorRGB = null;
			int[] fgColorRGB = null;
			String fontFamily = null;
			Condition[] bgForGivenConditions = null;
			Condition[] fgForGivenConditions = null;
			boolean isBold = false;
			boolean isItalic = false;
			boolean isEuroNumber = false;
			double columnSize = 100;
			int fontSize = 15;
			AddGlyphIcon glyphIcon = null;
			Column colAnnotation;
			for (Field childAttribut : childAttributs) {
				colAnnotation = childAttribut.getAnnotation(Column.class);
				bgColorRGB = colAnnotation.bgColor();
				fgColorRGB = colAnnotation.fgColor();
				fontFamily = colAnnotation.fontFamily();
				fontSize = colAnnotation.fontSize();
				columnSize = colAnnotation.columnSize();
				bgForGivenConditions = colAnnotation.bgForGivenConditions();
				isBold = colAnnotation.isBold();
				isItalic = colAnnotation.isItalic();
				bgColorRGB = colAnnotation.bgColor();
				isEuroNumber = colAnnotation.isEuroNumber();
				bgForGivenConditions = colAnnotation.bgForGivenConditions();
				fgForGivenConditions = colAnnotation.fgForGivenConditions();
				String colName = childAttribut.getName();
				String customname = colAnnotation.customname();
				colName = (customname.length() <= 0) ? colName : customname;
				glyphIcon = colAnnotation.addGlyphIcon();
				TableColumn tableColumn = createAndSetColumn(childAttribut, colName);
				tableColumn.setMinWidth(columnSize);
				tableColumn.setUserData(new UserData(bgColorRGB, fgColorRGB, isBold, isItalic, fontFamily, fontSize, glyphIcon, isEuroNumber,
														bgForGivenConditions, fgForGivenConditions));
				formatForGivenCondition(tableColumn);
				parentCol.getColumns().add(tableColumn);
			}
			listOfColumns.add(parentCol);
		}

	}

	private TableColumn createAndSetColumn(Field att, String colName) {
		TableColumn tableColumn = new TableColumn(colName);
		// associate data to column using setCellValueFactory
		if (att.getType().isAssignableFrom(String.class) || att.getType().isAssignableFrom(SimpleStringProperty.class)) {
			tableColumn.setCellValueFactory(new PropertyValueFactory<T, String>(att.getName()));
			// tableColumn.setConverterClazz(StringConverter.class);
		} else if (att.getType().isAssignableFrom(Integer.class) || att.getType().isAssignableFrom(int.class)
						|| att.getType().isAssignableFrom(SimpleIntegerProperty.class)) {
			tableColumn.setCellValueFactory(new PropertyValueFactory<T, Integer>(att.getName()));
			// tableColumn.setConverterClazz(IntegerStringConverter.class);
		} else if (att.getType().isAssignableFrom(Long.class) || att.getType().isAssignableFrom(long.class)
						|| att.getType().isAssignableFrom(SimpleLongProperty.class)) {
			tableColumn.setCellValueFactory(new PropertyValueFactory<T, Long>(att.getName()));
			// tableColumn.setConverterClazz(LongStringConverter.class);
		} else if (att.getType().isAssignableFrom(Double.class) || att.getType().isAssignableFrom(double.class)
						|| att.getType().isAssignableFrom(SimpleDoubleProperty.class)) {
			tableColumn.setCellValueFactory(new PropertyValueFactory<T, Double>(att.getName()));
			// tableColumn.setConverterClazz(DoubleStringConverter.class);
		} else if (att.getType().isAssignableFrom(Date.class) || att.getType().isAssignableFrom(LocalDate.class)) {
			tableColumn.setCellValueFactory(new PropertyValueFactory<T, Date>(att.getName()));
			// tableColumn.setConverterClazz(DateStringConverter.class);
		} else if (att.getType().isAssignableFrom(Float.class) || att.getType().isAssignableFrom(float.class)
						|| att.getType().isAssignableFrom(SimpleFloatProperty.class)) {
			tableColumn.setCellValueFactory(new PropertyValueFactory<T, Float>(att.getName()));
			// tableColumn.setConverterClazz(FloatStringConverter.class);
		} else {
			System.err.println(" Unzulässige Annotierung von Spalte " + att.getName()
							+ ". Nur Spalte mit primitiv Datentyp dürfen hier annotiert werden");
			System.exit(1);
		}
		return tableColumn;
	}


	/**
	 * to assign a custom node to the cells of a given column.
	 *
	 * @param column
	 * @param node
	 * @param direction
	 */
	private void applyGraphic(TableColumn column, Node node) {
		column.setCellFactory(new Callback<TableColumn, TableCell>() {

			@Override
			public TableCell call(TableColumn param) {
				TableCell cell = new TableCell() {

					@Override
					public void updateItem(Object item, boolean empty) {
						if (item != null) {
							setText(null);
							setGraphic(node);
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
	 *        array containing the columns name. If "ALL" is given then
	 *        Graphic will be assign on all column.
	 * @param componentClass:
	 *        the class of the components category you want to display in
	 *        the table
	 * @param serviceClass:
	 *        use to access via reflexion the method insides which will be
	 *        execute when the Event will be fired.
	 * @param method
	 * @param itemListForListComponent:
	 *        i.e. combobox does not have a other Eventhandler as Button
	 */
	public void assignGraphicToColumn(Node node, String... columnsName) {
		List<String> columnsnameList = Arrays.asList(columnsName);
		int i = 0;
		for (TableColumn column : listOfColumns) {
			if (columnsnameList.contains(column.getText())) {
				applyGraphic(column, node);
			}
		}
		tableView.getColumns().clear();
		tableView.getColumns().addAll(listOfColumns);
	}

	/**
	 * render the given column by assigning a component which Class correspond
	 * to the given class.
	 *
	 * @deprecated: method not completely implemented
	 * @param columnsName:
	 *        array containing the columns name. If "ALL" is given then
	 *        Graphic will be assign on all column.
	 * @param componentClass:
	 *        the class of the components category you want to display in
	 *        the table
	 * @param serviceClass:
	 *        use to access via reflexion the method insides which will be
	 *        execute when the Event will be fired.
	 * @param method
	 * @param itemListForListComponent:
	 *        i.e. combobox does not have a other Eventhandler as Button
	 */
	public void setCustomCellFactory(Node customControl, String... columnsName) {
		List<String> columnsnameList = Arrays.asList(columnsName);
		int i = 0;
		for (TableColumn column : listOfColumns) {
			if (columnsnameList.contains(column.getText())) {
				applyGraphic(column, customControl);
			}
		}
		tableView.getColumns().clear();
		tableView.getColumns().addAll(listOfColumns);
	}

	/**
	 * hide columns by name. Must be call before the
	 * cellFactory is setted.
	 *
	 * @param columnToBeExclude
	 */
	public void excludeColumns(String... columnToBeExclude) {
		List<String> toBeExcludedColumnNames = Arrays.asList(columnToBeExclude);
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (TableColumn tableColumnHelper : listOfColumns) {
			if (toBeExcludedColumnNames.contains(tableColumnHelper.getText())) {
				excludedColumns.add(tableColumnHelper);
				sb.append(tableColumnHelper.getText() + " ");
			}
		}
		listOfColumns.removeAll(excludedColumns);
		this.tableView.getColumns().clear();
		this.tableView.getColumns().addAll(listOfColumns);
	}

	/**
	 * hide column
	 * @param column
	 */
	public void excludeColumn(TableColumn column) {
		excludedColumns.add(column);
		listOfColumns.remove(column);
		this.tableView.getColumns().clear();
		this.tableView.getColumns().addAll(listOfColumns);
	}
	/**
	 * show columns by name
	 * @param columnToBeInclude
	 */
	public void includeColumns(String... columnToBeInclude) {
		List<String> toBeIncludedColumnNames = Arrays.asList(columnToBeInclude);
		List<TableColumn> helperList = new ArrayList<TableColumn>();
		for (TableColumn tableColumnHelper : excludedColumns) {
			if (toBeIncludedColumnNames.contains(tableColumnHelper.getText())) {
				listOfColumns.add(tableColumnHelper);
				helperList.add(tableColumnHelper);
			}
		}
		excludedColumns.removeAll(helperList);
		this.tableView.getColumns().clear();
		this.tableView.getColumns().addAll(listOfColumns);
	}
	/**
	 * show column column
	 * @param column
	 */
	public void includeColumn(TableColumnHelper column) {
		excludedColumns.remove(column);
		listOfColumns.add(column);
		this.tableView.getColumns().clear();
		this.tableView.getColumns().addAll(listOfColumns);
	}


	public void setTextFieldTableCellFactory(TableColumnHelper tabColumn, Class converterClazz) {
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

	public void setComboboxTableCellFactory(TableColumn tabColumn, Class converterClazz, ObservableList<T> ol) {
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


	public void setCheckBoxTableCellFactory(TableColumn tabColumn) {
		tabColumn.setCellFactory(column -> {
			return new CheckBoxTableCell();
		});
	}

	public void setTextFieldCellFactory(TableColumn column) {
		column.setCellFactory(new Callback<TableColumn<T, String>, TableCell<T, String>>() {

			String cellValue = "";

			@Override
			public TableCell call(TableColumn p) {

				TextFieldTableCell cell = new TextFieldTableCell(new StringConverter() {

					@Override
					public String toString(Object t) {
						cellValue = t.toString();
						return t.toString();
					}

					@Override
					public Object fromString(String string) {
						return string;
					}
				});
				return cell;
			}
		});
	}

	/**
	 * the function call the given update/edit method located in the given
	 * Service class after an edit action is done
	 *
	 * @param serviceClazz:
	 *        to access the class where the method to be execute is declared
	 * @param method:
	 *        name of the method to be execute
	 * @author ca.leumaleu
	 */
	public void performOnEditCommit(Class serviceClazz, String method) {
		for (TableColumn tableColumnHelper : listOfColumns) {
			tableColumnHelper.setCellFactory(TextFieldTableCell.forTableColumn());
			tableColumnHelper.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {

				@Override
				public void handle(CellEditEvent event) {
					T en = ((T) event.getTableView().getItems().get(event.getTablePosition().getRow()));
					Object o = null;
					String newValue = event.getNewValue().toString();
					String fieldName = ((PropertyValueFactory) event.getTableColumn().getCellValueFactory()).getProperty();
					try {
						en.getClass().getMethod("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), String.class).invoke(en,
										newValue);
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
					}
					catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
									| InstantiationException e) {
						logger.log(Level.SEVERE, "Commit action could not be perform " + e.getMessage());
					}
				}
			});
		}
	}

	public void onEditCommit(Class clazz, String method) {
		for (TableColumn column : listOfColumns) {
			column.setCellFactory(TextFieldTableCell.forTableColumn());
			column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {

				@Override
				public void handle(CellEditEvent event) {
					Object o = null;
					String newValue = event.getNewValue().toString();
					String fieldName = ((PropertyValueFactory) event.getTableColumn().getCellValueFactory()).getProperty();
					try {
						Object t = clazz.newInstance();
						Method[] allMethods = clazz.getDeclaredMethods();
						for (Method m : allMethods) {
							String mname = m.getName();
							if (!mname.startsWith(method) || (m.getGenericReturnType() != void.class)) {
								continue;
							}
							m.setAccessible(true);
							o = m.invoke(t);
						}
					}
					catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
									| InstantiationException e) {
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
	 * set the selections mode
	 *
	 * @param selectionMode
	 */
	public void setCellSelectionMode(SelectionMode selectionMode) {
		tableView.getSelectionModel().setCellSelectionEnabled(true);
		tableView.getSelectionModel().setSelectionMode(selectionMode);
	}

	public void setCellSelection(boolean b) {
		tableView.getSelectionModel().setCellSelectionEnabled(b);
	}


	public void hideTableHeader() {
		// Don't show header
		Pane header = (Pane) tableView.lookup("TableHeaderRow");
		if (header.isVisible()) {
			header.setMaxHeight(0);
			header.setMinHeight(0);
			header.setPrefHeight(0);
			header.setVisible(false);
		}
	}

	public TableView<T> getTableView() {
		return tableView;

	}
}
