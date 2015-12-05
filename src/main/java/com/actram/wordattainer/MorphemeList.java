package com.actram.wordattainer;

import java.util.ArrayList;

public class MorphemeList extends ArrayList<String> {
	private static final long serialVersionUID = -2082355773577858672L;

	public void swap(int index1, int index2) {
		
		String temp = get(index1);
		set(index1, get(index2));
		set(index2, temp);
	}
}