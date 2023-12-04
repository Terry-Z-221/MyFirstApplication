package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class BookItemDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item_details);

        Intent intent = new Intent();
        EditText editTextItemName = findViewById(R.id.editTextText_bookName);

        Button buttonOk = findViewById(R.id.button_item_details_ok);
        buttonOk.setOnClickListener(view -> {
            intent.putExtra("name", editTextItemName.getText().toString());
            setResult(Activity.RESULT_OK, intent);
            BookItemDetailsActivity.this.finish();
        });

        Button buttonCancel = findViewById(R.id.button_item_details_cancel);
        buttonCancel.setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED, intent);
            BookItemDetailsActivity.this.finish();
        });
    }
}