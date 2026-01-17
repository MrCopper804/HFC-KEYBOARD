package com.hfc.keyboard;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class HfcKeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    @Override public View onCreateInputView() {
        KeyboardView kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        kv.setKeyboard(new Keyboard(this, R.xml.qwerty));
        kv.setOnKeyboardActionListener(this);
        return kv;
    }
    @Override public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        if (primaryCode == Keyboard.KEYCODE_DELETE) { ic.deleteSurroundingText(1, 0); }
        else if (primaryCode == 32) { ic.commitText(" ", 1); }
        else { char code = (char) primaryCode; ic.commitText(convert(code), 1); }
    }
    private String convert(char c) {
        switch(Character.toLowerCase(c)) {
            case 'a': return "!"; case 'b': return "#"; case 'c': return "$"; case 'd': return "%";
            case 'e': return "&"; case 'f': return "*"; case 'g': return "("; case 'h': return ")";
            case 'i': return "+"; case 'j': return "-"; case 'k': return "="; case 'l': return "/";
            case 'm': return "?"; case 'n': return ">"; case 'o': return "<"; case 'p': return "^";
            case 'q': return "@"; case 'r': return "~"; case 's': return ":"; case 't': return ";";
            case 'u': return "["; case 'v': return "]"; case 'w': return "{"; case 'x': return "}";
            case 'y': return "|"; case 'z': return "£";
            default: return String.valueOf(c);
        }
    }
    @Override public void onPress(int p) {} @Override public void onRelease(int p) {}
    @Override public void onText(CharSequence t) {} @Override public void swipeLeft() {}
    @Override public void swipeRight() {} @Override public void swipeDown() {} @Override public void swipeUp() {}
}
