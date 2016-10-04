package com.tableview.manager.helper;

import com.tableview.manager.enums.CellFactoryTyp;

import javafx.scene.control.Cell;
import javafx.scene.control.TableColumn;

@SuppressWarnings("all")
public class TableColumnHelper extends TableColumn {
	private CellFactoryTyp cellFactoryTyp;
	private Class typeClazz;
	private Class converterClazz;

	public TableColumnHelper() {

	}

	public TableColumnHelper(String name) {
		super(name);
	}

	public CellFactoryTyp getCellFactoryTyp() {
		return cellFactoryTyp;
	}

	public void setCellFactoryTyp(CellFactoryTyp cellFactoryTyp) {
		this.cellFactoryTyp = cellFactoryTyp;
	}

	public Class getTypeClazz() {
		return typeClazz;
	}

	public void setTypeClazz(Class typeClazz) {
		this.typeClazz = typeClazz;
	}

	public Class getConverterClazz() {
		return converterClazz;
	}

	public void setConverterClazz(Class converterClazz) {
		this.converterClazz = converterClazz;
	}

}
