package com.actram.wordattainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 
 *
 * @author Peter André Johansen
 */
public class StandardGenerator implements Generator {
	private final List<List<String>> morphemeLists = new ArrayList<>();

	private GeneratorSettings settings;

	@Override
	public String query() {

		Random random = settings.getRandom();

		// Find the morpheme count in the next result
		final int morphemeCount;
		final int minCount = settings.getMinMorphemeCount();
		final int maxCount = settings.getMaxMorphemeCount();
		if (minCount == maxCount) {
			morphemeCount = minCount;
		} else {
			morphemeCount = random.nextInt(maxCount + 1) + minCount;
		}

		ResultCase capitalization = settings.getMorphemeCapitalization();
		// String morphemeSeparator;
		// boolean allowDuplicateConsecutiveMorphemes;

		// Create, validate and format each morpheme
		String[] resultParts = new String[morphemeCount];
		for (int i = 0; i < morphemeCount; i++) {

			// Find index of list of morphemes to pick from
			int listIndex;
			if (settings.isMorphemesMappedToLists()) {
				listIndex = (i % morphemeLists.size());
			} else {
				listIndex = random.nextInt(morphemeLists.size());
			}

			// Pick a random morpheme
			List<String> morphemeList = morphemeLists.get(listIndex);
			String morpheme = morphemeList.get(random.nextInt(morphemeList.size()));
			resultParts[i] = morpheme;
			if (i != morphemeCount - 1) {
				resultParts[i] += settings.getMorphemeSeparator();
			}

		}

		// Assemble result from morphemes
		StringBuilder builder = new StringBuilder();
		for (String part : resultParts) {
			builder.append(part);
		}
		return builder.toString();
	}

	@Override
	public void update(GeneratorSettings settings) throws IOException {
		this.settings = settings;
		
		MorphemeFileList fileList = settings.getMorphemeFileList();
		for (int i = 0; i < fileList.size(); i++) {
			List<String> morphemes = fileList.load(i);

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
				throw new IllegalArgumentException("no possible morpheme combinations for the given settings");
			}
			morphemeLists.clear();
			morphemeLists.add(morphemes);

		}
	}
}