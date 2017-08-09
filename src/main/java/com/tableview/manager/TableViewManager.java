/**
 * @author ca.leumaleu
 *         this class was implemented to simplify the initialization and the setting of TableView.
 *         The class is of course an open source to be improved as desired. that is the reason why it implements a
 *         Interface.
 *         please enter other functions in the interface that you mint they could be useful for the customizing or
 *         setting of
 *         TableView.
 *         In Order to initialize and render your table using TableViewManager, two main steps are supposed to be done
 *         first.
 *         1. You first have to initialize a TableViewManager Object and assign to its construct the defined table.
 *         2. then you have to call the initColumnSetValueFactory() function to initialize the columns of the table
 *         Just after this two steps you can start using oder functions.
 *         Some function are not stable that is the reason why they are annotated as deprecate. Some other are just
 *         meant to be
 *         use for
 *         special case that is why please first read the description before using the function.
 */
package com.tableview.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tableview.manager.annotation.AddGlyphIcon;
import com.tableview.manager.annotation.Column;
import com.tableview.manager.annotation.Condition;
import com.tableview.manager.annotation.Formatter;
import com.tableview.manager.annotation.Transient;
import com.tableview.manager.enums.Alignement;
import com.tableview.manager.enums.FormatterTyp;
import com.tableview.manager.helper.Helper;
import com.tableview.manager.helper.UserData;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

@SuppressWarnings("all")
public class TableViewManager<T> {

	private TableView			tableView;
	private List<TableColumn>	listOfColumns;
	private static Logger		logger	= null;

	public TableViewManager(TableView tableView) {
		this.tableView = tableView;
		listOfColumns = new ArrayList<TableColumn>();
		logger = Logger.getLogger(this.getClass().getSimpleName());
	}

	public TableViewManager() {
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

	public void clear() {
		tableView.getItems().clear();
		tableView.setItems(null);
	}

	public void setItems(ObservableList<T> items) {
		this.tableView.setItems(items);
		if (items != null && !items.isEmpty())
			initColumnAndSetValueFactory(items.get(0).getClass());
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
			Formatter formatter = null;
			Alignement alignement = null;
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
				formatter = colAnnotation.formatter();
				String parentName = colAnnotation.parent();
				isBold = colAnnotation.isBold();
				isItalic = colAnnotation.isItalic();
				glyphIcon = colAnnotation.addGlyphIcon();
				alignement = colAnnotation.alignement();
				if (parentName.trim().length() > 0) {
					childAttributs.add(att);
					mapParentNameAttribut.put(parentName, childAttributs);
					continue;
				}

			}
			TableColumn tableColumn = createColumnAndSetValueFactory(att, colName);
			tableColumn.setMinWidth(columnSize);
			tableColumn.setUserData(new UserData(	bgColorRGB, fgColorRGB, isBold, isItalic, fontFamily, fontSize, glyphIcon, bgForGivenConditions,
													fgForGivenConditions, formatter, alignement));
			renderColumnCells(tableColumn);
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
	}


