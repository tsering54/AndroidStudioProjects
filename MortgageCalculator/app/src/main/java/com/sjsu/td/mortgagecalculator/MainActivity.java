package com.sjsu.td.mortgagecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.app.Fragment fragment = new InputFragment();
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.Input_Fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.reset) {
            EditText homeValue = (EditText) findViewById(R.id.etHomeValue);
            EditText downPayment = (EditText) findViewById(R.id.etDownPayment);
            EditText apr = (EditText) findViewById(R.id.etApr);
            EditText taxRate = (EditText) findViewById(R.id.etTaxRate);
            Spinner terms = (Spinner) findViewById(R.id.etTerms);
            homeValue.setText("");
            downPayment.setText("");
            apr.setText("");
            taxRate.setText("");
            terms.setSelection(0);
        }

        return super.onOptionsItemSelected(item);
    }
}
