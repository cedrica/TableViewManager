package com.tableview.manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestClass extends Application{

	private final static String PATH = "/com/tableview/manager/Table.fxml";
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent table = FXMLLoader.load(getClass().getResource(PATH));
		Scene scene = new Scene(table);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(TestClass.class);
	}


}
