package com.actram.wordattainer.ui.controllers;

import java.io.IOException;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
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

	private boolean generationRunning = false;
	private boolean lastGenerationTimedOut = false;

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

	private boolean generateInit() {
		if (generationRunning) {
			setGenerationRunning(false);
			return false;
		}

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

		// Event handler to run when the generation is done
		ProgressBar progressBar = mainController.getResultsController().generateProgressBar;
		ResultList generatedResults = new ResultList(preferences.getResultAmount());
		EventHandler<WorkerStateEvent> onTaskDone = new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				setGenerationRunning(false);
				progressBar.setVisible(false);
				ResultList results = mainController.getResults();
				results.clear();
				results.addAll(generatedResults);
				if (preferences.isResultsAutoSort()) {
					results.sort();
				}

				if (lastGenerationTimedOut) {
					// @formatterOff
							int timeoutSeconds = preferences.getGeneratorTimeout();
							mainController.showInfoAlert("Generation timed out",
												"The generator timed out.\n\n"
													+ "This means that the generator didn't find any "
													+ "new results for " + timeoutSeconds + " seconds.");
							// @formatterOn
					lastGenerationTimedOut = false;
				}

				mainController.stateUpdated();
			}
		};

		// Task to run the generation. Cancel the task if it times out.
		Task<ResultList> generateTask = new Task<ResultList>() {
			@Override
			protected ResultList call() throws Exception {
				long start = System.currentTimeMillis();
				while (generatedResults.size() != preferences.getResultAmount()) {
					if ((System.currentTimeMillis() - start) > preferences.getGeneratorTimeout() * 1000) {
						lastGenerationTimedOut = true;
						break;
					}
					if (isCancelled() || !generationRunning) {
						break;
					}
					progressBar.setProgress(1 - ((double) 1 / (preferences.getResultAmount() - generatedResults.size())));
					String result = generator.query();
					if (!generatedResults.contains(result)) {
						generatedResults.add(result);
						start = System.currentTimeMillis();
					}
				}
				return generatedResults;
			}
		};
		generateTask.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, onTaskDone);
		generateTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, onTaskDone);
		progressBar.setVisible(true);
		setGenerationRunning(true);
		new Thread(generateTask).start();
	}

	public void selectionGenerate() {
		if (!generateInit()) {
			return;
		}
		mainController.getSelectionModeController().showSelectionMode();
	}

	private void setGenerationRunning(boolean running) {
		this.generationRunning = running;
		if (generationRunning) {
			generateButton.setText("Cancel");
		} else {
			generateButton.setText("Generate");
		}
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		modeRadioButtonMap.getSecondary(preferences.getGeneratorMode()).setSelected(true);
	}
}