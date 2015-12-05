package com.actram.wordattainer;

import java.util.ArrayList;
import java.util.Collections;

public class ResultList extends ArrayList<String> {
	private static final long serialVersionUID = 4458962424309645081L;
	
	public void sort() {
		Collections.sort(this);
	}
}