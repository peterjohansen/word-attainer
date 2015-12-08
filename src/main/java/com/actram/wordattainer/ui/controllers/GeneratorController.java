package com.actram.wordattainer.ui.controllers;

import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

import com.actram.wordattainer.Generator;
import com.actram.wordattainer.GeneratorSettings;
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
		mainController.accessGeneratorSettings(settings -> {
			mainController.accessPreferences(preferences -> {
				Generator generator = mainController.getResultGenerator();
				try {
					generator.update(settings);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				Random random = new Random();
				String[] generatedResults = new String[preferences.getResultAmount()];
				for (int i = 0; i < generatedResults.length; i++) {
					generatedResults[i] = generator.query();
				}
				mainController.accessResults(results -> {
					results.clear();
					results.addAll(Arrays.asList(generatedResults));
				});
			});
		});
	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;

		this.modeRadioButtonMap = new BiMap<>();
		modeRadioButtonMap.put(GeneratorMode.LIST, listModeRadioButton);
		modeRadioButtonMap.put(GeneratorMode.SELECTION, selectionModeRadioButton);
		generatorModeGroup.selectedToggleProperty().addListener((ChangeListener<Toggle>) (observable, oldValue, newValue) -> {
			RadioButton radioButton = (RadioButton) newValue;
			GeneratorMode mode = modeRadioButtonMap.getPrimary(radioButton);
			if (mode == null) {
				throw new AssertionError("no generator mode found for fx:id: " + radioButton.getId());
			}
			mainController.accessPreferences(preferences -> {
				preferences.setGeneratorMode(mode);
			});
		});
	}

	@Override
	public void updateUI(Preferences preferences, GeneratorSettings generatorSettings, ResultList results) {
		modeRadioButtonMap.getSecondary(preferences.getGeneratorMode()).setSelected(true);
	}
}