package com.actram.wordattainer;

import java.io.IOException;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 
 *
 * @author Peter André Johansen
 */
public class StandardGenerator implements Generator {
	private static final long serialVersionUID = 2294330934953407255L;

	private static final String VOWELS = "aeiouyæøå";
	private static final String CONSONANTS = "bcdfghjklmnpqrstvwxz";

	private final List<List<String>> morphemeLists = new ArrayList<>();

	private GeneratorSettings settings;
	private Set<String> allResults = new HashSet<>();

	private int uniqueResultsAmount = 0;

	@Override
	public int getUniqueResultsAmount() {
		return uniqueResultsAmount;
	}

	@Override
	public String query() throws InterruptedByTimeoutException {

		Random random = settings.getRandom();
		long start = System.currentTimeMillis();

		// Create results until we get a unique one
		String finalResult;
		do {
			if (System.currentTimeMillis() - start > settings.getGeneratorTimeout() * 1000) {
				throw new InterruptedByTimeoutException();
			}

			// Find the morpheme count in the next result
			final int morphemeCount;
			final int minCount = settings.getMinMorphemeCount();
			final int maxCount = settings.getMaxMorphemeCount();
			if (settings.isExactMorphemeCount()) {
				morphemeCount = settings.getExactMorphemeCount();
			} else {
				morphemeCount = (int) (Math.random() * ((maxCount + 1) - minCount) + minCount);
			}

			// Create, validate and format each morpheme
			Result result = new Result(morphemeCount);
			for (int i = 0; i < result.partCount(); i++) {

				// Find index of list of morphemes to pick from
				int listIndex;
				if (settings.isMorphemesMappedToLists()) {
					listIndex = (i % morphemeLists.size());
				} else {
					listIndex = random.nextInt(morphemeLists.size());
				}

				// Pick a random morpheme
				List<String> morphemeList = morphemeLists.get(listIndex);
				String morpheme = null;
				while (true) {
					morpheme = morphemeList.get(random.nextInt(morphemeList.size()));

					// Check for repeated morpheme and create
					// a new one if that's not allowed
					if (!settings.isDuplicateConsecutiveMorphemesAllowed() && result.isValue(i - 1, morpheme)) {
						continue;
					}

					// The morpheme is valid
					break;

				}
				result.set(i, morpheme);
			}

			boolean trimVowels = settings.isVowelsTrimmed();
			boolean trimConsonants = settings.isConsonantsTrimmed();
			if (trimVowels || trimConsonants) {
				if (trimVowels && trimConsonants && settings.getTrimVowelCount() == settings.getTrimConsonantCount()) {
					result.trimDuplicateConsecutive(settings.getTrimVowelCount(), VOWELS + CONSONANTS);
				} else if (trimVowels) {
					result.trimDuplicateConsecutive(settings.getTrimVowelCount(), VOWELS);
				} else if (trimConsonants) {
					result.trimDuplicateConsecutive(settings.getTrimConsonantCount(), CONSONANTS);
				}
			}
			result.capitalize(settings.getMorphemeCapitalization());
			finalResult = result.join(settings.getMorphemeSeparator());

		} while (allResults.contains(finalResult));

		allResults.add(finalResult);
		uniqueResultsAmount++;
		return finalResult;
	}

	@Override
	public void update(GeneratorSettings settings) throws IOException {
		this.settings = settings;
		uniqueResultsAmount = 0;
		allResults.clear();
		morphemeLists.clear();

		for (String file : settings.getMorphemeFileList().getFileList()) {
			List<String> morphemes = settings.getMorphemeFileList().load(file);

			// Remove any morphemes with illegal characters
			Iterator<String> morphemeIterator = morphemes.iterator();
			while (morphemeIterator.hasNext()) {
				String morpheme = morphemeIterator.next();
				for (char c : morpheme.toCharArray()) {
					if (!settings.getCharacterValidator().isValid(c)) {
						morphemeIterator.remove();
						break;
					}
				}
			}
			if (!morphemes.isEmpty()) {
				morphemeLists.add(morphemes);
			}
		}
		if (morphemeLists.isEmpty()) {
			throw new IllegalArgumentException("no possible morpheme combinations for the given settings");
		}
	}
}