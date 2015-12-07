package com.actram.wordattainer.ui.controllers;

import java.util.ResourceBundle;

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

	@FXML
	public void generate(ActionEvent event) {
		System.out.println("generate");
	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
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