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
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/ui/"));
		Parent root = loader.load(getClass().getResourceAsStream("/ui/main.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Word Attainer");
		stage.show();
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}
}