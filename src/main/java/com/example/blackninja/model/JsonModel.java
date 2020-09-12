package com.example.blackninja.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;


public class JsonModel {
    private final JsonObject obj;

    public JsonModel(Gson gson, String json) {
        obj = gson.fromJson(json, JsonElement.class).getAsJsonObject();
    }

    public String getAsString(String memberName) {
        if (!obj.has(memberName)) {
            return null;
        }
        JsonElement element = obj.get(memberName);
        if (element.isJsonNull()) {
            return null;
        }
        try {
            return element.getAsString();
        } catch (Exception ex) {
            return null;
        }
    }

    public OptionalInt getAsInt(String memberName) {
        if (!obj.has(memberName)) {
            return OptionalInt.empty();
        }
        JsonElement element = obj.get(memberName);
        if (element.isJsonNull()) {
            return OptionalInt.empty();
        }
        try {
            return OptionalInt.of(element.getAsInt());
        } catch (Exception ex) {
            return OptionalInt.empty();
        }
    }

    public OptionalLong getAsLong(String memberName) {
        if (!obj.has(memberName)) {
            return OptionalLong.empty();
        }
        JsonElement element = obj.get(memberName);
        if (element.isJsonNull()) {
            return OptionalLong.empty();
        }
        try {
            return OptionalLong.of(element.getAsLong());
        } catch (Exception ex) {
            return OptionalLong.empty();
        }
    }

    public boolean getAsBoolean(String memberName) {
        if(obj.has(memberName)) {
            return obj.get(memberName).getAsBoolean();
        }

        return false;
    }

    public boolean has(String memberName) {
        return obj.has(memberName);
    }

    public List<String> getAsStringList(String memberName) {
        List<String> values = new ArrayList<>();

        if (!obj.has(memberName)) {
            return values;
        }

        try {
            JsonArray jsonArray = obj.getAsJsonArray(memberName);
            String[] arrName = new Gson().fromJson(jsonArray, String[].class);
            return new ArrayList<>(Arrays.asList(arrName));
        }catch (Exception ex) {
            return values;
        }
    }
}
