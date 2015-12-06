package com.actram.wordattainer.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.actram.wordattainer.ui.generator.GeneratorMode;
import com.actram.wordattainer.util.BiMap;

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
	private MainController mainController;

	private BiMap<GeneratorMode, RadioButton> modeRadioButtonMap;

	@FXML
	public void generate(ActionEvent event) {
		System.out.println("generate");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.program = WordAttainer.getInstance();
		this.mainController = program.getMainController();

		/*
		this.modeRadioButtonMap = new BiMap<>();
		modeRadioButtonMap.put(GeneratorMode.LIST, listModeRadioButton);
		modeRadioButtonMap.put(GeneratorMode.SELECTION, selectionModeRadioButton);
		generatorModeGroup.selectedToggleProperty().addListener((ChangeListener<Toggle>) (observable, oldValue, newValue) -> {
			RadioButton radioButton = (RadioButton) newValue;
			GeneratorMode mode = modeRadioButtonMap.getPrimary(radioButton);
			if (mode == null) {
				throw new AssertionError("no generator mode found for fxid: " + radioButton.getId());
			}
			mainController.setGeneratorMode(mode);
		});
		updateGeneratorMode();
		*/
	}

	public void updateGeneratorMode() {
		modeRadioButtonMap.getSecondary(mainController.getGeneratorMode()).setSelected(true);
	}
}