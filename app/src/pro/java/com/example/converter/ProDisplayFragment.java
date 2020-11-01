package com.example.converter;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.ClipboardManager;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ProDisplayFragment extends DisplayFragment {

    private Button mCpyInput;
    private Button mCpyOutput;
    private Button mSwap;

    public ProDisplayFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pro_display, container, false);
        findViews(root);
        subscribe();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void findViews(View v) {
        super.findViews(v);
        mCpyInput = v.findViewById(R.id.button_cpy_input);
        mCpyOutput = v.findViewById(R.id.button_cpy_output);
        mSwap = v.findViewById(R.id.button_units_switch);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    void subscribe() {
        super.subscribe();
        View.OnClickListener onCpyClick = v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip;
            if (v == mCpyInput) {
                clip = ClipData.newPlainText("", mInputTextView.getText().toString());
                clipboard.setPrimaryClip(clip);
            } else
            if ( v == mCpyOutput) {
                clip = ClipData.newPlainText("", mOutputTextView.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
            Toast toast = Toast.makeText(requireContext(), R.string.toast_message, Toast.LENGTH_SHORT);
            toast.show();
        };

        View.OnClickListener onSwap = v -> {
            String output = mOutputTextView.getText().toString();
            String inputUnit = mInputSpinner.getSelectedItem().toString();
            String outputUnit = mOutputSpinner.getSelectedItem().toString();
            mInputSpinner.setSelection(((ArrayAdapter)mInputSpinner.getAdapter()).getPosition(outputUnit));
            mOutputSpinner.setSelection(((ArrayAdapter)mInputSpinner.getAdapter()).getPosition(inputUnit));
            mConverterViewModel.setInput(output);
        };

        mCpyOutput.setOnClickListener(onCpyClick);
        mCpyInput.setOnClickListener(onCpyClick);
        mSwap.setOnClickListener(onSwap);
    }
}