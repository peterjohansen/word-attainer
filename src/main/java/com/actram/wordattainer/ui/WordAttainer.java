package com.actram.wordattainer.ui;

import com.actram.wordattainer.MorphemeList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
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

	private final MorphemeList morphemes;

	private Stage stage;

	public WordAttainer() {
		this.morphemes = new MorphemeList();
		
		morphemes.add("test1");
		morphemes.add("test2");
		morphemes.add("test3");
		morphemes.add("test4");
	}

	public MorphemeList getMorphemes() {
		return morphemes;
	}

	public Stage getStage() {
		return stage;
	}

	public boolean showConfirmAlert(String title, String content, String okButtonText, String cancelButtonText) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.initOwner(stage);
		confirmAlert.setTitle(title);
		confirmAlert.setHeaderText(null);
		confirmAlert.setContentText(content);

		ButtonType okButton = new ButtonType(okButtonText, ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType(cancelButtonText, ButtonData.CANCEL_CLOSE);
		confirmAlert.getButtonTypes().setAll(okButton, cancelButton);

		return (confirmAlert.showAndWait().get() == ButtonType.OK);
	}

	@Override
	public void start(Stage stage) throws Exception {
		WordAttainer.instance = this;
		this.stage = stage;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/ui/"));
		Parent root = loader.load(getClass().getResourceAsStream("/ui/main.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle(TITLE);
		stage.show();
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}
}