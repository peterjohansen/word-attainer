package com.actram.wordattainer.ui;

import java.util.Objects;

import com.actram.wordattainer.GeneratorSettings;
import com.actram.wordattainer.MorphemeList;
import com.actram.wordattainer.ResultList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class WordAttainer extends Application {
	private static final String TITLE = "Word Attainer";

	private static WordAttainer instance;

	public static WordAttainer getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		Application.launch(WordAttainer.class, args);
	}

	private final GeneratorSettings settings;
	private final ResultList results;

	private Stage stage;

	public WordAttainer() {
		this.settings = new GeneratorSettings();
		this.results = new ResultList();

		MorphemeList morphemes = settings.getMorphemes();
		morphemes.add("test1");
		morphemes.add("test2");
		morphemes.add("test3");
		morphemes.add("test4");

		results.add("test1");
		results.add("test2");
		results.add("test3");
		results.add("test4");
	}

	public Alert createAlert(AlertType type, String title) {
		Objects.requireNonNull(type, "alert type cannot be null");

		Alert alert = new Alert(type);
		alert.initOwner(stage);
		alert.setHeaderText(null);
		alert.setTitle(title != null ? getTitle() + " - " + title : getTitle());
		return alert;
	}

	public ResultList getResults() {
		return results;
	}

	public GeneratorSettings getSettings() {
		return settings;
	}

	public Stage getStage() {
		return stage;
	}

	public String getTitle() {
		return stage.getTitle();
	}

	public Parent loadFXML(String name) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/ui/"));
		try {
			return loader.load(getClass().getResourceAsStream("/ui/" + name));
		} catch (Exception e) {
			e.printStackTrace();
			showErrorAlert("Initialization Error", "Unable to load UI module: " + name);
		}
		return null;
	}

	public boolean showConfirmAlert(String title, String content, String okButtonText, String cancelButtonText) {
		Objects.requireNonNull(content, "alert content text cannot be null");
		Objects.requireNonNull(okButtonText, "alert ok button text cannot be null");
		Objects.requireNonNull(cancelButtonText, "alert cancel button text cannot be null");

		Alert confirmAlert = createAlert(Alert.AlertType.CONFIRMATION, title);
		confirmAlert.setContentText(content);

		ButtonType okButton = new ButtonType(okButtonText, ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType(cancelButtonText, ButtonData.CANCEL_CLOSE);
		confirmAlert.getButtonTypes().setAll(okButton, cancelButton);

		return (confirmAlert.showAndWait().get().getButtonData() == ButtonData.OK_DONE);
	}

	public void showErrorAlert(String title, String content) {
		Objects.requireNonNull(content, "alert content text cannot be null");

		Alert confirmAlert = createAlert(Alert.AlertType.ERROR, title);
		confirmAlert.setContentText(content);
		confirmAlert.show();
	}

	public boolean showFormAlert(String title, Node content, String okButtonText, String cancelButtonText) {
		Objects.requireNonNull(content, "alert content cannot be null");
		Objects.requireNonNull(okButtonText, "alert ok button text cannot be null");
		Objects.requireNonNull(cancelButtonText, "alert cancel button text cannot be null");

		Alert alert = createAlert(AlertType.NONE, title);
		alert.getDialogPane().setContent(content);

		ButtonType okButton = new ButtonType(okButtonText, ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType(cancelButtonText, ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(okButton, cancelButton);

		return (alert.showAndWait().get().getButtonData() == ButtonData.OK_DONE);
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