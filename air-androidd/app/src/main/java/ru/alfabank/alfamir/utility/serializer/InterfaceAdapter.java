package ru.alfabank.alfamir.utility.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ru.alfabank.alfamir.post.data.dto.PostInterface;

public class InterfaceAdapter<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject wrapper = (JsonObject) json;
        final JsonElement typeName = get(wrapper, "type");
        final JsonElement data = get(wrapper, "content");
        final Type actualType = typeForName(typeName);
        return context.deserialize(data, actualType);
    }

    private Type typeForName(final JsonElement typeElem) {
        if(typeElem.getAsString().equals("html")){
            return PostInterface.ContentElementHtml.class;
        } else if (typeElem.getAsString().equals("image")){
            return PostInterface.ContentElementImage.class;
        }
        throw new JsonParseException("");
    }

    private JsonElement get(final JsonObject wrapper, String memberName) {
        final JsonElement elem = wrapper.get(memberName);
        if (elem == null)
            throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
        return elem;
    }
}
