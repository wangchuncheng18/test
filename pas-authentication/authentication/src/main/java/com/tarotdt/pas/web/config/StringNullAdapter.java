package com.tarotdt.pas.web.config;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class StringNullAdapter extends TypeAdapter<String> {
	public String read(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
			reader.nextNull();
			return "";
		}
		return reader.nextString();
	}

	public void write(JsonWriter writer, String value) throws IOException {
		if (value == null) {
			// 在这里处理null改为空字符串
			writer.value("");
			return;
		}
		writer.value(value);
	}
}
