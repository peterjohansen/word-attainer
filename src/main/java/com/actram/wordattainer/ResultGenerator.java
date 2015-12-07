package com.actram.wordattainer;

import java.util.Random;

/**
 *
 *
 * @author Peter André Johansen
 */
public interface ResultGenerator {

	public String query(Random random, GeneratorSettings settings) throws Exception;

}