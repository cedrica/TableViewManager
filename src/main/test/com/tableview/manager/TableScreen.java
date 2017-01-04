package com.tableview.manager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.tableview.manager.TableViewManager;

import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class TableScreen implements Initializable{

	@FXML TableView<TestObject> table;
	private ObservableList<TestObject> items;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<TestObject> list = new ArrayList<>();
		for(int i = 0; i < 5; i++){
			list.add(new TestObject("name"+i,"Cedric"+i,"Christelle"+i, i*1000, "homePage"+i));
		}
		items = FXCollections.observableArrayList(list);
		TableViewManager<TestObject> tvm = new TableViewManager<TestObject>(table);
		tvm.initColumnAndSetValueFactory(TestObject.class);
		table.setItems(items);
		table.setTableMenuButtonVisible(true);
	}
	
	public void print(){
		System.out.println("gut!");
	}

}
