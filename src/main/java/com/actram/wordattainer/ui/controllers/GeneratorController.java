package com.actram.wordattainer.ui.controllers;

import java.io.IOException;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ResourceBundle;

import com.actram.wordattainer.Generator;
import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;
import com.actram.wordattainer.ui.generator.GeneratorMode;
import com.actram.wordattainer.util.BiMap;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 *
 *
 * @author Peter André Johansen
 */
public class GeneratorController implements MainControllerChild {
	@FXML private ToggleGroup generatorModeGroup;

	@FXML private Button generateButton;

	@FXML private RadioButton listModeRadioButton;
	@FXML private RadioButton selectionModeRadioButton;

	private BiMap<GeneratorMode, RadioButton> modeRadioButtonMap;

	private MainController mainController;

	@FXML
	public void generate(ActionEvent event) {
		GeneratorMode mode = mainController.getPreferences().getGeneratorMode();
		if (mode == GeneratorMode.LIST) {
			listGenerate();
		} else if (mode == GeneratorMode.SELECTION) {
			selectionGenerate();
		} else {
			throw new AssertionError("unkown generator mode");
		}
	}

	void generateDone() {
		if (mainController.getPreferences().isResultsAutoSort()) {
			mainController.getResults().sort();
		}
		mainController.stateUpdated();
	}

	private boolean generateInit() {

		// Don't proceed if there are no morpheme lists
		Preferences preferences = mainController.getPreferences();
		if (preferences.getMorphemeFileList().isEmpty()) {
			mainController.showErrorAlert("Missing Morphemes", "At least one morpheme list must be added before results can be generated.");
			return false;
		}

		// Update generator with current preferences
		Generator generator = preferences.getGenerator();
		try {
			generator.update(preferences);
		} catch (IOException e) {
			e.printStackTrace();
			mainController.showErrorAlert("Generator Error", "Unable to generate words.");
		}

		return true;
	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;

		this.modeRadioButtonMap = new BiMap<>();
		modeRadioButtonMap.put(GeneratorMode.LIST, listModeRadioButton);
		modeRadioButtonMap.put(GeneratorMode.SELECTION, selectionModeRadioButton);
		generatorModeGroup.selectedToggleProperty().addListener((ChangeListener<Toggle>) (observable, oldValue, newValue) -> {
			RadioButton radioButton = (RadioButton) newValue;
			if (newValue != null) {
				GeneratorMode mode = modeRadioButtonMap.getPrimary(radioButton);
				if (mode == null) {
					throw new AssertionError("no generator mode found for fx:id: " + radioButton.getId());
				}
				mainController.getPreferences().setGeneratorMode(mode);
				mainController.stateUpdated();
			}
		});
	}

	public void listGenerate() {
		if (!generateInit()) {
			return;
		}

		Preferences preferences = mainController.getPreferences();
		Generator generator = preferences.getGenerator();
		ResultList generatedResults = new ResultList(preferences.getResultAmount());

		Task<Void> generateTask = new Task<Void>() {
			@Override
			protected Void call() {
				for (int i = 0; i < preferences.getResultAmount(); i++) {
					try {
						generatedResults.add(generator.query());
					} catch (InterruptedByTimeoutException e) {
						showTimedOutMessage();
						cancel();
					}
				}
				return null;
			}
		};
		generateTask.addEventHandler(WorkerStateEvent.ANY, event -> {
			if (event.getEventType() == WorkerStateEvent.WORKER_STATE_SUCCEEDED || event.getEventType() == WorkerStateEvent.WORKER_STATE_FAILED) {
				generateButton.setDisable(false);
				ResultList results = mainController.getResults();
				results.clear();
				results.addAll(generatedResults);
				generateDone();
			}
		});
		generateButton.setDisable(true);
		new Thread(generateTask).start();
	}

	public void selectionGenerate() {
		if (!generateInit()) {
			return;
		}
		mainController.getSelectionModeController().showSelectionMode();
	}

	void showTimedOutMessage() {
		int timeoutSeconds = mainController.getPreferences().getGeneratorTimeout();
		// @formatterOff
		mainController.showInfoAlert("Generation Timed Out",
								"The generator didn't find any "
								+ "new results for " + timeoutSeconds + " seconds.");
		// @formatterOn
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		modeRadioButtonMap.getSecondary(preferences.getGeneratorMode()).setSelected(true);
	}
}