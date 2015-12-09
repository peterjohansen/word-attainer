package com.actram.wordattainer.ui.controllers;

import static com.actram.wordattainer.GeneratorSettings.MAX_MORPHEME_COUNT;
import static com.actram.wordattainer.GeneratorSettings.MIN_MORPHEME_COUNT;

import java.util.ResourceBundle;

import com.actram.wordattainer.ResultCase;
import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
	@FXML private Spinner<Integer> exactValueSpinner;
	@FXML private RadioButton variableValueRadioButton;
	@FXML private Spinner<Integer> minValueSpinner;
	@FXML private Spinner<Integer> maxValueSpinner;

	// Generator Settings -> Miscellaneous
	@FXML private ComboBox<ResultCase> capitalizationComboBox;
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

		// Generator Settings -> Profile

		// Generator Settings -> Characters
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

		// Generator Settings -> Morpheme count
		exactValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_MORPHEME_COUNT, MAX_MORPHEME_COUNT));
		exactValueSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			preferences.setExactMorphemeCount(newValue);
		});
		exactValueRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
			preferences.setUseExactMorphemeCount(newValue);
		});
		variableValueRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
			preferences.setUseExactMorphemeCount(!newValue);
		});
		minValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_MORPHEME_COUNT, MAX_MORPHEME_COUNT));
		maxValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_MORPHEME_COUNT, MAX_MORPHEME_COUNT));
		minValueSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			preferences.setMorphemeCountRange(newValue, preferences.getMaxMorphemeCount());
		});
		maxValueSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			preferences.setMorphemeCountRange(preferences.getMinMorphemeCount(), newValue);
		});


		// Generator Settings -> Miscellaneous
		capitalizationComboBox.setItems(FXCollections.observableArrayList(ResultCase.values()));
		allowDuplicateConsecutiveCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			preferences.allowDuplicateConsecutiveMorphemes(newValue);
		});
		mapMorphemesToListsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			preferences.setMapListsToMorphemes(newValue);
		});

	}

	@FXML
	public void resetSettingsToProfile(ActionEvent event) {

	}

	@FXML
	public void savePreferences(ActionEvent event) {
		mainController.getPreferences().setTo(this.preferences);
		mainController.stateUpdated();
		stage.hide();
	}

	@FXML
	public void setDefaultCharacters(ActionEvent evt) {
		preferences.getCharacterValidator().setToDefault();
		updateUI(preferences, mainController.getResults());
	}

	@FXML
	public void setDefaultMiscellaneous(ActionEvent evt) {

	}

	@FXML
	public void setDefaultMorphemeCount(ActionEvent evt) {
		preferences.setMorphemeCountToDefault();
		updateUI(preferences, mainController.getResults());
	}

	void setStageParent(Parent parent) {
		this.stage = new Stage();
		stage.setScene(new Scene(parent));
		stage.setTitle("Preferences");
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}

	public void showPreferences() {
		this.preferences = new Preferences().setTo(mainController.getPreferences());
		updateUI(mainController.getPreferences(), mainController.getResults());
		stage.show();
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		if (this.preferences != null) {

			// Generator Settings -> Characters
			lettersCheckBox.setSelected(this.preferences.getCharacterValidator().isLettersAllowed());
			digitsCheckBox.setSelected(this.preferences.getCharacterValidator().isDigitsAllowed());
			punctuationCheckBox.setSelected(this.preferences.getCharacterValidator().isPunctuationAllowed());
			customCheckBox.setSelected(this.preferences.getCharacterValidator().isCustomAllowed());
			customTextField.setText(this.preferences.getCharacterValidator().getCustomCharactersString());

			// Generator Settings -> Morpheme count
			exactValueRadioButton.setSelected(this.preferences.isExactMorphemeCount());
			exactValueSpinner.getValueFactory().setValue(this.preferences.getExactMorphemeCount());
			variableValueRadioButton.setSelected(!this.preferences.isExactMorphemeCount());
			minValueSpinner.getValueFactory().setValue(this.preferences.getMinMorphemeCount());
			maxValueSpinner.getValueFactory().setValue(this.preferences.getMaxMorphemeCount());
			((SpinnerValueFactory.IntegerSpinnerValueFactory) minValueSpinner.getValueFactory()).setMax(this.preferences.getMaxMorphemeCount());
			((SpinnerValueFactory.IntegerSpinnerValueFactory) maxValueSpinner.getValueFactory()).setMin(this.preferences.getMinMorphemeCount());

			// Generator Settings -> Miscellaneous
			capitalizationComboBox.getSelectionModel().select(preferences.getMorphemeCapitalization());
			allowDuplicateConsecutiveCheckBox.setSelected(this.preferences.isDuplicateConsecutiveMorphemesAllowed());
			mapMorphemesToListsCheckBox.setSelected(this.preferences.isMorphemesMappedToLists());

		}
	}
}