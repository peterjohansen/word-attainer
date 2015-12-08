package com.actram.wordattainer;

import java.util.Objects;
import java.util.Random;

/**
 * Contains settings that can customize the set of results a {@link Generator}
 * produces.
 * <p>
 * <ul>
 * Explanation for each setting (accessed by setters and getters):
 * <li>{@link GeneratorSettings#morphemeFileList} - A list of files that contain
 * morphemes. Each line in each file corresponds to a morpheme.</li>
 * <li>{@link GeneratorSettings#characterValidator} - A
 * {@link CharacterValidator} that specifies which characters are allowed in
 * morphemes, thereby also in the final result.</li>
 * <li>{@link GeneratorSettings#random} - The {@link Random} to use for the
 * random numbers required to generate results.</li>
 * <li>{@link GeneratorSettings#morphemeCapitalization} - A {@link ResultCase}
 * that specifies how the morphemes should be capitalized in relation to each
 * other.</li>
 * <li>{@link GeneratorSettings#morphemeSeparator} - The {@link String} to join
 * the morphemes with.</li>
 * <li>{@link GeneratorSettings#minMorphemeCount} - The minimum amount of
 * morphemes in each result.</li>
 * <li>{@link GeneratorSettings#maxMorphemeCount} - The maximum amount of
 * morphemes in each result.</li>
 * <li>{@link GeneratorSettings#allowDuplicateConsecutiveMorphemes} - Whether or
 * not two equal morphemes can appear directly after one and other in a result.
 * </li>
 * <li>{@link GeneratorSettings#mapListsToMorphemes} - Whether or not each
 * morpheme should be picked from the list with the corresponding index,
 * wrapping around the file lists as needed.</li>
 * </ul>
 *
 * @author Peter André Johansen
 */
public class GeneratorSettings {
	private final MorphemeFileList morphemeFileList;
	private final CharacterValidator characterValidator;

	private Random random;
	private ResultCase morphemeCapitalization;
	private String morphemeSeparator;
	private int minMorphemeCount;
	private int maxMorphemeCount;
	private boolean allowDuplicateConsecutiveMorphemes;
	private boolean mapListsToMorphemes;

	public GeneratorSettings() {
		this.morphemeFileList = new MorphemeFileList();
		this.characterValidator = new CharacterValidator();
		setRandom(new Random());
		setMorphemeCapitalization(ResultCase.SENTENCE_CASE);
		setMorphemeSeparator("");
		setMorphemeCountRange(2, 3);
		allowDuplicateConsecutiveMorphemes(true);
		setMapListsToMorphemes(false);
	}

	public void allowDuplicateConsecutiveMorphemes(boolean allowDuplicateConsecutiveMorphemes) {
		this.allowDuplicateConsecutiveMorphemes = allowDuplicateConsecutiveMorphemes;
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

	public ResultCase getMorphemeCapitalization() {
		return morphemeCapitalization;
	}

	public MorphemeFileList getMorphemeFileList() {
		return morphemeFileList;
	}

	public String getMorphemeSeparator() {
		return morphemeSeparator;
	}

	public Random getRandom() {
		return random;
	}

	public boolean isDuplicateConsecutiveMorphemesAllowed() {
		return allowDuplicateConsecutiveMorphemes;
	}

	public boolean isMorphemesMappedToLists() {
		return mapListsToMorphemes;
	}

	public boolean mapListsToMorphemes() {
		return mapListsToMorphemes;
	}

	public void setExactMorphemeCount(int count) {
		if (count < 1) {
			throw new IllegalArgumentException("the exact morpheme count cannot be less than one");
		}
		this.minMorphemeCount = count;
		this.maxMorphemeCount = count;
	}

	public void setMapListsToMorphemes(boolean mapListsToMorphemes) {
		this.mapListsToMorphemes = mapListsToMorphemes;
	}

	public void setMorphemeCapitalization(ResultCase capitalization) {
		Objects.requireNonNull("capitalization cannot be null");
		this.morphemeCapitalization = capitalization;
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

	public void setRandom(Random random) {
		Objects.requireNonNull(random, "the random cannot be null");
		this.random = random;
	}
}