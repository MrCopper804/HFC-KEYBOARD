package com.hfc.keyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.graphics.Color;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private Map<Character, String> engToHfc = new HashMap<>();
    private Map<String, String> hfcToEng = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupMaps();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#000000"));
        layout.setPadding(40, 40, 40, 40);

        // Buttons for Setup
        Button btnEnable = new Button(this);
        btnEnable.setText("1. ENABLE KEYBOARD");
        btnEnable.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)));

        Button btnSelect = new Button(this);
        btnSelect.setText("2. SELECT HFC");
        btnSelect.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showInputMethodPicker();
        });

        // Translation Box
        TextView label = new TextView(this);
        label.setText("\n--- HFC TRANSLATOR ---");
        label.setTextColor(Color.CYAN);

        EditText inputField = new EditText(this);
        inputField.setHint("Type English or Paste HFC here...");
        inputField.setHintTextColor(Color.GRAY);
        inputField.setTextColor(Color.WHITE);

        TextView outputField = new TextView(this);
        outputField.setTextSize(18);
        outputField.setTextColor(Color.GREEN);
        outputField.setPadding(0, 20, 0, 0);

        inputField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String in = s.toString();
                if (in.contains("+") || in.contains("[]") || in.contains("©")) {
                    outputField.setText("ENG: " + decode(in));
                } else {
                    outputField.setText("HFC: " + encode(in));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        layout.addView(btnEnable);
        layout.addView(btnSelect);
        layout.addView(label);
        layout.addView(inputField);
        layout.addView(outputField);
        setContentView(layout);
    }

    private void setupMaps() {
        // Data from Archive
        String[][] data = {{"a","+"}, {"b","[]"}, {"c","©"}, {"d","÷"}, {"e","="}, {"f","_"}, {"g",">"}, {"h","|"}, {"i","!"}, {"j","#"}, {"k",":"}, {"l","<"}, {"m","*"}, {"n","°"}, {"o","()"}, {"p","¶"}, {"q","?"}, {"r","®"}, {"s","$"}, {"t","™"}, {"u","'"}, {"v","^"}, {"w","&"}, {"x","`"}, {"y","~"}, {"z","℅"}};
        for(String[] pair : data) {
            engToHfc.put(pair[0].charAt(0), pair[1]);
            hfcToEng.put(pair[1], pair[0]);
        }
    }

    private String encode(String s) {
        StringBuilder sb = new StringBuilder();
        for(char c : s.toLowerCase().toCharArray()) {
            sb.append(engToHfc.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    private String decode(String s) {
        // Simple decoder for symbols
        for (Map.Entry<String, String> entry : hfcToEng.entrySet()) {
            s = s.replace(entry.getKey(), entry.getValue());
        }
        return s;
    }
}
