package com.actram.wordattainer.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

/**
 *
 *
 * @author Peter André Johansen
 */
public class GeneratorController implements Initializable {
	@FXML private ToggleGroup generatorModeGroup;

	@FXML
	public void generate(ActionEvent event) {
		System.out.println("generate");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}