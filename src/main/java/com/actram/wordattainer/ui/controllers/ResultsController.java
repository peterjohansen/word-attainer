package com.actram.wordattainer.ui.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.actram.wordattainer.GeneratorSettings;
import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

/**
 *
 *
 * @author Peter André Johansen
 */
public class ResultsController implements MainControllerChild {
	@FXML private ListView<String> resultsListView;
	@FXML private Button editButton;
	@FXML private Button removeButton;

	private MainController mainController;

	private FileChooser mergeFileChooser;

	@FXML
	public void editResults(ActionEvent event) {
		mainController.accessResults(results -> {

			// Retrieve indices of items to edit
			List<Integer> indicesSelection;
			if (resultsListView.getSelectionModel().isEmpty()) {

				// Select every result
				indicesSelection = new ArrayList<>(results.size());
				for (int i = 0; i < results.size(); i++) {
					indicesSelection.add(i);
				}

			} else {

				// Use only selected results
				indicesSelection = resultsListView.getSelectionModel().getSelectedIndices();

			}

			// Copy results into edit alert
			Parent content = mainController.loadFXML("edit_results.fxml");
			TextArea resultsTextArea = (TextArea) content.lookup("#resultsTextArea");
			for (int i = 0; i < indicesSelection.size(); i++) {
				int index = indicesSelection.get(i);
				resultsTextArea.appendText(results.get(index));
				if (index != results.size() - 1) {
					resultsTextArea.appendText("\n");
				}
			}

			// Display edit alert and update results if appropriate
			if (mainController.showFormAlert("Edit Results", content, "Apply changes", "Cancel")) {
				String[] editedResults = resultsTextArea.getText().split("\\n");
				for (int i = 0; i < editedResults.length; i++) {
					int index = indicesSelection.get(i);
					results.set(index, editedResults[i]);
				}
			}

		});
	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;

		this.mergeFileChooser = new FileChooser();
		mergeFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		mergeFileChooser.setTitle("Select Results To Merge With");

		this.resultsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		BooleanBinding emptySelection = resultsListView.getSelectionModel().selectedItemProperty().isNull();
		this.removeButton.disableProperty().bind(emptySelection);
	}

	@FXML
	public void mergeResults(ActionEvent event) {
		mainController.accessResults(results -> {
			List<File> files = mergeFileChooser.showOpenMultipleDialog(mainController.getStage());
			if (files != null && !files.isEmpty()) {
				for (File file : files) {
					try {
						List<String> lines = Files.readAllLines(file.toPath());
						for (String line : lines) {
							if (!results.contains(line)) {
								results.add(line);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						mainController.showErrorAlert("Import Error", "Unable to open the selected files for merging.");
					}
				}
			}
		});
	}

	@FXML
	public void removeResults(ActionEvent event) {
		mainController.accessResults(results -> {
			List<String> selection = new ArrayList<>(resultsListView.getSelectionModel().getSelectedItems());
			results.removeAll(selection);
		});
	}

	@FXML
	public void sortResults(ActionEvent event) {
		mainController.accessResults(results -> {
			results.sort();
		});
	}

	@Override
	public void updateUI(Preferences preferences, GeneratorSettings generatorSettings, ResultList results) {
		resultsListView.getItems().setAll(results);
	}
}