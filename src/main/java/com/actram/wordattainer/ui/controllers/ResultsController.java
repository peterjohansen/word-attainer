package com.actram.wordattainer.ui.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.FileChooser;

/**
 *
 *
 * @author Peter André Johansen
 */
public class ResultsController implements MainControllerChild {
	@FXML private ListView<String> resultsListView;
	@FXML private Button removeButton;
	@FXML private Label resultCountLabel;

	private MainController mainController;

	private FileChooser mergeFileChooser;

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;

		this.mergeFileChooser = new FileChooser();
		mergeFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		mergeFileChooser.setTitle("Select Results To Merge With");

		this.resultsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		resultsListView.setCellFactory(TextFieldListCell.forListView());

		BooleanBinding emptySelection = resultsListView.getSelectionModel().selectedItemProperty().isNull();
		this.removeButton.disableProperty().bind(emptySelection);
	}

	@FXML
	public void mergeResults(ActionEvent event) {
		List<File> files = mergeFileChooser.showOpenMultipleDialog(mainController.getStage());
		if (files != null && !files.isEmpty()) {
			ResultList results = mainController.getResults();
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
			mainController.stateUpdated();
		}
	}

	@FXML
	public void removeResults(ActionEvent event) {
		List<String> selection = new ArrayList<>(resultsListView.getSelectionModel().getSelectedItems());
		mainController.getResults().removeAll(selection);
		mainController.stateUpdated();
	}

	@FXML
	public void resultEdited(ListView.EditEvent<String> result) {
		ResultList results = mainController.getResults();

		String newValue = result.getNewValue();
		if (newValue.trim().isEmpty()) {
			results.remove(result.getIndex());
		} else {
			results.set(result.getIndex(), newValue);
			results.removeDuplicates();
		}
		mainController.stateUpdated();
	}

	@FXML
	public void sortResults(ActionEvent event) {
		mainController.getResults().sort();
		mainController.stateUpdated();
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		resultsListView.getItems().setAll(results);
		resultCountLabel.setText(results.size() + " results");
	}
}