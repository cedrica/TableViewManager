/**
 * @author ca.leumaleu
 */
package com.tableview.manager;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.TableColumn;

@SuppressWarnings ("all")
public interface ITableViewManager<T> {

	public void addData(List<T> data);

	/**
	 * This function initialize table columns which belong to the same Object
	 *
	 * @param columnsTitle
	 * @param entity
	 */
	public void initColumnSetValueFactory(Class<T> entity);

	/**
	 * This function initialize table columns of a POJO. Here the columns name
	 * are represented given name in the columnname array
	 *
	 * @param entityClazz:
	 *            the class name of the PoJo
	 */
	public void initColumnSetValueFactory(Class entityClazz, String[] columnname);

	/**
	 * render the given column by assigning to its cells a Graphic which the Class
	 * correspond to the given class.
	 *
	 * @param columnsName: array of columns name. if "ALL" is given then graphic will be assign to all columns.
	 * @param componentClass: the class of the components category you want to display in the table i.e. Button.class
	 * @param serviceClass: use to access vie reflextion the method to be executed in the event handler
	 * @param method
	 * @param itemListForListComponent: i.e. Combobox does not have a other Eventhandler as Button
	 */
	public void assignGraphicToColumn(Node node,String... columnsName);

	/**
	 * hide the given columns
	 * @param entityClass: Object Class
	 * @param columnToBeExclude
	 */
	void excludeColumns(Class<T> entityClass, String... columnToBeExclude);

	public void addContextMenuToColumn(String column, Class<T> clazz);

	/**
	 * Apply CellFactory on the given column
	 * @param cellFactoryTyp: Components category to be display by double click on table cell
	 * @param converterClazz: converter class to be apply on the column. for example if the column will content Integer then
	 * you have to apply a IntegerStringConverter
	 * @param column: the the column to apply the cellFactory on
	 */
	public void applyCellFactoryTo(TableColumn column, CellFactoryTyp cellFactoryTyp, List<T> listItemsForCombobox, Class converterClazz);

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
	 */
	public void applyCellFactoryTo(CellFactoryTyp cellFactoryTyp , List<T> listItemsForCombobox, String ...columns);

	/**
	 * the function call the given update/edit method located in the given Service class after an edit action is done
	 * @param serviceClazz: to access the declared methods via reflexion
	 * @param method: name of the method to be execute
	 */
	public void performOnEditCommit(Class serviceClazz, String method);

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
	public void applyButtonCellFactoryToColumns(Class serviceClazz, String method, String css, String... columns);


	/**
	 * set a Button as graphic that will perform the given function. The Button
	 * Text is the text within the cell.
	 * @param tableColumnHelper
	 * @param serviceClazz
	 * @param method
	 * @param css
	 */
	public void applyButtonFactory(TableColumn tableColumn, Class serviceClazz, String method, String css);
}
