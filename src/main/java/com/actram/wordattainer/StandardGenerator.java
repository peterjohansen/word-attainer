package com.actram.wordattainer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 
 *
 * @author Peter André Johansen
 */
public class StandardGenerator implements ResultGenerator {
	private final Map<Integer, List<String>> morphemeListsCache = new HashMap<>();
	private int morphemeListsCacheHash;

	@Override
	public String query(Random random, GeneratorSettings settings) throws IOException {

		// Load (or reload) morphemes and cache them
		MorphemeFileList listFiles = settings.getMorphemeListFiles();
		int hash = listFiles.hashCode();
		if (this.morphemeListsCacheHash != hash) {
			this.morphemeListsCacheHash = listFiles.hashCode();
			morphemeListsCache.clear();
			for (int i = 0; i < listFiles.size(); i++) {
				morphemeListsCache.put(i, listFiles.load(i));
			}
		}

		// Find the morpheme count in the next result
		final int morphemeCount;
		final int minCount = settings.getMinMorphemeCount();
		final int maxCount = settings.getMaxMorphemeCount();
		if (minCount == maxCount) {
			morphemeCount = minCount;
		} else {
			morphemeCount = random.nextInt(maxCount + 1) + minCount;
		}

		CharacterValidator characterValidator = settings.getCharacterValidator();
		ResultCase capitalization = settings.getCapitalization();
		Map<Integer, List<String>> morphemeLists = morphemeListsCache;
		// String morphemeSeparator;
		// boolean allowDuplicateConsecutiveMorphemes;
		
		// Create, validate and format each morpheme 
		String[] resultParts = new String[morphemeCount];
		for (int i = 0; i < morphemeCount; i++) {
			
			// Find index of list of morphemes to pick from
			int listIndex;
			if (settings.mapListsToMorphemes()) {
				listIndex = (i % morphemeLists.size());
			} else {
				listIndex = random.nextInt(morphemeLists.size());
			}
			
			// Pick a random morpheme
			List<String> morphemeList = morphemeLists.get(listIndex);
			resultParts[i] = morphemeList.get(random.nextInt(morphemeList.size()));
			
		}

		// Assemble result from morphemes
		StringBuilder builder = new StringBuilder();
		for (String part : resultParts) {
			builder.append(part);
		}
		return builder.toString();
	}
}