package ru.alfabank.alfamir.messenger.data.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ru.alfabank.alfamir.messenger.data.dto.ChatRaw;
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw;
import ru.alfabank.alfamir.messenger.data.dto.PollDataRaw;
import ru.alfabank.alfamir.messenger.data.dto.StatusRaw;
import ru.alfabank.alfamir.messenger.data.dto.UserRaw;

public class PollDataAdapter implements JsonDeserializer<PollDataRaw> {

    @Override
    public PollDataRaw deserialize(JsonElement json, Type type1, JsonDeserializationContext context) throws JsonParseException {
        JsonObject wrapper = (JsonObject) json;
        String login = wrapper.get("Login").getAsString();
        String type = wrapper.get("Type").getAsString();
        String date = wrapper.get("CreationDate").getAsString();
        String id = wrapper.get("Guid").getAsString();

        JsonElement elementValue = wrapper.get("Value");
        Type actualType = typeForName(type);
        PollDataRaw.PollDataValue value = context.deserialize(elementValue, actualType);

        PollDataRaw pollDataRaw = new PollDataRaw(login, type, value, date, id);
        return pollDataRaw;
    }

    private Type typeForName(String typeName) {
        if(typeName.equals("user")){
            return UserRaw.class;
        } else if (typeName.equals("message")){
            return MessageRaw.class;
        } else if(typeName.equals("messagestatus")){
            return StatusRaw.class;
        } else if(typeName.equals("chat")){
            return ChatRaw.class;
        }
        throw new JsonParseException("");
    }

}
