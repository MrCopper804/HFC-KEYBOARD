package com.hfc.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class HfcKeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private boolean isHfcMode = true;

    @Override
    public View onCreateInputView() {
        SharedPreferences prefs = getSharedPreferences("hfc_prefs", MODE_PRIVATE);
        boolean showNumbers = prefs.getBoolean("show_numbers", true);
        int heightBoost = prefs.getInt("kb_height", 50);

        KeyboardView kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        
        // Qwerty selection based on settings
        int layoutId = showNumbers ? R.xml.qwerty : R.xml.qwerty_no_numbers;
        Keyboard keyboard = new Keyboard(this, layoutId);
        
        // Set height dynamically
        keyboard.setKeyHeight(120 + heightBoost); 

        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        if (primaryCode == -100) { isHfcMode = !isHfcMode; return; }

        if (primaryCode == Keyboard.KEYCODE_DELETE) {
            ic.deleteSurroundingText(1, 0);
        } else if (primaryCode == 32) {
            ic.commitText(" ", 1);
        } else {
            char code = (char) primaryCode;
            if (isHfcMode) ic.commitText(convertToHfc(code), 1);
            else ic.commitText(String.valueOf(code), 1);
        }
    }

    private String convertToHfc(char c) {
        switch(Character.toLowerCase(c)) {
            case 'a': return "+";   case 'b': return "[]";  case 'c': return "©";
            case 'd': return "÷";   case 'e': return "=";   case 'f': return "_";
            case 'g': return ">";   case 'h': return "|";   case 'i': return "!";
            case 'j': return "#";   case 'k': return ":";   case 'l': return "<";
            case 'm': return "*";   case 'n': return "°";   case 'o': return "()";
            case 'p': return "¶";   case 'q': return "?";   case 'r': return "®";
            case 's': return "$";   case 't': return "™";   case 'u': return "'";
            case 'v': return "^";   case 'w': return "&";   case 'x': return "`";
            case 'y': return "~";   case 'z': return "℅";
            default: return String.valueOf(c);
        }
    }

    @Override public void onPress(int p) {} @Override public void onRelease(int p) {}
    @Override public void onText(CharSequence t) {} @Override public void swipeLeft() {}
    @Override public void swipeRight() {} @Override public void swipeDown() {} @Override public void swipeUp() {}
}
