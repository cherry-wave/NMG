package cherry_wave.nmg.view.syllables;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orm.SugarRecord;

import butterknife.BindString;
import cherry_wave.nmg.R;
import cherry_wave.nmg.model.Syllable;
import cherry_wave.nmg.view.NMGDialogFragment;

public class SyllableDialogFragment extends NMGDialogFragment {

    private static final String TAG = SyllableDialogFragment.class.getCanonicalName();
    private static final String ARG_CHARACTERS = "characters";

    private SyllableDialogListener syllableDialogListener;

    @BindString(R.string.syllable_hint)
    String hint;

    public interface SyllableDialogListener {
        public void onSyllableSave();
    }

    public static SyllableDialogFragment newInstance(String characters) {
        SyllableDialogFragment syllableDialog = new SyllableDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHARACTERS, characters);
        syllableDialog.setArguments(args);
        return syllableDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            syllableDialogListener = (SyllableDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement SyllableDialogListener interface");
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title(R.string.syllable_hint)
                .autoDismiss(false)
                .positiveText(R.string.save)
                .positiveColorRes(R.color.colorPrimary)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText editText = dialog.getInputEditText();
                        if (isValid(editText)) {
                            save(editText);
                            dialog.dismiss();
                        }
                    }
                })
                .negativeText(R.string.cancel)
                .negativeColorRes(R.color.colorPrimary)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(hint, getArguments().getString(ARG_CHARACTERS), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                    }
                });
        final MaterialDialog materialDialog = builder.build();

        return materialDialog;
    }

    private boolean isValid(EditText editText) {
        editText.setError(null);
        if (!Syllable.isValid(editText.getText().toString())) {
            editText.setError(getString(R.string.error_invalid_syllable));
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private void save(EditText editText) {
        SugarRecord.save(new Syllable(editText.getText().toString()));
        syllableDialogListener.onSyllableSave();
    }

}