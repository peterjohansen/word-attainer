package com.actram.wordattainer.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

/**
 *
 *
 * @author Peter André Johansen
 */
public class ResultsController implements Initializable {
	@FXML private ListView<?> resultsList;

	@FXML
	public void editResults(ActionEvent event) {
		System.out.println("edit results");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void mergeResults(ActionEvent event) {
		System.out.println("merge results");
	}

	@FXML
	public void removeResults(ActionEvent event) {
		System.out.println("remove rsults");
	}

	@FXML
	public void sortResults(ActionEvent event) {
		System.out.println("sort results");
	}
}