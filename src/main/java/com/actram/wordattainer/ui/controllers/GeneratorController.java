package com.actram.wordattainer.ui.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.actram.wordattainer.Generator;
import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;
import com.actram.wordattainer.ui.generator.GeneratorMode;
import com.actram.wordattainer.util.BiMap;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

	@FXML private RadioButton listModeRadioButton;
	@FXML private RadioButton selectionModeRadioButton;

	private BiMap<GeneratorMode, RadioButton> modeRadioButtonMap;

	private MainController mainController;

	@FXML
	public void generate(ActionEvent event) {

		// Don't proceed if there are no morpheme lists
		Preferences preferences = mainController.getPreferences();
		if (preferences.getMorphemeFileList().isEmpty()) {
			mainController.showErrorAlert("Missing Morphemes", "At least one morpheme list must be added before results can be generated.");
			return;
		}

		// Update generator with current preferences
		Generator generator = preferences.getGenerator();
		try {
			generator.update(preferences);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Generate desired amount of results
		String[] generatedResults = new String[preferences.getResultAmount()];
		for (int i = 0; i < generatedResults.length; i++) {
			generatedResults[i] = generator.query();
		}

		// Copy generated results into results
		ResultList results = mainController.getResults();
		results.clear();
		results.addAll(Arrays.asList(generatedResults));

		mainController.stateUpdated();
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

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		modeRadioButtonMap.getSecondary(preferences.getGeneratorMode()).setSelected(true);
	}
}