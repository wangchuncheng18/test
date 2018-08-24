package com.tarotdt.pas.web.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class GsonNanToNullTypeAdaptorFactory implements TypeAdapterFactory {
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    final Class<T> rawType = (Class<T>) type.getRawType();
    if ((rawType != Double.TYPE) && (rawType != Float.TYPE) && (rawType != Double.class)
        && (rawType != Float.class)) {
      return null;
    }

    return new TypeAdapter<T>() {
      private Double mapToNull(Double dbl) {
        if ((dbl == null) || (dbl.isNaN()) || (dbl.isInfinite())) {
          return null;
        }
        return dbl;
      }

      private Float mapToNull(Float flt) {
        if ((flt == null) || (flt.isNaN()) || (flt.isInfinite())) {
          return null;
        }
        return flt;
      }

      public void write(JsonWriter out, T value) throws IOException {
        if ((rawType == Double.TYPE) || (rawType == Double.class)) {
          Double dbl = mapToNull((Double) value);
          if (dbl == null) {
            out.nullValue();
          } else {
            out.value(dbl.doubleValue());
          }
        } else {
          Float dbl = mapToNull((Float) value);
          if (dbl == null) {
            out.nullValue();
          } else {
            out.value(dbl.floatValue());
          }
        }
      }

      public T read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
          reader.nextNull();

          if ((rawType == Double.class) || (rawType == Float.class))
            return null;
          if (rawType == Double.TYPE)
            return (T) new Double(0d / 0d);
          if (rawType == Float.TYPE) {
            return (T) new Float(0f / 0f);
          }
        }

        if ((rawType == Double.TYPE) || (rawType == Double.class)) {
          return (T) new Double(reader.nextDouble());
        }
        return (T) new Float(reader.nextDouble());
      }
    };
  }
}
