package com.beemdevelopment.aegis.vault;

import com.beemdevelopment.aegis.util.UUIDMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class VaultNote extends UUIDMap.Value {
    private String _title = "";
    private String _body = "";

    public VaultNote() {
        super();
    }

    public VaultNote(UUID uuid) {
        super(uuid);
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title == null ? "" : title;
    }

    public String getBody() {
        return _body;
    }

    public void setBody(String body) {
        _body = body == null ? "" : body;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uuid", getUUID().toString());
            obj.put("title", _title);
            obj.put("body", _body);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static VaultNote fromJson(JSONObject obj) throws JSONException {
        UUID uuid = obj.has("uuid") ? UUID.fromString(obj.getString("uuid")) : UUID.randomUUID();
        VaultNote note = new VaultNote(uuid);
        note.setTitle(obj.optString("title", ""));
        note.setBody(obj.optString("body", ""));
        return note;
    }
}
