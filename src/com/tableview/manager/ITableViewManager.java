package com.tableview.manager;

import java.util.List;

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
	 * render the given column by assigning to its cells a component which Class
	 * correspond to the given class for Combobox Object you first have to
	 *
	 * @param columnName
	 * @param componentClass: the class of the components category you want to display in the table i.e. Button.class
	 * @param serviceClass:
	 *            use by reflexion to  execute the
	 *            function having the same name as method argument
	 * @param method
	 * @param itemListForListComponent: i.e. combobox does not have a other Eventhandler as Button
	 */
	void assignCellFactoryToSpecificColumn(String columnName, Class componentClass, Class serviceClass,
			String method, List<String>... itemListForListComponent);

	/**
	 * hide the given columns
	 * @param entityClass: Object Class
	 * @param columnToBeExclude
	 */
	void excludeColumns(Class<T> entityClass, String... columnToBeExclude);

	/**
	 * the function call the given update/edit method located in the given busynesslogic class after an edit action is done
	 * @param busynessLogic
	 * @param clazz
	 * @author ca.leumaleu
	 */
	public void performEditActionOnCell(Class<T> busynessLogic, Class<T> clazz, String method);

	public void addContextMenuToColumn(String column, Class<T> clazz);

	/**
	 * Apply CellFactory on the given column
	 * @param cellFactoryTyp: Components category to be display by double click on table cell
	 * @param converterClazz: converter class to be apply on the column. for example if the column will content Integer then
	 * you have to apply a IntegerStringConverter
	 * @param column: the the column to apply the cellFactory on
	 */
	public void applyCellFactoryTo(TableColumnHelper column, CellFactoryTyp cellFactoryTyp, Class converterClazz);

	/**
	 * Apply CellFactory on given columns
	 * @param cellFactoryTyp: Components category to be display by double click on table cell
	 * @param converterClazz: converter class to be apply on the column. for example if the column will content Integer then
	 * you have to apply a IntegerStringConverter
	 * @param columns: the list of the column to apply the cellFactory on. if the list is empty or or the given columns name is ALL the
	 * it will apply the Factory on all the column
	 */
	public void applyCellFactoryTo(CellFactoryTyp cellFactoryTyp , String ...columns);


	public void performOnEditCommit(Class serviceClazz, String method);
}
