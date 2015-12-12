package com.actram.wordattainer.ui;

import java.io.File;
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
	private File rootFile;

	public File getRootFile() {
		return rootFile;
	}

	public Stage getStage() {
		return stage;
	}

	public String getTitle() {
		return stage.getTitle();
	}

	public FXMLLoader loadFXML(String name) {
		return loadFXML(name, null);
	}

	public <T extends Parent> FXMLLoader loadFXML(String name, T root) {
		Objects.requireNonNull(name, "the name cannot be null");

		final String path = (UI_DIRECTORY + name);
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(path));
			loader.setRoot(root);
			loader.load(getClass().getResourceAsStream(path));
			return loader;
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException("unable to load .fxml file: " + path);
	}

	@Override
	public void start(Stage stage) throws Exception {
		WordAttainer.instance = this;
		this.stage = stage;

		this.rootFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

		stage.setScene(new Scene(loadFXML("main.fxml").getRoot()));
		stage.setTitle(TITLE);
		stage.show();
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}
}