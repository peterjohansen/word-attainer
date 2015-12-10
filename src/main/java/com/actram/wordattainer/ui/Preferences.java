package com.actram.wordattainer.ui;

import java.util.Objects;

import com.actram.wordattainer.Generator;
import com.actram.wordattainer.GeneratorSettings;
import com.actram.wordattainer.StandardGenerator;
import com.actram.wordattainer.ui.generator.GeneratorMode;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Preferences extends GeneratorSettings {
	public static final int MIN_RESULT_AMOUNT = 1;
	public static final int MAX_RESULT_AMOUNT = 1000000;

	public static final int MIN_GENERATOR_TIMEOUT = 1;
	public static final int MAX_GENERATOR_TIMEOUT = 3600;

	private Generator generator;
	private GeneratorMode generatorMode;
	private int resultAmount;
	private int generatorTimeout;
	private boolean autoSortResults;

	public Preferences() {
		setGenerator(new StandardGenerator());
		setGeneratorMode(GeneratorMode.LIST);
		setResultAmount(16);
		setGeneratorTimeout(2);
		setAutoSortResults(false);
	}

	public Generator getGenerator() {
		return generator;
	}

	public GeneratorMode getGeneratorMode() {
		return generatorMode;
	}

	public int getGeneratorTimeout() {
		return generatorTimeout;
	}

	public int getResultAmount() {
		return resultAmount;
	}

	public boolean isResultsAutoSort() {
		return autoSortResults;
	}

	public void setAutoSortResults(boolean autoSortResults) {
		this.autoSortResults = autoSortResults;
	}

	public void setGenerator(Generator generator) {
		Objects.requireNonNull(generator, "the generator cannot be null");
		this.generator = generator;
	}

	public void setGeneratorMode(GeneratorMode generatorMode) {
		Objects.requireNonNull(generatorMode, "the generator mode cannot be null");
		this.generatorMode = generatorMode;
	}

	public void setGeneratorTimeout(int generatorTimeout) {
		if (generatorTimeout < MIN_GENERATOR_TIMEOUT) {
			throw new IllegalArgumentException("generator timeout is too low");
		}
		if (generatorTimeout > MAX_GENERATOR_TIMEOUT) {
			throw new IllegalArgumentException("generator timeout is too high");
		}
		this.generatorTimeout = generatorTimeout;
	}

	public void setResultAmount(int resultAmount) {
		if (resultAmount < 1) {
			throw new IllegalArgumentException("result amount cannot be less than one");
		}
		this.resultAmount = resultAmount;
	}

	public Preferences setTo(Preferences preferences) {
		Objects.requireNonNull(preferences, "preferences cannot be null");

		getMorphemeFileList().setTo(preferences.getMorphemeFileList());
		getCharacterValidator().setTo(preferences.getCharacterValidator());
		setRandom(preferences.getRandom());
		setMorphemeCapitalization(preferences.getMorphemeCapitalization());
		setMorphemeSeparator(preferences.getMorphemeSeparator());
		setMorphemeCountRange(preferences.getMinMorphemeCount(), preferences.getMaxMorphemeCount());
		allowDuplicateConsecutiveMorphemes(preferences.isDuplicateConsecutiveMorphemesAllowed());
		setMapListsToMorphemes(preferences.isMorphemesMappedToLists());

		setGenerator(preferences.getGenerator());
		setGeneratorMode(preferences.getGeneratorMode());
		setResultAmount(preferences.getResultAmount());

		return this;
	}
}