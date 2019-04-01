package sidev17.siits.proshare.Utils.ViewTool;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EditTextMod {
    public static void enableEditText(EditText ed, final int inputType, boolean enable){
        if(enable){
            ed.setKeyListener(new KeyListener() {
                @Override
                public int getInputType() {
                    return inputType;
                }

                @Override
                public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                    return false;
                }

                @Override
                public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                    return false;
                }

                @Override
                public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                    return false;
                }

                @Override
                public void clearMetaKeyState(View view, Editable content, int states) {

                }
            });
            ed.getBackground().setAlpha(255);
            ed.getBackground().setTint(Color.parseColor("#000000"));
            InputMethodManager imm= (InputMethodManager) ed.getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(ed, InputMethodManager.SHOW_IMPLICIT);
            Selection.setSelection(ed.getText(), ed.length());
        } else{
            ed.setInputType(InputType.TYPE_NULL);
            ed.setKeyListener(null);
            ed.getBackground().setAlpha(0);
            InputMethodManager imm= (InputMethodManager) ed.getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
        }
        ed.setEnabled(enable);
    }

    public static void aturTulisan(TextView tv, CharSequence tulisan, int batas){
    }

    public static void tampilkanSoftKey(View v, boolean tampilkan){
        final InputMethodManager imm= (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if(tampilkan)
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        else
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
