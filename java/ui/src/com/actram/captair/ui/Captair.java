package com.actram.captair.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Captair extends Application {
	public static void main(String[] args) {
		Application.launch(Captair.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = new FXMLLoader().load(getClass().getResourceAsStream("/ui/morpheme_lists.fxml"));
		stage.setTitle("FXML Welcome");
		stage.setScene(new Scene(root));
		stage.show();
	}
}