package com.actram.captair;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {
	private static ObjectMapper mapper;

	public static <T> T read(InputStream jsonStream, Class<T> cls) throws IOException {
		Objects.requireNonNull(jsonStream, "json stream cannot be null");
		Objects.requireNonNull(cls, "class cannot be null");

		if (mapper == null) {
			mapper = new ObjectMapper();
		}
		try {
			return mapper.readValue(jsonStream, cls);
		} catch (JsonMappingException | JsonParseException e) {
			System.err.println("bad json: " + e);
			throw e;
		}
	}

	private JsonReader() {
	}
}