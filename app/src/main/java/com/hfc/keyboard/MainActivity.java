package com.hfc.keyboard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
        SharedPreferences prefs = getSharedPreferences("hfc_prefs", MODE_PRIVATE);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.BLACK);
        layout.setPadding(50, 50, 50, 50);

        // --- Customization Section ---
        TextView title = new TextView(this);
        title.setText("HFC KEYBOARD PRO SETTINGS");
        title.setTextColor(Color.CYAN);
        title.setTextSize(22);
        title.setPadding(0, 0, 0, 40);

        CheckBox cbNumbers = new CheckBox(this);
        cbNumbers.setText("Show Number Row");
        cbNumbers.setTextColor(Color.WHITE);
        cbNumbers.setChecked(prefs.getBoolean("show_numbers", true));
        cbNumbers.setOnCheckedChangeListener((v, isChecked) -> prefs.edit().putBoolean("show_numbers", isChecked).apply());

        TextView hLabel = new TextView(this);
        hLabel.setText("\nKeyboard Height Boost:");
        hLabel.setTextColor(Color.WHITE);

        SeekBar heightBar = new SeekBar(this);
        heightBar.setMax(150);
        heightBar.setProgress(prefs.getInt("kb_height", 50));
        heightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean b) { prefs.edit().putInt("kb_height", p).apply(); }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });

        Button btnEnable = new Button(this);
        btnEnable.setText("ENABLE KEYBOARD");
        btnEnable.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)));

        // --- Translator Section ---
        TextView tLabel = new TextView(this);
        tLabel.setText("\n--- HFC TRANSLATOR ---");
        tLabel.setTextColor(Color.YELLOW);

        EditText inputField = new EditText(this);
        inputField.setHint("Type Eng or Paste HFC...");
        inputField.setHintTextColor(Color.GRAY);
        inputField.setTextColor(Color.WHITE);

        TextView outputField = new TextView(this);
        outputField.setTextSize(18);
        outputField.setTextColor(Color.GREEN);

        inputField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String in = s.toString();
                if (in.contains("+") || in.contains("[]") || in.contains("©")) outputField.setText("RESULT: " + decode(in));
                else outputField.setText("RESULT: " + encode(in));
            }
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) {}
        });

        layout.addView(title);
        layout.addView(cbNumbers);
        layout.addView(hLabel);
        layout.addView(heightBar);
        layout.addView(btnEnable);
        layout.addView(tLabel);
        layout.addView(inputField);
        layout.addView(outputField);
        
        scrollView.addView(layout);
        setContentView(scrollView);
    }

    private void setupMaps() {
        String[][] data = {{"a","+"}, {"b","[]"}, {"c","©"}, {"d","÷"}, {"e","="}, {"f","_"}, {"g",">"}, {"h","|"}, {"i","!"}, {"j","#"}, {"k",":"}, {"l","<"}, {"m","*"}, {"n","°"}, {"o","()"}, {"p","¶"}, {"q","?"}, {"r","®"}, {"s","$"}, {"t","™"}, {"u","'"}, {"v","^"}, {"w","&"}, {"x","`"}, {"y","~"}, {"z","℅"}};
        for(String[] p : data) { engToHfc.put(p[0].charAt(0), p[1]); hfcToEng.put(p[1], p[0]); }
    }

    private String encode(String s) {
        StringBuilder sb = new StringBuilder();
        for(char c : s.toLowerCase().toCharArray()) sb.append(engToHfc.getOrDefault(c, String.valueOf(c)));
        return sb.toString();
    }

    private String decode(String s) {
        for (Map.Entry<String, String> entry : hfcToEng.entrySet()) s = s.replace(entry.getKey(), entry.getValue());
        return s;
    }
}