	private void renderColumnCells(TableColumn column) {
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
							Formatter formatter = userData.getFormatter();

							if (formatter != null) {
								if (formatter.formatterTyp() == FormatterTyp.CURRENCY_STRING_CONVERTER) {
									CurrencyStringConverter c = new CurrencyStringConverter();
									item = c.toString(new BigDecimal(item.toString()));
								} else if (formatter.formatterTyp() == FormatterTyp.AMOUNT) {
									item = Helper.customFormat(formatter.pattern(), item);
								}else if (formatter.formatterTyp() == FormatterTyp.LOCALDATE) {
									item = Helper.customLocalDateFormat(formatter.pattern(), (LocalDate)item);
								}
								label = new Label(item.toString());
							}
							bg = Helper.generateBgFont(item, hb, bg, userData, bgForGivenConditions);
							fg = Helper.generateFgFont(item, hb, fg, userData, fgForGivenConditions);
							AddGlyphIcon addGlyphIcon = userData.getIcon();
							hb = Helper.assignGlyphIcon(hb, label, addGlyphIcon);
							label.setStyle(fg);
							hb.setSpacing(5);
							hb.setStyle(bg);
							VBox align = new VBox();
							Alignement alignement = userData.getAlignement();
							if (alignement != null) {
								if (alignement == Alignement.LEFT) {
									align.getChildren().add(hb);
									hb.setAlignment(Pos.CENTER_LEFT);
								} else if (alignement == Alignement.MIDDLE) {
									align.getChildren().add(hb);
									hb.setAlignment(Pos.CENTER);
								} else if (alignement == Alignement.RIGHT) {
									align.getChildren().add(hb);
									hb.setAlignment(Pos.CENTER_RIGHT);
								}

								setGraphic(align);
							} else {

								setGraphic(hb);
							}

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

	/**
	 * assign column children to they corresponding parent
	 * 
	 * @param map:
	 *        key is the parent name and values children´s one
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
			Formatter formatter;
			Alignement alignement = null;
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
				bgForGivenConditions = colAnnotation.bgForGivenConditions();
				fgForGivenConditions = colAnnotation.fgForGivenConditions();
				formatter = colAnnotation.formatter();
				String colName = childAttribut.getName();
				String customname = colAnnotation.customname();
				colName = (customname.length() <= 0) ? colName : customname;
				glyphIcon = colAnnotation.addGlyphIcon();
				alignement = colAnnotation.alignement();
				TableColumn tableColumn = createColumnAndSetValueFactory(childAttribut, colName);
				tableColumn.setMinWidth(columnSize);
				tableColumn.setUserData(new UserData(	bgColorRGB, fgColorRGB, isBold, isItalic, fontFamily, fontSize, glyphIcon, bgForGivenConditions,
														fgForGivenConditions, formatter, alignement));
				renderColumnCells(tableColumn);
				parentCol.getColumns().add(tableColumn);
			}
			listOfColumns.add(parentCol);
		}

	}

	/**
	 * create column and set factory value
	 * 
	 * @param att:
	 *        field of the Pojos the be map to the column
	 * @param colName:
	 *        column name
	 * @return
	 */
	private TableColumn createColumnAndSetValueFactory(Field att, String colName) {
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
	 * render the given column cells with the given Node
	 * 
	 * @param column
	 * @param customNode
	 */
	public void setCustomCellFactory(TableColumn column, Node customNode) {
		applyGraphic(column, customNode);
	}

	/**
	 * set the cell factory depending on the given converter. if converter is null the return a new
	 * TextFieldTableCell(new StringConverterHelper());
	 * 
	 * @param tabColumn
	 * @param converterClazz
	 */
	public void setTextFieldTableCellFactory(TableColumn column, Class converterClazz) {
		if (column == null)
			return;
		if (converterClazz == null)
			column.setCellFactory(c -> {
				return new TextFieldTableCell(new StringConverterHelper());
			});
		column.setCellFactory(c -> {
			if (converterClazz.isAssignableFrom(StringConverter.class)) {
				return new TextFieldTableCell(new StringConverterHelper());
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
			return new TextFieldTableCell(new StringConverterHelper());
		});
	}

	/**
	 * set the cell factory depending on the given converter
	 * 
	 * @param column
	 * @param converterClazz
	 * @param ol:
	 *        list of items
	 */
	public void setComboboxTableCellFactory(TableColumn column, Class converterClazz, ObservableList<T> ol) {
		if (column == null)
			return;
		if (converterClazz == null)
			column.setCellFactory(c -> {
				return new ComboBoxTableCell(ol);
			});
		column.setCellFactory(c -> {
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

	/**
	 * set CheckBoxTableCellFactory
	 * 
	 * @param tabColumn
	 */
	public void setCheckBoxTableCellFactory(TableColumn tabColumn) {
		tabColumn.setCellFactory(column -> {
			return new CheckBoxTableCell();
		});
	}


	/**
	 * @author ca.leumaleu
	 *         the function is used to update a cell by performing an action when a cell is edited.
	 *         Notice that this function will replace any present cellFactory by a TextFieldCellFactory as follow:
	 *         {@code: column.setCellFactory(TextFieldTableCell.forTableColumn());
	 * @param serviceClazz:
	 *        to access the class where the method to execute is declared
	 * @param method:
	 *        name of the method to execute
	 */
	public void performOnEditCommit(TableColumn column, Class serviceClazz, String method) {
		column.setCellFactory(TextFieldTableCell.forTableColumn());
		column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {

			@Override
			public void handle(CellEditEvent event) {
				T itemObject = ((T) event.getTableView().getItems().get(event.getTablePosition().getRow()));
				Object o = null;
				String enteredValue = event.getNewValue().toString();
				String fieldName = ((PropertyValueFactory) event.getTableColumn().getCellValueFactory()).getProperty();
				try {
					itemObject.getClass().getMethod("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), String.class)
									.invoke(itemObject, enteredValue);
					Object t = serviceClazz.newInstance();
					Method[] allMethods = serviceClazz.getDeclaredMethods();
					for (Method m : allMethods) {
						String mname = m.getName();
						if (!mname.startsWith(method) || (m.getGenericReturnType() != void.class)) {
							continue;
						}
						m.setAccessible(true);
						o = m.invoke(t, itemObject);
					}
				}
				catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
								| InstantiationException e) {
					logger.log(Level.SEVERE, "Commit action could not be perform " + e.getMessage());
				}
			}
		});
	}

	private class StringConverterHelper extends StringConverter {

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

	/**
	 * hide the table header
	 */
	public void hideTableHeader() {
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

	public void setItemsPropertyMethod(ListProperty<T> listProperty) {
		tableView.itemsProperty().bind(listProperty);
		if (listProperty.get() != null && !listProperty.get().isEmpty())
			initColumnAndSetValueFactory(listProperty.get().get(0).getClass());
	}
	
	public void setTableMenuButtonVisible(boolean b){
	    tableView.setTableMenuButtonVisible(b);
	}
}
