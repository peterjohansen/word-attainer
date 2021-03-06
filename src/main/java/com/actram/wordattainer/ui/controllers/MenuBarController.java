package com.actram.wordattainer.ui.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;
import com.actram.wordattainer.ui.generator.GeneratorMode;
import com.actram.wordattainer.util.BiMap;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 *
 * @author Peter André Johansen
 */
public class MenuBarController implements MainControllerChild {
	private MainController mainController;

	private BiMap<GeneratorMode, RadioMenuItem> modeRadioItemMap;

	@FXML private ToggleGroup generatorModeGroup;
	@FXML private RadioMenuItem listModeRadioItem;
	@FXML private RadioMenuItem selectionModeRadioItem;

	private FileChooser saveResultsFileChooser;
	private FileChooser createListFileChooser;

	@FXML
	public void addLists(ActionEvent evt) {
		mainController.getMorphemeListsController().addLists(evt);
	}

	@FXML
	public void clearLists(ActionEvent evt) {
		mainController.getMorphemeListsController().clearLists(evt);
	}

	@FXML
	public void createNewList(ActionEvent evt) {
		File file = createListFileChooser.showSaveDialog(mainController.getStage());
		if (file != null) {
			try {
				file.createNewFile();
				Desktop.getDesktop().edit(file);
			} catch (IOException e) {
				e.printStackTrace();
				mainController.showErrorAlert("File Save Error", "Unable to save morpheme list file.");
			}
		}
	}

	@FXML
	public void editPreferences(ActionEvent evt) {
		mainController.getPreferencesController().showPreferences();
	}

	@FXML
	public void exit(ActionEvent evt) {
		Platform.exit();
	}

	@FXML
	public void exportProfile(ActionEvent evt) {
		mainController.getPreferencesController().exportProfile(false);
	}

	@FXML
	public void generateResults(ActionEvent evt) {
		mainController.getGeneratorController().generate(evt);
	}

	@FXML
	public void importProfile(ActionEvent evt) {
		mainController.getPreferencesController().importProfile(evt);
	}
	
	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;

		this.saveResultsFileChooser = new FileChooser();
		saveResultsFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		saveResultsFileChooser.setTitle("Save Results");
		ExtensionFilter defaultExtensionFilter = new ExtensionFilter("Text file", "*.txt");
		saveResultsFileChooser.getExtensionFilters().add(defaultExtensionFilter);
		saveResultsFileChooser.getExtensionFilters().add(new ExtensionFilter("Any type", "*"));
		saveResultsFileChooser.setSelectedExtensionFilter(defaultExtensionFilter);
		
		this.createListFileChooser = new FileChooser();
		createListFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		createListFileChooser.setTitle("Choose Where To Create Morpheme List");
		createListFileChooser.getExtensionFilters().add(defaultExtensionFilter);
		createListFileChooser.getExtensionFilters().add(new ExtensionFilter("Any type", "*"));
		createListFileChooser.setSelectedExtensionFilter(defaultExtensionFilter);

		this.modeRadioItemMap = new BiMap<>();
		modeRadioItemMap.put(GeneratorMode.LIST, listModeRadioItem);
		modeRadioItemMap.put(GeneratorMode.SELECTION, selectionModeRadioItem);
		generatorModeGroup.selectedToggleProperty().addListener((ChangeListener<Toggle>) (observable, oldValue, newValue) -> {
			RadioMenuItem radioItem = (RadioMenuItem) newValue;
			if (newValue != null) {
				GeneratorMode mode = modeRadioItemMap.getPrimary(radioItem);
				if (mode == null) {
					throw new AssertionError("no generator mode found for fx:id: " + radioItem.getId());
				}
				mainController.getPreferences().setGeneratorMode(mode);
				mainController.stateUpdated();
			}
		});
	}

	@FXML
	public void mergeResults(ActionEvent evt) {
		mainController.getResultsController().mergeResults(null);
	}

	@FXML
	public void moveListsDown(ActionEvent evt) {
		mainController.getMorphemeListsController().moveListsDown(evt);
	}

	@FXML
	public void moveListsUp(ActionEvent evt) {
		mainController.getMorphemeListsController().moveListsUp(evt);
	}

	@FXML
	public void removeLists(ActionEvent evt) {
		mainController.getMorphemeListsController().removeLists(evt);
	}

	@FXML
	public void removeResults(ActionEvent evt) {
		mainController.getResultsController().removeResults(null);
	}

	@FXML
	public void saveResults(ActionEvent evt) {
		File file = saveResultsFileChooser.showSaveDialog(mainController.getStage());

		if (file != null) {
			try {
				Files.write(file.toPath(), mainController.getResults());
			} catch (IOException e) {
				e.printStackTrace();
				mainController.showErrorAlert("Results Save Error", "Unable to save results to: " + file.getAbsolutePath());
			}
		}
	}

	@FXML
	public void showAbout(ActionEvent evt) {
		WebView webView = new WebView();
		String html;
		try {
			html = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("ui/about.html").toURI())));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			mainController.showErrorAlert("About load fail", "Unable to load about file.");
			return;
		}
		webView.setPrefSize(480, 240);
		webView.getEngine().loadContent(html);
		mainController.showFormAlert("About", webView, "Close", null);
	}

	@FXML
	public void showHelp(ActionEvent evt) {
		WebView webView = new WebView();
		String html;
		try {
			html = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("ui/help.html").toURI())));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			mainController.showErrorAlert("Help load fail", "Unable to load help file.");
			return;
		}
		webView.setPrefSize(720, 480);
		webView.getEngine().loadContent(html);
		mainController.showFormAlert("Help", webView, "Close", null);
	}

	@FXML
	public void sortResults(ActionEvent evt) {
		mainController.getResultsController().sortResults(null);
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		modeRadioItemMap.getSecondary(preferences.getGeneratorMode()).setSelected(true);
	}
}