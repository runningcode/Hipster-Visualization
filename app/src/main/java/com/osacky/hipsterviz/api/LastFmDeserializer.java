package com.osacky.hipsterviz.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class LastFmDeserializer<T> implements JsonDeserializer<T> {
    String field;

    public LastFmDeserializer(String field) {
        this.field = field;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement jsonElement = json.getAsJsonObject().get(field);
        return new Gson().fromJson(jsonElement, typeOfT);
    }
}
