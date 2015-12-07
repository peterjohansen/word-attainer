package com.actram.wordattainer;

import java.util.Objects;

/**
 * 
 *
 * @author Peter André Johansen
 */
public class GeneratorSettings {
	private final MorphemeFileList morphemeListFiles;
	private CharacterValidator characterValidator;
	private ResultCase capitalization;
	private String morphemeSeparator;
	private int minMorphemeCount;
	private int maxMorphemeCount;
	private boolean allowDuplicateConsecutiveMorphemes;
	private boolean mapListsToMorphemes;

	public GeneratorSettings() {
		this.morphemeListFiles = new MorphemeFileList();
		setCharacterValidator(new CharacterValidator());
		setCapitalization(ResultCase.SENTENCE_CASE);
		setMorphemeSeparator("");
		setMorphemeCountRange(2, 4);
		setAllowDuplicateConsecutiveMorphemes(true);
		setMapListsToMorphemes(false);
	}

	public boolean allowDuplicateConsecutiveMorphemes() {
		return allowDuplicateConsecutiveMorphemes;
	}

	public ResultCase getCapitalization() {
		return capitalization;
	}

	public CharacterValidator getCharacterValidator() {
		return characterValidator;
	}

	public int getMaxMorphemeCount() {
		return maxMorphemeCount;
	}

	public int getMinMorphemeCount() {
		return minMorphemeCount;
	}

	public MorphemeFileList getMorphemeListFiles() {
		return morphemeListFiles;
	}

	public String getMorphemeSeparator() {
		return morphemeSeparator;
	}

	public boolean mapListsToMorphemes() {
		return mapListsToMorphemes;
	}

	public void setAllowDuplicateConsecutiveMorphemes(boolean allowDuplicateConsecutiveMorphemes) {
		this.allowDuplicateConsecutiveMorphemes = allowDuplicateConsecutiveMorphemes;
	}

	public void setCapitalization(ResultCase capitalization) {
		Objects.requireNonNull("capitalization cannot be null");
		this.capitalization = capitalization;
	}

	public void setCharacterValidator(CharacterValidator characterValidator) {
		Objects.requireNonNull(characterValidator, "the character validator cannot be null");
		this.characterValidator = characterValidator;
	}

	public void setExactMorphemeCount(int count) {
		if (count < 0) {
			throw new IllegalArgumentException("the exact morpheme count cannot be less than one");
		}
		this.minMorphemeCount = count;
		this.maxMorphemeCount = count;
	}

	public void setMapListsToMorphemes(boolean mapListsToMorphemes) {
		this.mapListsToMorphemes = mapListsToMorphemes;
	}

	public void setMorphemeCountRange(int min, int max) {
		if (min < 1) {
			throw new IllegalArgumentException("the minimum morpheme count cannot be less than one");
		}
		if (min > max) {
			throw new IllegalArgumentException("the minimum morpheme count cannot be larger than the maximum");
		}
		this.minMorphemeCount = min;
		this.maxMorphemeCount = max;
	}

	public void setMorphemeSeparator(String morphemeSeparator) {
		Objects.requireNonNull(morphemeSeparator, "the morpheme separator cannot be null");
		this.morphemeSeparator = morphemeSeparator;
	}
}