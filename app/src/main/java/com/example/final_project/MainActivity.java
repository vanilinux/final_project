package com.example.final_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView notesRecyclerView;
    private TextView emptyStateText;
    private Button addButton;

    private DatabaseHelper dbHelper;
    private NotesAdapter notesAdapter;
    private List<Note> notesList;

    private static final int CREATE_NOTE_REQUEST = 1;
    private static final int EDIT_NOTE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        addButton = findViewById(R.id.addButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notesRecyclerView.setLayoutManager(layoutManager);

        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList);
        notesRecyclerView.setAdapter(notesAdapter);

        loadNotes();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateNoteActivity();
            }
        });

        notesAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openNoteForEditing(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteNote(position);
            }
        });
    }

    private void loadNotes() {
        notesList.clear();
        notesList.addAll(dbHelper.getAllNotes());
        notesAdapter.updateNotes(notesList);

        if (notesList.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            notesRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            notesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void openCreateNoteActivity() {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivityForResult(intent, CREATE_NOTE_REQUEST);
    }

    private void openNoteForEditing(int position) {
        if (position >= 0 && position < notesList.size()) {
            Note note = notesList.get(position);

            Intent intent = new Intent(this, CreateNoteActivity.class);
            intent.putExtra("NOTE_ID", note.getId());
            intent.putExtra("NOTE_TITLE", note.getTitle());
            intent.putExtra("NOTE_CONTENT", note.getContent());

            startActivityForResult(intent, EDIT_NOTE_REQUEST);
        }
    }

    private void deleteNote(int position) {
        if (position >= 0 && position < notesList.size()) {
            Note noteToDelete = notesList.get(position);
            dbHelper.deleteNote(noteToDelete.getId());

            Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show();

            loadNotes();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");

            String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(new Date());

            if (requestCode == CREATE_NOTE_REQUEST) {
                long id = dbHelper.addNote(title, content, currentDate);

                if (id != -1) {
                    Toast.makeText(this, "Заметка создана", Toast.LENGTH_SHORT).show();
                    loadNotes();
                }

            } else if (requestCode == EDIT_NOTE_REQUEST) {
                int noteId = data.getIntExtra("note_id", -1);

                if (noteId != -1) {
                    Note updatedNote = new Note(noteId, title, content, currentDate);

                    int rowsAffected = dbHelper.updateNote(updatedNote);

                    if (rowsAffected > 0) {
                        Toast.makeText(this, "Заметка обновлена", Toast.LENGTH_SHORT).show();
                        loadNotes();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}