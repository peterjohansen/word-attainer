package com.actram.wordattainer.ui.controllers;

import java.util.ResourceBundle;

import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class PreferencesController extends Parent implements MainControllerChild {
	private MainController mainController;

	// General -> General
	@FXML private ComboBox<?> languageComboBox;

	// General -> Results
	@FXML private Spinner<?> timeoutSpinner;
	@FXML private Spinner<?> resultAmountSpinner;
	@FXML private CheckBox autoSortCheckBox;

	// Generator Settings -> Profile
	@FXML private Label lastProfileLabel;

	// Generator Settings -> Characters
	@FXML private CheckBox lettersCheckBox;
	@FXML private CheckBox digitsCheckBox;
	@FXML private CheckBox punctuationCheckBox;
	@FXML private CheckBox customCheckBox;
	@FXML private TextField customTextField;

	// Generator Settings -> Morpheme count
	@FXML private RadioButton exactValueRadioButton;
	@FXML private ToggleGroup morphemeCountGroup;
	@FXML private Spinner<?> exactValueTextField;
	@FXML private RadioButton variableValueRadioButton;
	@FXML private Spinner<?> minValueTextField;
	@FXML private Spinner<?> maxValueTextField;

	// Generator Settings -> Miscellaneous
	@FXML private ComboBox<?> capitalizationComboBox;
	@FXML private CheckBox allowDuplicateConsecutiveCheckBox;
	@FXML private CheckBox mapMorphemesToListsCheckBox;

	private Stage stage;
	private Preferences preferences;

	@FXML
	public void cancelPreferences(ActionEvent event) {
		stage.hide();
	}

	@FXML
	public void exportProfile(ActionEvent event) {

	}

	@FXML
	public void importProfile(ActionEvent event) {

	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;

		lettersCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			preferences.getCharacterValidator().allowLetters(newValue);
		});
		digitsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			preferences.getCharacterValidator().allowDigits(newValue);
		});
		punctuationCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			preferences.getCharacterValidator().allowPunctuation(newValue);
		});
		customCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			preferences.getCharacterValidator().allowCustom(newValue);
		});
		customTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			preferences.getCharacterValidator().setCustomCharacters(newValue.toCharArray());
		});

	}

	@FXML
	public void resetSettingsToProfile(ActionEvent event) {

	}

	@FXML
	public void savePreferences(ActionEvent event) {
		mainController.getPreferences().setTo(this.preferences);
		stage.hide();
	}

	@FXML
	public void setDefaultCharacters(ActionEvent evt) {
		preferences.getCharacterValidator().setToDefault();
	}

	@FXML
	public void setDefaultMiscellaneous(ActionEvent evt) {

	}

	@FXML
	public void setDefaultMorphemeCount(ActionEvent evt) {

	}

	void setStageParent(Parent parent) {
		this.stage = new Stage();
		stage.setScene(new Scene(parent));
		stage.setTitle("Preferences");
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}

	public void showPreferences() {
		stage.show();
		this.preferences = new Preferences().setTo(mainController.getPreferences());
		updateUI(mainController.getPreferences(), mainController.getResults());
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		if (this.preferences != null) {
			lettersCheckBox.setSelected(this.preferences.getCharacterValidator().isLettersAllowed());
			digitsCheckBox.setSelected(this.preferences.getCharacterValidator().isDigitsAllowed());
			punctuationCheckBox.setSelected(this.preferences.getCharacterValidator().isPunctuationAllowed());
			customCheckBox.setSelected(this.preferences.getCharacterValidator().isCustomAllowed());
			customTextField.setText(this.preferences.getCharacterValidator().getCustomCharactersString());
		}
	}
}