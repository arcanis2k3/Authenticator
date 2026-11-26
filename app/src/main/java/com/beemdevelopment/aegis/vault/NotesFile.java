package com.beemdevelopment.aegis.vault;

import androidx.annotation.Nullable;

import com.beemdevelopment.aegis.crypto.CryptResult;
import com.beemdevelopment.aegis.crypto.MasterKeyException;
import com.beemdevelopment.aegis.encoding.Base64;
import com.beemdevelopment.aegis.encoding.EncodingException;
import com.beemdevelopment.aegis.vault.slots.PasswordSlot;
import com.beemdevelopment.aegis.vault.slots.SlotList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A notes-only file. It never contains authenticator entries, so importing it
 * cannot replace or delete codes.
 */
public class NotesFile {
    public static final String TYPE = "idroot-notes";
    private static final int VERSION = 1;

    private final boolean _encrypted;
    @Nullable
    private final VaultFile.Header _header;
    @Nullable
    private final String _db;
    @Nullable
    private final JSONArray _plainNotes;

    private NotesFile(boolean encrypted, @Nullable VaultFile.Header header, @Nullable String db, @Nullable JSONArray plainNotes) {
        _encrypted = encrypted;
        _header = header;
        _db = db;
        _plainNotes = plainNotes;
    }

    public boolean isEncrypted() {
        return _encrypted;
    }

    @Nullable
    public SlotList getSlots() {
        return _header == null ? null : _header.getSlots();
    }

    public List<PasswordSlot> getPasswordSlots() {
        SlotList slots = getSlots();
        if (slots == null) {
            return new ArrayList<>();
        }
        return slots.findAll(PasswordSlot.class);
    }

    public static byte[] export(Collection<VaultNote> notes, @Nullable VaultFileCredentials creds) throws NotesFileException {
        try {
            JSONArray array = new JSONArray();
            for (VaultNote note : notes) {
                array.put(note.toJson());
            }

            JSONObject root = new JSONObject();
            root.put("type", TYPE);
            root.put("version", VERSION);

            if (creds != null) {
                JSONObject payload = new JSONObject();
                payload.put("notes", array);
                VaultFileCredentials exportable = creds.exportable();
                CryptResult result = exportable.encrypt(payload.toString().getBytes(StandardCharsets.UTF_8));
                VaultFile.Header header = new VaultFile.Header(exportable.getSlots(), result.getParams());
                root.put("encrypted", true);
                root.put("header", header.toJson());
                root.put("db", Base64.encode(result.getData()));
            } else {
                root.put("encrypted", false);
                root.put("notes", array);
            }

            return root.toString(4).getBytes(StandardCharsets.UTF_8);
        } catch (JSONException | MasterKeyException e) {
            throw new NotesFileException(e);
        }
    }

    public static NotesFile parse(byte[] data) throws NotesFileException {
        try {
            JSONObject root = new JSONObject(new String(data, StandardCharsets.UTF_8));
            if (!TYPE.equals(root.optString("type")) || root.optInt("version") > VERSION) {
                throw new NotesFileException("not a notes file");
            }
            if (root.optBoolean("encrypted")) {
                VaultFile.Header header = VaultFile.Header.fromJson(root.getJSONObject("header"));
                return new NotesFile(true, header, root.getString("db"), null);
            }
            return new NotesFile(false, null, null, root.getJSONArray("notes"));
        } catch (JSONException | VaultFileException e) {
            throw new NotesFileException(e);
        }
    }

    public List<VaultNote> getNotes(@Nullable VaultFileCredentials creds) throws NotesFileException {
        try {
            JSONArray array;
            if (!_encrypted) {
                array = _plainNotes;
            } else {
                if (creds == null || _header == null || _db == null) {
                    throw new NotesFileException("missing credentials");
                }
                byte[] bytes = Base64.decode(_db);
                CryptResult result = creds.decrypt(bytes, _header.getParams());
                JSONObject payload = new JSONObject(new String(result.getData(), StandardCharsets.UTF_8));
                array = payload.getJSONArray("notes");
            }

            List<VaultNote> notes = new ArrayList<>();
            if (array == null) {
                return notes;
            }
            for (int i = 0; i < array.length(); i++) {
                notes.add(VaultNote.fromJson(array.getJSONObject(i)));
            }
            return notes;
        } catch (JSONException | MasterKeyException | EncodingException e) {
            throw new NotesFileException(e);
        }
    }
}
