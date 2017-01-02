package com.tableview.manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestClass extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader table = new FXMLLoader(TestClass.class.getResource("/com/tableview/manager/Table.fxml"));
		table.load();
		Scene scene = new Scene(table.getRoot());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(TestClass.class);
	}


}
