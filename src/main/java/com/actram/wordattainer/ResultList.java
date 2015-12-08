package com.actram.wordattainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 *
 * @author Peter André Johansen
 */
public class ResultList extends ArrayList<String> {
	private static final long serialVersionUID = 4458962424309645081L;

	public void removeDuplicates() {
		List<String> listSet = new ArrayList<>(new LinkedHashSet<>(this));
		clear();
		addAll(listSet);
	}

	public void sort() {
		Collections.sort(this);
	}
}