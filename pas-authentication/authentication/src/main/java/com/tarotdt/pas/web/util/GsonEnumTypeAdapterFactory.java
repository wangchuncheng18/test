package com.tarotdt.pas.web.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GsonEnumTypeAdapterFactory implements TypeAdapterFactory {

  @SuppressWarnings("unchecked")
  public TypeAdapter<Object> create(Gson gson, TypeToken type) {
    Class<?> rawType = type.getRawType();
    if (!rawType.isEnum()) {
      return null;
    }

    final Map<String, Object> strToEnum = new HashMap<String, Object>();
    for (Object enumValue : rawType.getEnumConstants()) {
      strToEnum.put(enumValue.toString(), enumValue);
    }

    return new TypeAdapter<Object>() {
      public void write(JsonWriter out, Object value) throws IOException {
        if (value == null) {
          out.nullValue();
        } else {
          out.value(value.toString());
        }
      }

      public Object read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
          reader.nextNull();
          return null;
        }
        return strToEnum.get(reader.nextString());
      }
    };
  }
}
