package com.osacky.hipsterviz.api.lastFmApi;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.osacky.hipsterviz.models.track.RealBaseTrack;
import com.osacky.hipsterviz.models.track.RealTrackNoTags;
import com.osacky.hipsterviz.models.track.RealTrackWithOneTag;
import com.osacky.hipsterviz.models.track.RealTrackWithTags;

import java.lang.reflect.Type;

@SuppressWarnings("unused")
class TrackDeserializer<T extends RealBaseTrack> implements JsonDeserializer<RealBaseTrack> {

    private static final String TAG = "TrackDeserializer";

    public TrackDeserializer() {
    }

    @Override
    public RealBaseTrack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String trackString = "track";
        String topTagsString = "toptags";
        if (jsonObject.toString().equals("{}")) {
            throw new JsonParseException("Object was {}");
        } else if (jsonObject.has("error")) {
            String message = jsonObject.get("message").getAsString();
            throw new JsonParseException(message);
        } else if (jsonObject.has(trackString)) {
            final JsonObject jsonElement = jsonObject.getAsJsonObject(trackString);
            if (jsonElement.get(topTagsString).isJsonPrimitive()) {
                return new Gson().fromJson(jsonElement, RealTrackNoTags.class);
            } else if (jsonElement.get(topTagsString).getAsJsonObject().get("tag").isJsonObject()) {
                return new Gson().fromJson(jsonElement, RealTrackWithOneTag.class);
            } else {
                return new Gson().fromJson(jsonElement, RealTrackWithTags.class);
            }
        } else {
            if (jsonObject.get(topTagsString).isJsonPrimitive()) {
                return new Gson().fromJson(json, RealTrackNoTags.class);
            } else if (jsonObject.get(topTagsString).getAsJsonObject().get("tag").isJsonObject()) {
                return new Gson().fromJson(json, RealTrackWithOneTag.class);
            } else {
                return new Gson().fromJson(json, RealTrackWithTags.class);
            }
        }
    }
}
