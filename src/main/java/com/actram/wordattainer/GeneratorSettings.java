package com.actram.wordattainer;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * Contains settings that can customize the set of results a {@link Generator}
 * produces.
 * 
 * @author Peter André Johansen
 */
public class GeneratorSettings implements Serializable {
	private static final long serialVersionUID = 6011048486666320311L;

	public static final int MIN_GENERATOR_TIMEOUT = 1;
	public static final int MAX_GENERATOR_TIMEOUT = 3600;

	public static final int MIN_MORPHEME_COUNT = 1;
	public static final int MAX_MORPHEME_COUNT = 1000;

	public static final int MIN_TRIM_COUNT = 1;

	private static void checkMorphemeCount(int count) {
		if (count < MIN_MORPHEME_COUNT || count > MAX_MORPHEME_COUNT) {
			throw new IllegalArgumentException("morpheme count is out of range: " + count);
		}
	}

	private final MorphemeFileList morphemeFileList;
	private int generatorTimeout;

	private final CharacterValidator characterValidator;
	private Random random;
	private ResultCase morphemeCapitalization;

	private boolean trimVowels;
	private int trimVowelCount;
	private boolean trimConsonants;
	private int trimConsonantCount;

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
		setGeneratorTimeoutToDefault();
		setCharactersToDefault();
		setRandom(new Random());
		setMorphemeCapitalizationToDefault();
		setMorphemeSeparatorToDefault();
		setMorphemeCountToDefault();
		setAllowDuplicateConsecutiveMorphemesToDefault();
		setMapListsToMorphemesToDefault();
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

	public int getGeneratorTimeout() {
		return generatorTimeout;
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

	public int getTrimConsonantCount() {
		return trimConsonantCount;
	}

	public int getTrimVowelCount() {
		return trimVowelCount;
	}

	public boolean isConsonantsTrimmed() {
		return trimConsonants;
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

	public boolean isVowelsTrimmed() {
		return trimVowels;
	}

	public void setAllowDuplicateConsecutiveMorphemesToDefault() {
		allowDuplicateConsecutiveMorphemes(true);
	}

	public void setCharactersToDefault() {
		characterValidator.setToDefault();
		setTrimVowels(true);
		setTrimVowelCount(2);
		setTrimConsonants(true);
		setTrimConsonantCount(2);
	}

	public void setExactMorphemeCount(int count) {
		checkMorphemeCount(count);
		this.exactMorphemeCount = count;
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

	public void setGeneratorTimeoutToDefault() {
		setGeneratorTimeout(2);
	}

	public void setMapListsToMorphemes(boolean mapListsToMorphemes) {
		this.mapListsToMorphemes = mapListsToMorphemes;
	}

	public void setMapListsToMorphemesToDefault() {
		setMapListsToMorphemes(false);
	}

	public void setMorphemeCapitalization(ResultCase capitalization) {
		Objects.requireNonNull("capitalization cannot be null");
		this.morphemeCapitalization = capitalization;
	}

	public void setMorphemeCapitalizationToDefault() {
		setMorphemeCapitalization(ResultCase.SENTENCE_CASE);
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
	}

	public void setMorphemeSeparator(String morphemeSeparator) {
		Objects.requireNonNull(morphemeSeparator, "the morpheme separator cannot be null");
		this.morphemeSeparator = morphemeSeparator;
	}

	public void setMorphemeSeparatorToDefault() {
		setMorphemeSeparator("");
	}

	public void setRandom(Random random) {
		Objects.requireNonNull(random, "the random cannot be null");
		this.random = random;
	}

	public void setTrimConsonantCount(int trimConsonantCount) {
		if (trimConsonantCount < MIN_TRIM_COUNT) {
			throw new IllegalArgumentException("trim count cannot be less than " + MIN_TRIM_COUNT);
		}
		this.trimConsonantCount = trimConsonantCount;
	}

	public void setTrimConsonants(boolean trimConsonants) {
		this.trimConsonants = trimConsonants;
	}

	public void setTrimVowelCount(int trimVowelCount) {
		if (trimVowelCount < MIN_TRIM_COUNT) {
			throw new IllegalArgumentException("trim count cannot be less than " + MIN_TRIM_COUNT);
		}
		this.trimVowelCount = trimVowelCount;
	}

	public void setTrimVowels(boolean trimVowels) {
		this.trimVowels = trimVowels;
	}

	public void setUseExactMorphemeCount(boolean useExactMorphemeCount) {
		this.useExactMorphemeCount = useExactMorphemeCount;
	}
}