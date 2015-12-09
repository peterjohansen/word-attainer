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
	public static final int MIN_MORPHEME_COUNT = 1;
	public static final int MAX_MORPHEME_COUNT = 1000;

	private static void checkMorphemeCount(int count) {
		if (count < MIN_MORPHEME_COUNT || count > MAX_MORPHEME_COUNT) {
			throw new IllegalArgumentException("morpheme count is out of range: " + count);
		}
	}
	private final MorphemeFileList morphemeFileList;

	private final CharacterValidator characterValidator;
	private Random random;
	private ResultCase morphemeCapitalization;

	private String morphemeSeparator;
	private boolean useExactMorphemeCount;
	private int exactMorphemeCount;
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
		setMorphemeCountToDefault();
		allowDuplicateConsecutiveMorphemes(true);
		setMapListsToMorphemes(false);
	}

	public void allowDuplicateConsecutiveMorphemes(boolean allowDuplicateConsecutiveMorphemes) {
		this.allowDuplicateConsecutiveMorphemes = allowDuplicateConsecutiveMorphemes;
	}

	public CharacterValidator getCharacterValidator() {
		return characterValidator;
	}

	public int getExactMorphemeCount() {
		return exactMorphemeCount;
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

	public boolean isExactMorphemeCount() {
		return useExactMorphemeCount;
	}

	public boolean isMorphemesMappedToLists() {
		return mapListsToMorphemes;
	}

	public void setExactMorphemeCount(int count) {
		checkMorphemeCount(count);
		this.exactMorphemeCount = count;
	}

	public void setMapListsToMorphemes(boolean mapListsToMorphemes) {
		this.mapListsToMorphemes = mapListsToMorphemes;
	}

	public void setMorphemeCapitalization(ResultCase capitalization) {
		Objects.requireNonNull("capitalization cannot be null");
		this.morphemeCapitalization = capitalization;
	}

	public void setMorphemeCountRange(int min, int max) {
		checkMorphemeCount(min);
		checkMorphemeCount(max);
		if (min > max) {
			throw new IllegalArgumentException("the minimum morpheme count cannot be larger than the maximum");
		}
		this.minMorphemeCount = min;
		this.maxMorphemeCount = max;
	}

	public void setMorphemeCountToDefault() {
		setUseExactMorphemeCount(false);
		setExactMorphemeCount(3);
		setMorphemeCountRange(2, 4);
		setMorphemeCountRange(2, 2);
	}

	public void setMorphemeSeparator(String morphemeSeparator) {
		Objects.requireNonNull(morphemeSeparator, "the morpheme separator cannot be null");
		this.morphemeSeparator = morphemeSeparator;
	}

	public void setRandom(Random random) {
		Objects.requireNonNull(random, "the random cannot be null");
		this.random = random;
	}

	public void setUseExactMorphemeCount(boolean useExactMorphemeCount) {
		this.useExactMorphemeCount = useExactMorphemeCount;
	}
}