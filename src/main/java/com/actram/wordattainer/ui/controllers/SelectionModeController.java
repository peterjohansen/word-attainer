package com.actram.wordattainer.ui.controllers;

import java.util.ResourceBundle;

import com.actram.wordattainer.Generator;
import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class SelectionModeController implements MainControllerChild {
	@FXML private Label statsLabel;
	@FXML private Label resultLabel;
	@FXML private Button discardPrevButton;
	@FXML private Button keepPrevButton;
	@FXML private Label discardPrevLabel;
	@FXML private Label keepPrevLabel;

	private MainController mainController;

	private Stage stage;

	private final ResultList keptResults = new ResultList();
	private String prevResult;
	private String currentResult;

	@FXML
	public void cancelSelectionMode(ActionEvent evt) {
		if (!keptResults.isEmpty()) {
			// @formatterOff
			if (mainController.showConfirmAlert("Discard Results", "You will lose all your kept results.\n\n"
																		+ "Are you sure you want to cancel?",
																		"Keep them", "Discard them")) {
				// @formatterOn
				return;
			}
		}
		close();
	}

	public void close() {
		stage.close();
	}

	@FXML
	public void discardCurrent(ActionEvent evt) {
		nextResult(false);
	}

	@FXML
	public void discardPrevious(ActionEvent evt) {
		if (prevResult != null) {
			keptResults.remove(prevResult);
			updateUI(mainController.getPreferences(), mainController.getResults());
		}
	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;
	}

	@FXML
	public void keepCurrent(ActionEvent evt) {
		nextResult(true);
	}

	@FXML
	public void keepPrevious(ActionEvent evt) {
		if (prevResult != null) {
			keptResults.add(prevResult);
			updateUI(mainController.getPreferences(), mainController.getResults());
		}
	}

	@FXML
	public void keepResults(ActionEvent evt) {
		mainController.getResults().clear();
		mainController.getResults().addAll(keptResults);
		mainController.getResults().removeDuplicates();
		close();
		mainController.stateUpdated();
	}

	private void nextResult(boolean keepCurrent) {
		Preferences preferences = mainController.getPreferences();
		Generator generator = preferences.getGenerator();

		if (keepCurrent) {
			keptResults.add(currentResult);
		}
		prevResult = currentResult;
		this.currentResult = generator.query();
		updateUI(preferences, mainController.getResults());
	}

	void setRootNode(Parent parent) {
		this.stage = new Stage();
		stage.setScene(new Scene(parent));
		stage.setTitle("Generator: Selection Mode");
		stage.setOnShowing(evt -> {
			stage.initOwner(mainController.getStage());
			stage.initModality(Modality.WINDOW_MODAL);
		});
		stage.getScene().setOnKeyReleased(evt -> {
			switch (evt.getCode()) {
				case F:
					discardPrevious(null);
					break;
				case G:
					discardCurrent(null);
					break;
				case H:
					keepCurrent(null);
					break;
				case J:
					keepPrevious(null);
					break;
				default:
					break;
			}
		});
	}

	public void showSelectionMode() {
		keptResults.clear();
		prevResult = null;
		this.currentResult = mainController.getPreferences().getGenerator().query();

		updateUI(mainController.getPreferences(), mainController.getResults());
		stage.show();
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		discardPrevButton.setDisable(prevResult == null || !keptResults.contains(prevResult));
		keepPrevButton.setDisable(prevResult == null || keptResults.contains(prevResult));
		discardPrevLabel.setText(prevResult == null ? "-" : prevResult);
		keepPrevLabel.setText(prevResult == null ? "-" : prevResult);
		resultLabel.setText(currentResult);
		final int resultCount = preferences.getGenerator().getUniqueResultsAmount() - 1;
		statsLabel.setText(String.format("%s kept · %s discarded · %s total", keptResults.size(), resultCount - keptResults.size(), resultCount));
	}

	@FXML
	public void viewShortcuts(ActionEvent evt) {
		// @formatterOff
		mainController.showInfoAlert("Selection Mode Shortcuts",  "H\tKeep result\n"
																+ "J\tKeep previous\n"
																+ "G\tDiscard result\n"
																+ "F\tDiscard previous\n");
		// @formatterOn
	}
}