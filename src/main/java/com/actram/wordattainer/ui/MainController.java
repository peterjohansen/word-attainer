package com.actram.wordattainer.ui;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.actram.wordattainer.GeneratorSettings;
import com.actram.wordattainer.MorphemeList;
import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.generator.GeneratorMode;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MainController implements Initializable {
	@FXML private GeneratorController generatorController;
	@FXML private MenuBarController menuBarController;
	@FXML private MorphemeListsController morphemeListsController;
	@FXML private ResultsController resultsController;

	private WordAttainer program;

	private GeneratorSettings generatorSettings;
	private GeneratorMode generatorMode;
	private ResultList results;

	public GeneratorController getGeneratorController() {
		return generatorController;
	}

	public MenuBarController getMenuBarController() {
		return menuBarController;
	}

	public MorphemeListsController getMorphemeListsController() {
		return morphemeListsController;
	}

	public ResultsController getResultsController() {
		return resultsController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.program = WordAttainer.getInstance();
		program.setMainController(this);

		this.generatorSettings = new GeneratorSettings();
		this.generatorMode = GeneratorMode.LIST;
		this.results = new ResultList();

		MorphemeList morphemes = generatorSettings.getMorphemes();
		morphemes.add("test1");
		morphemes.add("test2");
		morphemes.add("test3");
		morphemes.add("test4");

		results.add("test1");
		results.add("test2");
		results.add("test3");
		results.add("test4");
	}
	
	public ResultList getResults() {
		return results;
	}

	public void setGeneratorMode(GeneratorMode mode) {
		Objects.requireNonNull(mode, "the generator mode cannot be null");
		this.generatorMode = mode;
	}
}