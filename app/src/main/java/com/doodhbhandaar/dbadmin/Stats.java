package com.doodhbhandaar.dbadmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Stats extends Fragment {
    Double Cost = 0.0;
    Double totalquantity= 0.0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView textViewTotal;
    Spinner spinner_due_paid, spinner_months;
    private String mParam1;
    private String mParam2;

    RecyclerView customerRecyclerView;
    DatabaseReference customersReference,customerdataReference;
    public Stats() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customersReference = FirebaseDatabaseReference.getDatabaseInstance().getReference("CUSTOMERS");
        customerdataReference = FirebaseDatabaseReference.getDatabaseInstance().getReference("CUSTMERSDATA");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_stats, container, false);
        spinner_due_paid = (Spinner) view.findViewById(R.id.due_paid);
        spinner_months = (Spinner) view.findViewById(R.id.month);
        textViewTotal= (TextView) view.findViewById(R.id.text_total);

        ArrayAdapter<CharSequence> adapter_due_paid = ArrayAdapter.createFromResource(getContext(), R.array.duePaid
                , android.R.layout.simple_spinner_item);
        adapter_due_paid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<CharSequence> adapter_months = ArrayAdapter.createFromResource(getContext(), R.array.months
                , android.R.layout.simple_spinner_item);
        adapter_months.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_due_paid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateCostByDuePaid();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateCostByMonth();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_months.setAdapter(adapter_months);
        spinner_due_paid.setAdapter(adapter_due_paid);

        spinner_months.setSelection(Calendar.MONTH);
        spinner_due_paid.setSelection(0);



        return view;


    }

    private void updateCostByMonth() {

       final int months= spinner_months.getSelectedItemPosition();
        final Double[] totalCost = {0.0};
        customerdataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalquantity=0.0;

                for(final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){

//                        totalquantity=0.0;
                        Log.i("key" , dataSnapshot2.getKey()+" "+ Calendar.getInstance().get(Calendar.YEAR) + "" + (months + 1));

                        if(dataSnapshot2.getKey().equals(Calendar.getInstance().get(Calendar.YEAR) + "" + (months + 1))){

                            for(DataSnapshot dates: dataSnapshot2.getChildren()) {
                                if (!dates.getKey().equals("TotalCostPaid"))
                                    totalquantity += Double.parseDouble(dates.child("quantity").getValue().toString());
                            }
                            Log.i("Cost" , ""+totalquantity);

                            Cost=0.0;
                            customersReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                   Cost =Double.parseDouble( dataSnapshot.child(dataSnapshot1.getKey()).child("Cost").getValue().toString());
                                    Log.i("Cost" , dataSnapshot1.getKey().toString()+""+Cost);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        totalCost[0] +=totalquantity*Cost;
                        Log.i("totalCost" , ""+totalCost[0]);

                    }
                }
                textViewTotal.setText("Total " + spinner_due_paid.getSelectedItem().toString() + " : " + totalCost[0] );


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void updateCostByDuePaid() {

        totalquantity=0.0;
    }
}
