package com.hfc.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.InputConnection;
import java.util.HashMap;
import java.util.Map;

public class HfcKeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private boolean isHfcMode = true; // Default HFC mode
    private Map<String, String> hfcToEng = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        // HFC to English Mapping
        hfcToEng.put("+", "a");  hfcToEng.put("[]", "b"); hfcToEng.put("©", "c");
        hfcToEng.put("÷", "d");  hfcToEng.put("=", "e");  hfcToEng.put("_", "f");
        hfcToEng.put(">", "g");  hfcToEng.put("|", "h");  hfcToEng.put("!", "i");
        hfcToEng.put("#", "j");  hfcToEng.put(":", "k");  hfcToEng.put("<", "l");
        hfcToEng.put("*", "m");  hfcToEng.put("°", "n");  hfcToEng.put("()", "o");
        hfcToEng.put("¶", "p");  hfcToEng.put("?", "q");  hfcToEng.put("®", "r");
        hfcToEng.put("$", "s");  hfcToEng.put("™", "t");  hfcToEng.put("'", "u");
        hfcToEng.put("^", "v");  hfcToEng.put("&", "w");  hfcToEng.put("`", "x");
        hfcToEng.put("~", "y");  hfcToEng.put("℅", "z");
    }

    @Override
    public View onCreateInputView() {
        KeyboardView kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        kv.setKeyboard(new Keyboard(this, R.xml.qwerty));
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        if (primaryCode == -100) { // Custom code for Switch Button
            isHfcMode = !isHfcMode;
            return;
        }

        if (primaryCode == Keyboard.KEYCODE_DELETE) {
            ic.deleteSurroundingText(1, 0);
        } else if (primaryCode == 32) {
            ic.commitText(" ", 1);
        } else {
            char code = (char) primaryCode;
            String output = String.valueOf(code);
            
            if (isHfcMode) {
                output = convertToHfc(code);
            } else {
                // Agar user HFC symbol type kare, to Eng nikle (Logic for Decryption)
                // Note: QWERTY par letters hain, isliye manually switch mode zaroori hai
                output = String.valueOf(code); 
            }
            ic.commitText(output, 1);
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
