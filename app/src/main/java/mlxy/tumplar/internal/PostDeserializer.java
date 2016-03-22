package mlxy.tumplar.internal;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;

import mlxy.tumplar.entity.UnknownTypePost;

/** Modified from Jumblr (https://github.com/tumblr/jumblr). **/
public class PostDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject object = je.getAsJsonObject();
        String typeName = object.get("type").getAsString();
        String className = typeName.substring(0, 1).toUpperCase() + typeName.substring(1) + "Post";
        try {
            Class<?> clz = Class.forName("mlxy.tumplar.entity." + className);
            return jdc.deserialize(je, clz);
        } catch (ClassNotFoundException e) {
            Logger.w("deserialized post for unknown type: " + typeName);
            return jdc.deserialize(je, UnknownTypePost.class);
        }
    }
}
