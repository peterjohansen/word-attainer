package com.actram.wordattainer.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.actram.wordattainer.ui.generator.GeneratorMode;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 *
 *
 * @author Peter André Johansen
 */
public class GeneratorController implements Initializable {
	@FXML private ToggleGroup generatorModeGroup;

	@FXML private RadioButton listModeRadioButton;
	@FXML private RadioButton selectionModeRadioButton;

	private WordAttainer program;

	private Map<RadioButton, GeneratorMode> radioButtonToGeneratorModeMap;
	private Map<GeneratorMode, RadioButton> generatorModeToRadioButtonMap;

	private void addGeneratorMode(GeneratorMode mode, RadioButton checkBox) {
		this.radioButtonToGeneratorModeMap.put(checkBox, mode);
		this.generatorModeToRadioButtonMap.put(mode, checkBox);
	}

	@FXML
	public void generate(ActionEvent event) {
		System.out.println("generate");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.program = WordAttainer.getInstance();

		this.radioButtonToGeneratorModeMap = new HashMap<>();
		this.generatorModeToRadioButtonMap = new HashMap<>();
		addGeneratorMode(GeneratorMode.LIST, listModeRadioButton);
		addGeneratorMode(GeneratorMode.SELECTION, selectionModeRadioButton);
		generatorModeGroup.selectedToggleProperty().addListener((ChangeListener<Toggle>) (observable, oldValue, newValue) -> {
			RadioButton radioButton = (RadioButton) newValue;
			GeneratorMode mode = radioButtonToGeneratorModeMap.get(radioButton);
			if (mode == null) {
				throw new AssertionError("no generator mode found for fxid: " + radioButton.getId());
			}
			program.setGeneratorMode(mode);
		});
		generatorModeToRadioButtonMap.get(program.getGeneratorMode()).setSelected(true);

	}

	public void setGeneratorMode(GeneratorMode mode) {
		program.setGeneratorMode(mode);
		generatorModeToRadioButtonMap.get(program.getGeneratorMode()).setSelected(true);
	}
}