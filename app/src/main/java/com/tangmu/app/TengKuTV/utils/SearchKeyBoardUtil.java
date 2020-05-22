package com.tangmu.app.TengKuTV.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.tangmu.app.TengKuTV.R;

import java.lang.reflect.Method;

public class SearchKeyBoardUtil {

    private KeyboardView keyboardView;
    private EditText editText;
    private Keyboard k1;// 自定义键盘

    public SearchKeyBoardUtil(KeyboardView keyboardView, EditText editText) {
        //setInputType为InputType.TYPE_NULL   不然会弹出系统键盘
//        editText.setInputType(InputType.TYPE_NULL);
        k1 = new Keyboard(editText.getContext(), R.xml.key_full);
        this.keyboardView = keyboardView;
        this.editText = editText;
        editText.setCursorVisible(true);
        this.keyboardView.setOnKeyboardActionListener(listener);
        this.keyboardView.setKeyboard(k1);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:
                case Keyboard.KEYCODE_DONE:
                    break;
                default:
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }
    };

    // Activity中获取焦点时调用，显示出键盘
    public void showKeyboard() {
        setShowSoftInputOnFocus(editText);
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    // 隐藏键盘
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }

    private void setShowSoftInputOnFocus(EditText... editTexts) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {//4.0以下
            for (EditText editText : editTexts) {
                editText.setInputType(InputType.TYPE_NULL);
            }
        } else {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                for (EditText editText : editTexts) {
                    setShowSoftInputOnFocus.invoke(editText, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
