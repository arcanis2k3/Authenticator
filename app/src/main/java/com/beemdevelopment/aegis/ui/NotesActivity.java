package com.beemdevelopment.aegis.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beemdevelopment.aegis.R;
import com.beemdevelopment.aegis.helpers.ViewHelper;
import com.beemdevelopment.aegis.ui.dialogs.Dialogs;
import com.beemdevelopment.aegis.ui.tasks.PasswordSlotDecryptTask;
import com.beemdevelopment.aegis.util.IOUtils;
import com.beemdevelopment.aegis.vault.NotesFile;
import com.beemdevelopment.aegis.vault.NotesFileException;
import com.beemdevelopment.aegis.vault.VaultFileCredentials;
import com.beemdevelopment.aegis.vault.VaultNote;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AegisActivity {
    private NoteAdapter _adapter;
    private View _emptyStateView;

    private final ActivityResultLauncher<Intent> _exportLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() != RESULT_OK || result.getData() == null || result.getData().getData() == null) {
                    return;
                }
                writeExport(result.getData().getData());
            });

    private final ActivityResultLauncher<Intent> _importLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() != RESULT_OK || result.getData() == null || result.getData().getData() == null) {
                    return;
                }
                readImport(result.getData().getData());
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (abortIfOrphan(savedInstanceState)) {
            return;
        }
        setContentView(R.layout.activity_notes);
        setSupportActionBar(findViewById(R.id.toolbar));
        ViewHelper.setupAppBarInsets(findViewById(R.id.app_bar_layout));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        _adapter = new NoteAdapter();
        RecyclerView list = findViewById(R.id.list_notes);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(_adapter);
        _emptyStateView = findViewById(R.id.vEmptyList);
        findViewById(R.id.fab).setOnClickListener(v -> showEditor(null));
        reload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.action_export_notes) {
            startExport();
            return true;
        } else if (itemId == R.id.action_import_notes) {
            startImport();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startExport() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("application/json")
                .putExtra(Intent.EXTRA_TITLE, "idroot-notes.json");
        _exportLauncher.launch(intent);
    }

    private void writeExport(Uri uri) {
        boolean encrypted = _vaultManager.getVault().isEncryptionEnabled();
        try (OutputStream stream = getContentResolver().openOutputStream(uri)) {
            if (stream == null) {
                throw new NotesFileException("no stream");
            }
            VaultFileCredentials creds = encrypted ? _vaultManager.getVault().getCredentials() : null;
            List<VaultNote> notes = new ArrayList<>();
            for (VaultNote note : _vaultManager.getVault().getNotes()) {
                notes.add(note);
            }
            stream.write(NotesFile.export(notes, creds));
        } catch (Exception e) {
            Toast.makeText(this, R.string.notes_export_error, Toast.LENGTH_LONG).show();
            return;
        }
        int message = encrypted ? R.string.notes_exported : R.string.notes_export_plain;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void startImport() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("*/*");
        _importLauncher.launch(intent);
    }

    private void readImport(Uri uri) {
        byte[] bytes;
        try (InputStream stream = getContentResolver().openInputStream(uri)) {
            if (stream == null) {
                throw new NotesFileException("no stream");
            }
            bytes = IOUtils.readAll(stream);
        } catch (Exception e) {
            Toast.makeText(this, R.string.notes_import_error, Toast.LENGTH_LONG).show();
            return;
        }

        NotesFile file;
        try {
            file = NotesFile.parse(bytes);
        } catch (NotesFileException e) {
            Toast.makeText(this, R.string.notes_import_wrong_file, Toast.LENGTH_LONG).show();
            return;
        }

        if (!file.isEncrypted()) {
            try {
                finishImport(file, null);
            } catch (NotesFileException e) {
                Toast.makeText(this, R.string.notes_import_error, Toast.LENGTH_LONG).show();
            }
            return;
        }
        if (_vaultManager.getVault().isEncryptionEnabled()) {
            try {
                finishImport(file, _vaultManager.getVault().getCredentials());
                return;
            } catch (NotesFileException ignored) {
            }
        }
        if (file.getPasswordSlots().isEmpty()) {
            Toast.makeText(this, R.string.notes_import_error, Toast.LENGTH_LONG).show();
            return;
        }
        Dialogs.showPasswordInputDialog(this, R.string.notes_unlock, R.string.notes_import_password, password -> {
            PasswordSlotDecryptTask task = new PasswordSlotDecryptTask(this, result -> {
                if (result == null) {
                    Toast.makeText(this, R.string.notes_import_bad_password, Toast.LENGTH_LONG).show();
                    return;
                }
                VaultFileCredentials creds = new VaultFileCredentials(result.getKey(), file.getSlots());
                try {
                    finishImport(file, creds);
                } catch (NotesFileException e) {
                    Toast.makeText(this, R.string.notes_import_error, Toast.LENGTH_LONG).show();
                }
            });
            task.execute(new PasswordSlotDecryptTask.Params(file.getPasswordSlots(), password));
        }, null);
    }

    private void finishImport(NotesFile file, @Nullable VaultFileCredentials creds) throws NotesFileException {
        int added = 0;
        int updated = 0;
        for (VaultNote note : file.getNotes(creds)) {
            if (_vaultManager.getVault().getNotes().has(note.getUUID())) {
                _vaultManager.getVault().getNotes().replace(note);
                updated++;
            } else {
                _vaultManager.getVault().getNotes().add(note);
                added++;
            }
        }
        if (saveAndBackupVault()) {
            reload();
            Toast.makeText(this, getString(R.string.notes_imported, added, updated), Toast.LENGTH_LONG).show();
        }
    }

    private void reload() {
        List<VaultNote> notes = new ArrayList<>();
        for (VaultNote note : _vaultManager.getVault().getNotes()) {
            notes.add(note);
        }
        _adapter.setNotes(notes);
        _emptyStateView.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showEditor(@Nullable VaultNote existing) {
        View view = getLayoutInflater().inflate(R.layout.dialog_note, null);
        TextInputEditText titleView = view.findViewById(R.id.text_note_title);
        TextInputEditText bodyView = view.findViewById(R.id.text_note_body);
        if (existing != null) {
            titleView.setText(existing.getTitle());
            bodyView.setText(existing.getBody());
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(existing == null ? R.string.add_note : R.string.edit_note)
                .setView(view)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String title = textOf(titleView).trim();
                    String body = textOf(bodyView);
                    if (title.isEmpty() && body.trim().isEmpty()) {
                        return;
                    }
                    if (existing == null) {
                        VaultNote note = new VaultNote();
                        note.setTitle(title);
                        note.setBody(body);
                        _vaultManager.getVault().getNotes().add(note);
                    } else {
                        existing.setTitle(title);
                        existing.setBody(body);
                    }
                    if (saveAndBackupVault()) {
                        reload();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        if (existing != null) {
            builder.setNeutralButton(R.string.delete_note, (dialog, which) -> confirmDelete(existing));
        }
        builder.show();
    }

    private void confirmDelete(VaultNote note) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_note)
                .setMessage(R.string.delete_note_confirm)
                .setPositiveButton(R.string.delete_note, (dialog, which) -> {
                    _vaultManager.getVault().getNotes().remove(note);
                    if (saveAndBackupVault()) {
                        reload();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @SuppressLint("InlinedApi")
    private void copyNote(VaultNote note) {
        String text = note.getTitle().isEmpty() ? note.getBody() : note.getTitle() + "\n" + note.getBody();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text/plain", text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            PersistableBundle extras = new PersistableBundle();
            extras.putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true);
            clip.getDescription().setExtras(extras);
        }
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.note_copied, Toast.LENGTH_SHORT).show();
    }

    private static String textOf(TextInputEditText view) {
        return view.getText() == null ? "" : view.getText().toString();
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.Holder> {
        private final List<VaultNote> _notes = new ArrayList<>();

        void setNotes(List<VaultNote> notes) {
            _notes.clear();
            _notes.addAll(notes);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_note, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            VaultNote note = _notes.get(position);
            String title = note.getTitle().isEmpty() ? getString(R.string.note_untitled) : note.getTitle();
            holder.title.setText(title);
            holder.body.setText(note.getBody());
            holder.body.setVisibility(note.getBody().isEmpty() ? View.GONE : View.VISIBLE);
            holder.content.setOnClickListener(v -> showEditor(note));
            holder.copy.setOnClickListener(v -> copyNote(note));
        }

        @Override
        public int getItemCount() {
            return _notes.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            final View content;
            final TextView title;
            final TextView body;
            final ImageButton copy;

            Holder(@NonNull View itemView) {
                super(itemView);
                content = itemView.findViewById(R.id.note_content);
                title = itemView.findViewById(R.id.text_note_title);
                body = itemView.findViewById(R.id.text_note_body);
                copy = itemView.findViewById(R.id.button_copy_note);
            }
        }
    }
}
