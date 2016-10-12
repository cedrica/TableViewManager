package main;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestClass extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		URL location = new URL(TestClass.class.getResource("/com/tableview/manager/Table.fxml").toExternalForm());
		FXMLLoader table = new FXMLLoader(location);
		table.load();
		Scene scene = new Scene(table.getRoot());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(TestClass.class);
	}


}
