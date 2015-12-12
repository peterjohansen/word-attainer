package com.actram.wordattainer.ui.controllers;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ResourceBundle;

import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
	@FXML private TextField resultTextField;
	@FXML private Button discardPrevButton;
	@FXML private Button keepPrevButton;
	@FXML private Label nextLabel;
	@FXML private Label prevLabel;
	@FXML private Parent choiceParent;

	private MainController mainController;

	private Stage stage;

	private final ResultList keptResults = new ResultList();
	private String prevResult;
	private String currentResult;

	private boolean stageShowedOnce = false;

	private boolean timedOut = false;

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

	@FXML
	public void editDone() {
		String result = resultTextField.getText().trim();
		if (!result.isEmpty()) {
			currentResult = result;
		}
		resultLabel.setVisible(true);
		resultTextField.setVisible(false);
		updateUI(mainController.getPreferences(), mainController.getResults());
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
		mainController.getGeneratorController().generateDone();
		close();
		mainController.stateUpdated();
	}

	private String nextResult() {
		try {
			return currentResult = mainController.getPreferences().getGenerator().query();
		} catch (InterruptedByTimeoutException e) {
			keepResults(null);
			mainController.getGeneratorController().showTimedOutMessage();
			return null;
		}
	}

	private void nextResult(boolean keepCurrent) {
		if (keepCurrent) {
			keptResults.add(currentResult);
		}
		prevResult = currentResult;
		currentResult = null;

		this.currentResult = nextResult();
		updateUI(mainController.getPreferences(), mainController.getResults());

	}

	void setRootNode(Parent parent) {
		this.stage = new Stage();
		stage.setScene(new Scene(parent));
		stage.setTitle("Generator: Selection Mode");
		stage.getScene().setOnKeyReleased(evt -> {
			if (resultTextField.isVisible() || timedOut) {
				return;
			}
			switch (evt.getCode()) {
				case F:
					if (!discardPrevButton.isDisabled()) {
						discardPrevious(null);
					}
					break;
				case G:
					discardCurrent(null);
					break;
				case H:
					keepCurrent(null);
					break;
				case J:
					if (!keepPrevButton.isDisable()) {
						keepPrevious(null);
					}
					break;
				default:
					break;
			}
		});
	}

	public void showSelectionMode() {
		keptResults.clear();
		prevResult = null;
		choiceParent.setDisable(false);
		timedOut = false;

		if (!stageShowedOnce) {
			stage.initOwner(mainController.getStage());
			stage.initModality(Modality.WINDOW_MODAL);
		}
		stage.show();
		stageShowedOnce = true;
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());

		this.currentResult = nextResult();
		updateUI(mainController.getPreferences(), mainController.getResults());
	}

	@FXML
	public void startEdit(MouseEvent evt) {
		if (evt.getClickCount() == 2) {
			resultTextField.setText(currentResult);
			resultLabel.setVisible(false);
			resultTextField.setVisible(true);
			resultTextField.selectAll();
		}
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		discardPrevButton.setDisable(prevResult == null || !keptResults.contains(prevResult));
		keepPrevButton.setDisable(prevResult == null || keptResults.contains(prevResult));
		resultLabel.setText(currentResult);
		prevLabel.setText(prevResult == null ? "-" : prevResult);
		final int resultCount = preferences.getGenerator().getUniqueResultsAmount() - 1;
		statsLabel.setText(String.format("%s kept | %s discarded | %s total", keptResults.size(), resultCount - keptResults.size(), resultCount));
	}

	@FXML
	public void viewShortcuts(ActionEvent evt) {
		// @formatterOff
		mainController.showInfoAlert("Selection Mode Shortcuts",  "Shortcuts:\n"
																+ "H\tKeep result\n"
																+ "J\tKeep previous\n"
																+ "G\tDiscard result\n"
																+ "F\tDiscard previous\n");
		// @formatterOn
	}
}