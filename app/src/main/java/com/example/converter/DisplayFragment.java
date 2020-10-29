package com.example.converter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayFragment extends Fragment {

    private TextView mInputTextView;
    private TextView mOutputTextView;
    private Spinner mInputSpinner;
    private Spinner mOutputSpinner;
    private ConverterViewModel mConverterViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayFragment newInstance(String param1, String param2) {
        DisplayFragment fragment = new DisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_display, container, false);
        mInputTextView = root.findViewById(R.id.textView_input);
        mOutputTextView = root.findViewById(R.id.textView_output);
        mInputSpinner = root.findViewById(R.id.spinner_input);
        mOutputSpinner = root.findViewById(R.id.spinner_output);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.my_spinner_item, ConverterViewModel.mass_units);

        adapter.setDropDownViewResource(R.layout.my_spinner_dropdown_item);

        mInputSpinner.setAdapter(adapter);
        mOutputSpinner.setAdapter(adapter);
        mConverterViewModel = new ViewModelProvider(requireActivity()).get(ConverterViewModel.class);

        subscribe();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("SetTextI18n")
    void subscribe() {

        mConverterViewModel.input.observe(
                requireActivity(), value -> {
                    if (value != null) {
                        mInputTextView.setText(value);
                     }
                });

        mConverterViewModel.output.observe(
                requireActivity(), value -> {
                    if (value != null) {
                        mOutputTextView.setText(value);
                    }
                });

        AdapterView.OnItemSelectedListener onInputSpinnerItemSelected = new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mConverterViewModel.setInputUnit((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        AdapterView.OnItemSelectedListener onOutputSpinnerItemSelected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mConverterViewModel.setOutputUnit((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        mOutputSpinner.setOnItemSelectedListener(onOutputSpinnerItemSelected);

        mInputSpinner.setOnItemSelectedListener(onInputSpinnerItemSelected);
    }
}