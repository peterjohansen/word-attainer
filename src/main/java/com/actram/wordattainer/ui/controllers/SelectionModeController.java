package com.actram.wordattainer.ui.controllers;

import java.util.ResourceBundle;

import com.actram.wordattainer.Generator;
import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class SelectionModeController implements MainControllerChild {
	private MainController mainController;

	private Stage stage;

	public void close() {
		stage.close();
	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;
	}

	void setRootNode(Parent parent) {
		this.stage = new Stage();
		stage.setScene(new Scene(parent));
		stage.setTitle("Generator: Selection Mode");
	}

	public void showSelectionMode() {
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(mainController.getStage());
		stage.show();

		Preferences preferences = mainController.getPreferences();
		Generator generator = preferences.getGenerator();
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {

	}
}