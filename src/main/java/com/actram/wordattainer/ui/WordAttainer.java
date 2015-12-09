package com.actram.wordattainer.ui;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class WordAttainer extends Application {
	private static final String TITLE = "Word Attainer";

	private static final String UI_DIRECTORY = "/ui/";

	private static WordAttainer instance;

	public static WordAttainer getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		Application.launch(WordAttainer.class, args);
	}

	private Stage stage;

	public Stage getStage() {
		return stage;
	}

	public String getTitle() {
		return stage.getTitle();
	}

	public Parent load(FXMLLoader loader, String name) {
		Objects.requireNonNull(loader, "the loader cannot be null");
		Objects.requireNonNull(name, "the name cannot be null");

		loader.setLocation(getClass().getResource(UI_DIRECTORY));
		final String path = (UI_DIRECTORY + name);
		try {
			return loader.load(getClass().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException("unable to load .fxml file: " + path);
	}

	public Parent loadFXML(String name) {
		return load(new FXMLLoader(), name);
	}

	@Override
	public void start(Stage stage) throws Exception {
		WordAttainer.instance = this;
		this.stage = stage;

		stage.setScene(new Scene(loadFXML("main.fxml")));
		stage.setTitle(TITLE);
		stage.show();
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}
}