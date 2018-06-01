package com.sjsu.td.mortgagecalculator;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class InputFragment extends android.app.Fragment {
    public static final String TAG_INPUT = "INPUT_FRAGMENT";
    Button calculate;
    EditText homeValue;
    EditText downPayment;
    EditText apr;
    Spinner terms;
    EditText taxRate;
    Double etHomeValue;
    Double etDownPayment;
    Double etApr;
    Double etTerms;
    Double etTaxRate;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static InputFragment newInstance(String param1, String param2) {
        InputFragment fragment = new InputFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        homeValue = (EditText) view.findViewById(R.id.etHomeValue);
        downPayment = (EditText) view.findViewById(R.id.etDownPayment);
        apr = (EditText) view.findViewById(R.id.etApr);
        terms = (Spinner) view.findViewById(R.id.etTerms);
        taxRate = (EditText) view.findViewById(R.id.etTaxRate);
        calculate = (Button) view.findViewById(R.id.calculate);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                buttonCalculateOnClick(v);
            }
        });
        return view;
    }

    private void buttonCalculateOnClick(View v) {
        if (validateInputValues()) {
            Log.i(TAG_INPUT, "got all value " + etHomeValue + " - " + etDownPayment + " - " + etApr +
                    " - " + etTerms + " - " + etTaxRate);
            calculateMortgageValues();
        }
    }

    public boolean validateInputValues() {
        if (homeValue.getText().toString() != "") {
            etHomeValue = Double.parseDouble(homeValue.getText().toString());
        } else {
            homeValue.setError("Home Value cannot be blank");
            return false;
        }

        if (downPayment.getText().toString() != "") {
            etDownPayment = Double.parseDouble(downPayment.getText().toString());
        } else {
            downPayment.setError("Down Payment cannot be blank");
            return false;
        }

        if (apr.getText().toString() != "") {
            etApr = Double.parseDouble(apr.getText().toString());
        } else {
            apr.setError("APR cannot be blank");
            return false;
        }

        if (taxRate.getText().toString() != "") {
            etTaxRate = Double.parseDouble(taxRate.getText().toString());
        } else {
            taxRate.setError("Tax Rate cannot be blank");
            return false;
        }

        etTerms = Double.parseDouble(terms.getSelectedItem().toString());
        return true;

    }

    public void calculateMortgageValues() {
        double N = etTerms * 12;
        double P = etHomeValue - etDownPayment;
        double totalPropertyTax = etHomeValue * etTaxRate * etTerms / 100;

        double monthlyPropertyTax = totalPropertyTax / N;

        double i = etApr / 1200;
        double tt = Math.pow(1 + i, N);
        double num = P * i * tt;
        double den = tt - 1;
        double monthlyPayment = num / den;
        double totalInterestPaid = monthlyPayment + monthlyPropertyTax;
        double totalMonthlyPayment = (totalInterestPaid * N) - P - totalPropertyTax;

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, (int) N);

        String date = new SimpleDateFormat("MMM").format(cal.getTime()) + ", " +
                new SimpleDateFormat("yyyy").format(cal.getTime());


        android.app.Fragment fragment2 = new OutputFragment();
        FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putString("TPT", round(totalPropertyTax, 2) + "");
        args.putString("TMP", round(totalMonthlyPayment, 2) + "");
        args.putString("TIP", round(totalInterestPaid, 2) + "");
        args.putString("DATE", date);
        fragment2.setArguments(args);
        ft.replace(R.id.Output_Fragment, fragment2);
        ft.commit();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public void onRestoreInstanceState() {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

}
