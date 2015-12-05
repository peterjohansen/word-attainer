package com.actram.wordattainer.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.actram.wordattainer.ResultList;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class ResultsController implements Initializable {
	@FXML private ListView<String> resultsListView;
	@FXML private Button editButton;
	@FXML private Button removeButton;

	private WordAttainer program;
	private ResultList results;

	private FileChooser mergeFileChooser;

	private ListProperty<String> resultsListProperty;

	@FXML
	public void editResults(ActionEvent event) {

		Parent content = program.loadFXML("edit_results.fxml");
		TextArea resultsTextArea = (TextArea) content.lookup("#resultsTextArea");
		for (int i = 0; i < results.size(); i++) {
			resultsTextArea.appendText(results.get(i));
			if (i != results.size() - 1) {
				resultsTextArea.appendText("\n");
			}
		}

		if (program.showFormAlert("Edit Results", content, "Apply changes", "Cancel")) {
			results.clear();
			results.addAll(Arrays.asList(resultsTextArea.getText().split("\\n")));

			updateResultsListUI();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.program = WordAttainer.getInstance();
		this.results = program.getResults();

		this.mergeFileChooser = new FileChooser();
		mergeFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		mergeFileChooser.setTitle("Select Results To Merge With");

		this.resultsListProperty = new SimpleListProperty<>();
		resultsListView.setItems(FXCollections.observableArrayList(results));
		resultsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		resultsListView.getSelectionModel().selectedIndexProperty();

		resultsListView.itemsProperty().bindBidirectional(resultsListProperty);
		updateResultsListUI();

		BooleanBinding emptySelection = resultsListView.getSelectionModel().selectedItemProperty().isNull();
		this.removeButton.disableProperty().bind(emptySelection);

	}

	@FXML
	public void mergeResults(ActionEvent event) {
		List<File> files = mergeFileChooser.showOpenMultipleDialog(program.getStage());
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
					program.showErrorAlert("Import Error", "Unable to open the selected files for merging.");
				}
			}
			updateResultsListUI();
		}
	}

	@FXML
	public void removeResults(ActionEvent event) {
		List<String> selection = new ArrayList<>(resultsListView.getSelectionModel().getSelectedItems());
		results.removeAll(selection);
		updateResultsListUI();
	}

	@FXML
	public void sortResults(ActionEvent event) {
		results.sort();
		updateResultsListUI();
	}

	private void updateResultsListUI() {
		resultsListProperty.set(FXCollections.observableArrayList(results));
	}
}