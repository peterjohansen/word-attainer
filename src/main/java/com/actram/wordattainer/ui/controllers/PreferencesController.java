package com.actram.wordattainer.ui.controllers;

import static com.actram.wordattainer.GeneratorSettings.MAX_MORPHEME_COUNT;
import static com.actram.wordattainer.GeneratorSettings.MIN_MORPHEME_COUNT;
import static com.actram.wordattainer.ui.Preferences.MAX_GENERATOR_TIMEOUT;
import static com.actram.wordattainer.ui.Preferences.MAX_RESULT_AMOUNT;
import static com.actram.wordattainer.ui.Preferences.MIN_GENERATOR_TIMEOUT;
import static com.actram.wordattainer.ui.Preferences.MIN_RESULT_AMOUNT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import com.actram.wordattainer.GeneratorSettings;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public class PreferencesController extends Parent implements MainControllerChild {
	private MainController mainController;

	// General -> General
	@FXML private ComboBox<String> languageComboBox;

	// General -> Results
	@FXML private Spinner<Integer> timeoutSpinner;
	@FXML private Spinner<Integer> resultAmountSpinner;
	@FXML private CheckBox autoSortCheckBox;

	// Generator Settings -> Profile
	@FXML private Label lastProfileLabel;

	// Generator Settings -> Characters
	@FXML private CheckBox lettersCheckBox;
	@FXML private CheckBox digitsCheckBox;
	@FXML private CheckBox punctuationCheckBox;
	@FXML private CheckBox customCheckBox;
	@FXML private TextField customTextField;
	@FXML private CheckBox vowelCheckBox;
	@FXML private Spinner<Integer> vowelSpinner;
	@FXML private CheckBox consonantCheckBox;
	@FXML private Spinner<Integer> consonantSpinner;

	// Generator Settings -> Morpheme count
	@FXML private RadioButton exactValueRadioButton;
	@FXML private ToggleGroup morphemeCountGroup;
	@FXML private Spinner<Integer> exactValueSpinner;
	@FXML private RadioButton variableValueRadioButton;
	@FXML private Spinner<Integer> minValueSpinner;
	@FXML private Spinner<Integer> maxValueSpinner;

	// Generator Settings -> Miscellaneous
	@FXML private ComboBox<ResultCase> capitalizationComboBox;
	@FXML private Label capitalizationExampleLabel;
	@FXML private TextField separatorTextField;
	@FXML private CheckBox allowDuplicateConsecutiveCheckBox;
	@FXML private CheckBox mapMorphemesToListsCheckBox;

	private FileChooser profileFileChooser;

	private Stage stage;
	private Preferences preferences;
	private Preferences lastProfileLoaded;

	private boolean stageShowedOnce = false;

	@FXML
	public void cancelPreferences(ActionEvent event) {
		stage.hide();
	}

	@FXML
	public void exportProfile(ActionEvent event) {
		exportProfile(true);
	}

	public void exportProfile(boolean current) {
		File file = profileFileChooser.showSaveDialog(stage);

		if (file != null) {
			try (OutputStream os = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(os)) {
				oos.writeObject(current ? getPreferences() : mainController.getPreferences());
			} catch (IOException e) {
				e.printStackTrace();
				mainController.showErrorAlert("Profile Export Error", "Unable to export profile:\n" + file);
			}
		}
	}

	public Preferences getPreferences() {
		if (preferences == null) {
			preferences = new Preferences();
		}
		return this.preferences;
	}

	@FXML
	public void importProfile(ActionEvent evt) {
		File file = profileFileChooser.showOpenDialog(stage);

		if (file != null) {
			try (InputStream is = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(is)) {
				lastProfileLoaded = (Preferences) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				mainController.showErrorAlert("Profile import error", "Unable to import profile:\n" + file);
			}
			if (lastProfileLoaded != null) {
				lastProfileLabel.setText(file.toPath().getFileName().toString());
				getPreferences().setTo(lastProfileLoaded);
				if (!stage.isShowing()) {
					savePreferences(evt);
				}
				updateUI(getPreferences(), mainController.getResults());
			}
		}
	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;

		this.profileFileChooser = new FileChooser();
		profileFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		profileFileChooser.setTitle("Select Profile File");
		profileFileChooser.getExtensionFilters().add(new ExtensionFilter("Word Attainer Profile", "*.wap"));
		this.lastProfileLabel.setText("-");

		// General -> General
		languageComboBox.setItems(FXCollections.observableArrayList("English")); // TODO
																					// I18N
		languageComboBox.getSelectionModel().select("English");

		// General -> Results
		timeoutSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_GENERATOR_TIMEOUT, MAX_GENERATOR_TIMEOUT));
		timeoutSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setGeneratorTimeout(newValue);
		});
		resultAmountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_RESULT_AMOUNT, MAX_RESULT_AMOUNT));
		resultAmountSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setResultAmount(newValue);
		});
		autoSortCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setAutoSortResults(newValue);
		});

		// Generator Settings -> Profile

		// Generator Settings -> Characters
		lettersCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().getCharacterValidator().allowLetters(newValue);
		});
		digitsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().getCharacterValidator().allowDigits(newValue);
		});
		punctuationCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().getCharacterValidator().allowPunctuation(newValue);
		});
		customCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().getCharacterValidator().allowCustom(newValue);
		});
		customTextField.disableProperty().bind(customCheckBox.selectedProperty().not());
		customTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().getCharacterValidator().setCustomCharacters(newValue.toCharArray());
		});
		vowelCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setTrimVowels(newValue);
		});
		vowelSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setTrimVowelCount(newValue);
		});
		vowelSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(GeneratorSettings.MIN_TRIM_COUNT, Integer.MAX_VALUE));
		vowelSpinner.disableProperty().bind(vowelCheckBox.selectedProperty().not());
		consonantCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setTrimConsonants(newValue);
		});
		consonantSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setTrimConsonantCount(newValue);
		});
		consonantSpinner.disableProperty().bind(consonantCheckBox.selectedProperty().not());
		consonantSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(GeneratorSettings.MIN_TRIM_COUNT, Integer.MAX_VALUE));

		// Generator Settings -> Morpheme count
		exactValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_MORPHEME_COUNT, MAX_MORPHEME_COUNT));
		exactValueSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setExactMorphemeCount(newValue);
		});
		exactValueRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setUseExactMorphemeCount(newValue);
		});
		exactValueSpinner.disableProperty().bind(exactValueRadioButton.selectedProperty().not());
		variableValueRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setUseExactMorphemeCount(!newValue);
		});
		minValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_MORPHEME_COUNT, MAX_MORPHEME_COUNT));
		maxValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_MORPHEME_COUNT, MAX_MORPHEME_COUNT));
		minValueSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setMorphemeCountRange(newValue, getPreferences().getMaxMorphemeCount());
			updateUI(preferences, mainController.getResults());
		});
		maxValueSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setMorphemeCountRange(getPreferences().getMinMorphemeCount(), newValue);
			updateUI(preferences, mainController.getResults());
		});
		minValueSpinner.disableProperty().bind(variableValueRadioButton.selectedProperty().not());
		maxValueSpinner.disableProperty().bind(variableValueRadioButton.selectedProperty().not());

		// Generator Settings -> Miscellaneous
		capitalizationComboBox.setItems(FXCollections.observableArrayList(ResultCase.values()));
		capitalizationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setMorphemeCapitalization(newValue);
			updateUI(preferences, mainController.getResults());
		});
		separatorTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setMorphemeSeparator(newValue);
		});
		allowDuplicateConsecutiveCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().allowDuplicateConsecutiveMorphemes(newValue);
		});
		mapMorphemesToListsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			getPreferences().setMapListsToMorphemes(newValue);
		});

	}

	@FXML
	public void resetSettingsToProfile(ActionEvent event) {
		if (lastProfileLoaded == null) {
			// @formatterOff
			mainController.showErrorAlert("No Profile Loaded",
											"Cannot reset preferences to the last profile "
											+ "because no profile has been imported yet.");
			//@formatterOn
		} else {
			// @formatterOff
			if (mainController.showConfirmAlert("Reset Preferences",
												"This will reset all your preferences to the last imported profile.",
												"Reset preferences", "Cancel")) {
				getPreferences().setTo(lastProfileLoaded);
				// @formatterOn
				updateUI(getPreferences(), mainController.getResults());
			}
		}
	}

	@FXML
	public void savePreferences(ActionEvent event) {
		mainController.getPreferences().setTo(getPreferences());
		mainController.stateUpdated();
		stage.hide();
	}

	@FXML
	public void setDefaultCharacters(ActionEvent evt) {
		getPreferences().setCharactersToDefault();
		updateUI(getPreferences(), mainController.getResults());
	}

	@FXML
	public void setDefaultMiscellaneous(ActionEvent evt) {
		getPreferences().setMorphemeCapitalizationToDefault();
		getPreferences().setMorphemeSeparatorToDefault();
		getPreferences().setAllowDuplicateConsecutiveMorphemesToDefault();
		getPreferences().setMapListsToMorphemesToDefault();
		updateUI(getPreferences(), mainController.getResults());
	}

	@FXML
	public void setDefaultMorphemeCount(ActionEvent evt) {
		getPreferences().setMorphemeCountToDefault();
		updateUI(getPreferences(), mainController.getResults());
	}

	@FXML
	public void setDefaultResults(ActionEvent evt) {
		getPreferences().setResultAmountToDefault();
		getPreferences().setGeneratorTimeoutToDefault();
		getPreferences().setAutoSortResultsToDefault();
		updateUI(getPreferences(), mainController.getResults());
	}

	void setRootNode(Parent parent) {
		this.stage = new Stage();
		stage.setScene(new Scene(parent));
		stage.setTitle("Preferences");
	}

	public void showPreferences() {
		getPreferences().setTo(mainController.getPreferences());
		updateUI(mainController.getPreferences(), mainController.getResults());
		if (!stageShowedOnce) {
			stage.initOwner(mainController.getStage());
			stage.initModality(Modality.WINDOW_MODAL);
		}
		stage.show();
		stageShowedOnce = true;
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {

		// General -> Results
		timeoutSpinner.getValueFactory().setValue(getPreferences().getGeneratorTimeout());
		resultAmountSpinner.getValueFactory().setValue(getPreferences().getResultAmount());
		autoSortCheckBox.setSelected(getPreferences().isResultsAutoSort());

		// Generator Settings -> Characters
		lettersCheckBox.setSelected(getPreferences().getCharacterValidator().isLettersAllowed());
		digitsCheckBox.setSelected(getPreferences().getCharacterValidator().isDigitsAllowed());
		punctuationCheckBox.setSelected(getPreferences().getCharacterValidator().isPunctuationAllowed());
		customCheckBox.setSelected(getPreferences().getCharacterValidator().isCustomAllowed());
		customTextField.setText(getPreferences().getCharacterValidator().getCustomCharactersString());
		vowelCheckBox.setSelected(getPreferences().isVowelsTrimmed());
		vowelSpinner.getValueFactory().setValue(getPreferences().getTrimVowelCount());
		consonantCheckBox.setSelected(getPreferences().isConsonantsTrimmed());
		consonantSpinner.getValueFactory().setValue(getPreferences().getTrimConsonantCount());

		// Generator Settings -> Morpheme count
		exactValueRadioButton.setSelected(getPreferences().isExactMorphemeCount());
		exactValueSpinner.getValueFactory().setValue(getPreferences().getExactMorphemeCount());
		variableValueRadioButton.setSelected(!getPreferences().isExactMorphemeCount());
		minValueSpinner.getValueFactory().setValue(getPreferences().getMinMorphemeCount());
		maxValueSpinner.getValueFactory().setValue(getPreferences().getMaxMorphemeCount());
		((SpinnerValueFactory.IntegerSpinnerValueFactory) minValueSpinner.getValueFactory()).setMax(getPreferences().getMaxMorphemeCount());
		((SpinnerValueFactory.IntegerSpinnerValueFactory) maxValueSpinner.getValueFactory()).setMin(getPreferences().getMinMorphemeCount());

		// Generator Settings -> Miscellaneous
		capitalizationComboBox.getSelectionModel().select(preferences.getMorphemeCapitalization());
		String[] sentenceParts = capitalizationExampleLabel.getText().split(" ");
		preferences.getMorphemeCapitalization().fixCapitalization(sentenceParts);
		String sentence = "";
		for (String part : sentenceParts) {
			sentence += part + " ";
		}
		capitalizationExampleLabel.setText(sentence);
		separatorTextField.setText(getPreferences().getMorphemeSeparator());
		allowDuplicateConsecutiveCheckBox.setSelected(getPreferences().isDuplicateConsecutiveMorphemesAllowed());
		mapMorphemesToListsCheckBox.setSelected(getPreferences().isMorphemesMappedToLists());

	}
}