package com.example.converter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KeyboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KeyboardFragment extends Fragment {

    private ConverterViewModel mConverterViewModel;
    private Button mBtn00;
    private Button mBtn0;
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private Button mBtn4;
    private Button mBtn5;
    private Button mBtn6;
    private Button mBtn7;
    private Button mBtn8;
    private Button mBtn9;
    private Button mBtnDot;
    private Button mBtnE;
    private Button mBtnC;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KeyboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KeyboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KeyboardFragment newInstance(String param1, String param2) {
        KeyboardFragment fragment = new KeyboardFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_keyboard, container, false);
        mConverterViewModel = new ViewModelProvider(requireActivity()).get(ConverterViewModel.class);
        mBtn0 = (Button) root.findViewById(R.id.button_0);
        mBtn1 = (Button) root.findViewById(R.id.button_1);
        mBtn2 = (Button) root.findViewById(R.id.button_2);
        mBtn3 = (Button) root.findViewById(R.id.button_3);
        mBtn4 = (Button) root.findViewById(R.id.button_4);
        mBtn5 = (Button) root.findViewById(R.id.button_5);
        mBtn6 = (Button) root.findViewById(R.id.button_6);
        mBtn7 = (Button) root.findViewById(R.id.button_7);
        mBtn8 = (Button) root.findViewById(R.id.button_8);
        mBtn9 = (Button) root.findViewById(R.id.button_9);
        mBtn00 = (Button) root.findViewById(R.id.button_double_zero);
        mBtnC = (Button) root.findViewById(R.id.button_clear);
        mBtnE = (Button) root.findViewById(R.id.button_erase);
        mBtnDot = (Button) root.findViewById(R.id.button_dot);

        subscribe();

        return root;
    }

    private void subscribe() {
        View.OnClickListener onDigitClick = v -> {
            if (v == mBtn0) {
                mConverterViewModel.addDigit("0");
            } else
            if (v == mBtn1) {
                mConverterViewModel.addDigit("1");
            }
            if (v == mBtn2) {
                mConverterViewModel.addDigit("2");
            } else
            if (v == mBtn3) {
                mConverterViewModel.addDigit("3");
            }
            if (v == mBtn4) {
                mConverterViewModel.addDigit("4");
            } else
            if (v == mBtn5) {
                mConverterViewModel.addDigit("5");
            }
            if (v == mBtn6) {
                mConverterViewModel.addDigit("6");
            } else
            if (v == mBtn7) {
                mConverterViewModel.addDigit("7");
            }
            if (v == mBtn8) {
                mConverterViewModel.addDigit("8");
            } else
            if (v == mBtn9) {
                mConverterViewModel.addDigit("9");
            }
            if (v == mBtn00) {
                mConverterViewModel.addDigit("00");
            }
        };

        mBtn0.setOnClickListener(onDigitClick);
        mBtn1.setOnClickListener(onDigitClick);
        mBtn2.setOnClickListener(onDigitClick);
        mBtn3.setOnClickListener(onDigitClick);
        mBtn4.setOnClickListener(onDigitClick);
        mBtn5.setOnClickListener(onDigitClick);
        mBtn6.setOnClickListener(onDigitClick);
        mBtn7.setOnClickListener(onDigitClick);
        mBtn8.setOnClickListener(onDigitClick);
        mBtn9.setOnClickListener(onDigitClick);
        mBtn00.setOnClickListener(onDigitClick);
    }
}