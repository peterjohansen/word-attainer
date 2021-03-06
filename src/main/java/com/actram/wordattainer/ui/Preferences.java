package com.actram.wordattainer.ui;

import java.io.Serializable;
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
public class Preferences extends GeneratorSettings implements Serializable {
	private static final long serialVersionUID = -3946149163610904723L;

	public static final int MIN_RESULT_AMOUNT = 1;
	public static final int MAX_RESULT_AMOUNT = 1000000;

	private Generator generator;
	private GeneratorMode generatorMode;
	private int resultAmount;
	private boolean autoSortResults;

	public Preferences() {
		setGenerator(new StandardGenerator());
		setGeneratorMode(GeneratorMode.LIST);
		setResultAmountToDefault();
		setAutoSortResultsToDefault();
	}

	public Generator getGenerator() {
		return generator;
	}

	public GeneratorMode getGeneratorMode() {
		return generatorMode;
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

	public void setAutoSortResultsToDefault() {
		setAutoSortResults(false);
	}

	public void setGenerator(Generator generator) {
		Objects.requireNonNull(generator, "the generator cannot be null");
		this.generator = generator;
	}

	public void setGeneratorMode(GeneratorMode generatorMode) {
		Objects.requireNonNull(generatorMode, "the generator mode cannot be null");
		this.generatorMode = generatorMode;
	}

	public void setResultAmount(int resultAmount) {
		if (resultAmount < 1) {
			throw new IllegalArgumentException("result amount cannot be less than one");
		}
		this.resultAmount = resultAmount;
	}

	public void setResultAmountToDefault() {
		setResultAmount(16);
	}

	public Preferences setTo(Preferences preferences) {
		Objects.requireNonNull(preferences, "preferences cannot be null");

		getMorphemeFileList().setTo(preferences.getMorphemeFileList());
		getCharacterValidator().setTo(preferences.getCharacterValidator());
		setRandom(preferences.getRandom());
		setTrimVowels(preferences.isVowelsTrimmed());
		setTrimVowelCount(preferences.getTrimVowelCount());
		setTrimConsonants(preferences.isConsonantsTrimmed());
		setTrimConsonantCount(preferences.getTrimConsonantCount());
		setMorphemeCapitalization(preferences.getMorphemeCapitalization());
		setMorphemeSeparator(preferences.getMorphemeSeparator());
		setUseExactMorphemeCount(preferences.isExactMorphemeCount());
		setExactMorphemeCount(preferences.getExactMorphemeCount());
		setMorphemeCountRange(preferences.getMinMorphemeCount(), preferences.getMaxMorphemeCount());
		allowDuplicateConsecutiveMorphemes(preferences.isDuplicateConsecutiveMorphemesAllowed());
		setMapListsToMorphemes(preferences.isMorphemesMappedToLists());

		setGenerator(preferences.getGenerator());
		setGeneratorMode(preferences.getGeneratorMode());
		setResultAmount(preferences.getResultAmount());
		setGeneratorTimeout(preferences.getGeneratorTimeout());
		setAutoSortResults(preferences.isResultsAutoSort());

		return this;
	}
}