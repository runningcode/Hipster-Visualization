package com.osacky.hipsterviz.api.lastFmApi;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.osacky.hipsterviz.models.artist.RealArtistWithNoTags;
import com.osacky.hipsterviz.models.artist.RealArtistWithOneTag;
import com.osacky.hipsterviz.models.artist.RealArtistWithTags;
import com.osacky.hipsterviz.models.artist.RealBaseArtist;

import java.lang.reflect.Type;

@SuppressWarnings("unused")
public class ArtistDeserializer<T extends RealBaseArtist> implements
        JsonDeserializer<RealBaseArtist> {

    private static final String TAG = "ArtistDeserializer";

    public ArtistDeserializer() {
    }

    @Override
    public RealBaseArtist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
            context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String trackString = "artist";
        String tagsString = "tags";
        if (jsonObject.toString().equals("{}")) {
            throw new JsonParseException("Object was {}");
        } else if (jsonObject.has("error")) {
            String message = jsonObject.get("message").getAsString();
            throw new JsonParseException(message);
        } else if (jsonObject.has(trackString)) {
            final JsonObject jsonElement = jsonObject.getAsJsonObject(trackString);
            if (jsonElement.get(tagsString).isJsonPrimitive()) {
                return new Gson().fromJson(jsonElement, RealArtistWithNoTags.class);
            } else if (jsonElement.get(tagsString).getAsJsonObject().get("tag").isJsonObject()) {
                return new Gson().fromJson(jsonElement, RealArtistWithOneTag.class);
            } else {
                return new Gson().fromJson(jsonElement, RealArtistWithTags.class);
            }
        } else {
            if (jsonObject.get(tagsString).isJsonPrimitive()) {
                return new Gson().fromJson(json, RealArtistWithNoTags.class);
            } else if (jsonObject.get(tagsString).getAsJsonObject().get("tag").isJsonObject()) {
                return new Gson().fromJson(json, RealArtistWithOneTag.class);
            } else {
                return new Gson().fromJson(json, RealArtistWithTags.class);
            }
        }
    }
}
