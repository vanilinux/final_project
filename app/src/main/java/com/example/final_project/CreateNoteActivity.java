package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private ImageButton saveButton;
    private ImageButton cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        saveButton = findViewById(R.id.imageButton2);
        cancelButton = findViewById(R.id.imageButton1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty()) {
            titleEditText.setError("Введите заголовок");
            Toast.makeText(this, "Введите заголовок заметки", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            contentEditText.setError("Введите текст заметки");
            Toast.makeText(this, "Введите текст заметки", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("content", content);

        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "Заметка сохранена", Toast.LENGTH_SHORT).show();

        finish();
    }
}
