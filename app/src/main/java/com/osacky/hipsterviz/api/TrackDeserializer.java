package com.osacky.hipsterviz.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.osacky.hipsterviz.models.track.RealBaseTrack;
import com.osacky.hipsterviz.models.track.RealTrackNoTags;
import com.osacky.hipsterviz.models.track.RealTrackWithTags;

import java.lang.reflect.Type;

public class TrackDeserializer<T extends RealBaseTrack> implements JsonDeserializer<T> {
    @SuppressWarnings("unused")
    private static final String TAG = "TrackDeserializer";

    private final String field = "track";

    public TrackDeserializer() {
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.toString().equals("{}")) {
            throw new JsonParseException("Object was {}");
        } else if (jsonObject.has("error")) {
            String message = jsonObject.get("message").getAsString();
            throw new JsonParseException(message);
        } else if (jsonObject.has(field)) {
            final JsonObject jsonElement = jsonObject.getAsJsonObject(field);
            if (jsonElement.get("toptags").isJsonPrimitive()) {
                return (T) new Gson().fromJson(jsonElement, RealTrackNoTags.class);
            } else {
                return (T) new Gson().fromJson(jsonElement, RealTrackWithTags.class);
            }
        } else {
            final JsonObject jsonElement = jsonObject.getAsJsonObject(field);
            if (jsonElement.get("toptags").isJsonPrimitive()) {
                return (T) new Gson().fromJson(json, RealTrackNoTags.class);
            } else {
                return (T) new Gson().fromJson(json, RealTrackWithTags.class);
            }
        }
    }
}
