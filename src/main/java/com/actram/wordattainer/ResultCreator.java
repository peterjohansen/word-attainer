package com.actram.wordattainer;

import java.util.Objects;
import java.util.Random;

/**
 *
 *
 * @author Peter André Johansen
 */
public class ResultCreator {
	private ResultCase capitalization;
	private String morphemeSeparator;
	private int minMorphemeCount;
	private int maxMorphemeCount;
	private boolean allowDuplicateConsecutiveMorphemes;
	private boolean mapListsToMorphemes;

	public ResultCreator() {
		setCapitalization(ResultCase.SENTENCE_CASE);
		setMorphemeSeparator("");
		setMinMorphemeCount(2);
		setMaxMorphemeCount(4);
		setAllowDuplicateConsecutiveMorphemes(true);
		setMapListsToMorphemes(false);
	}

	public boolean allowDuplicateConsecutiveMorphemes() {
		return allowDuplicateConsecutiveMorphemes;
	}

	public ResultCase getCapitalization() {
		return capitalization;
	}

	public String getMorphemeSeparator() {
		return morphemeSeparator;
	}

	public boolean mapListsToMorphemes() {
		return mapListsToMorphemes;
	}

	public String nextResult(Random rand, MorphemeFileList[] morphemeLists) {
		return null;
	}

	public void setAllowDuplicateConsecutiveMorphemes(boolean allowDuplicateConsecutiveMorphemes) {
		this.allowDuplicateConsecutiveMorphemes = allowDuplicateConsecutiveMorphemes;
	}

	public void setCapitalization(ResultCase capitalization) {
		Objects.requireNonNull("capitalization cannot be null");
		this.capitalization = capitalization;
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

	public void setMaxMorphemeCount(int maxMorphemeCount) {
		if (maxMorphemeCount < 0) {
			throw new IllegalArgumentException("the maximum morpheme count cannot be less than one");
		}
		if (maxMorphemeCount < minMorphemeCount) {
			throw new IllegalArgumentException("the maximum morpheme count cannot be smaller than the minimum");
		}
		this.maxMorphemeCount = maxMorphemeCount;
	}

	public void setMinMorphemeCount(int minMorphemeCount) {
		if (minMorphemeCount < 1) {
			throw new IllegalArgumentException("the minimum morpheme count cannot be less than one");
		}
		if (minMorphemeCount > maxMorphemeCount) {
			throw new IllegalArgumentException("the minimum morpheme count cannot be larger than the maximum");
		}
		this.minMorphemeCount = minMorphemeCount;
	}

	public void setMorphemeSeparator(String morphemeSeparator) {
		Objects.requireNonNull(morphemeSeparator, "the morpheme separator cannot be null");
		this.morphemeSeparator = morphemeSeparator;
	}
}