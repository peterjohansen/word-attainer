package com.actram.captair.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WordAttainer extends Application {
	public static void main(String[] args) {
		Application.launch(WordAttainer.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = new FXMLLoader().load(getClass().getResourceAsStream("/ui/main.fxml"));
		stage.setTitle("FXML Welcome");
		stage.setScene(new Scene(root));
		stage.show();
	}
}