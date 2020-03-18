package com.doodhbhandaar.dbadmin;

import android.app.ProgressDialog;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class delivery_boy_stats extends AppCompatActivity {

    String delivery_boy_name;
    DatabaseReference customersReference,customerdataReference;
    ArrayList<CustomerData> delivery_boy_customerslist ,customerDataList;
    Spinner spinner;
    Double quantity_paid[] = new Double[31];
    Double quantity_due[] = new Double[31];
    Double morningQ=0.0,eveningQ=0.0;
    TextView total_litres,total_cost_due,total_cost_paid,total_cost_rate;
    TextView oneP,twoP,threeP,fourP,fiveP,sixP,sevenP,eightP,nineP,tenP,elevenP,twelveP,thirteenP,fourteenP,fifteenP,sixteenP,seventeenP,eightteenP,nineteenP,twentyP,twenty_oneP,twenty_twoP,twenty_threeP,twenty_fourP,twenty_fiveP,twenty_sixP,twenty_sevenP,twenty_eightP,twenty_nineP,thirtyP,thiety_oneP;
    TextView one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen,fourteen,fifteen,sixteen,seventeen,eightteen,nineteen,twenty,twenty_one,twenty_two,twenty_three,twenty_four,twenty_five,twenty_six,twenty_seven,twenty_eight,twenty_nine,thirty,thiety_one;
    LinearLayout ll1,ll2,ll3,ll4,ll5,ll6,ll7,ll8,ll9,ll10,ll11,ll12,ll13,ll14,ll15,ll16,ll17,ll18,ll19,ll20,ll21,ll22,ll23,ll24,ll25,ll26,ll27,ll28,ll29,ll30,ll31;
    ProgressDialog progressDialog;
    TextView morningQun,eveningQun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_stats);
        for (int i=0;i<31;i++)
        quantity_due[i]=quantity_paid[i]=0.0;
        progressDialog =new ProgressDialog(delivery_boy_stats.this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        delivery_boy_name = getIntent().getStringExtra("name");
        delivery_boy_customerslist = new ArrayList<>();

        customersReference = FirebaseDatabase.getInstance().getReference("CUSTOMERS");
        customerdataReference = FirebaseDatabaseReference.getDatabaseInstance().getReference("CUSTMERSDATA");
        setView();
//
//        CustomersFragment customersFragment = new CustomersFragment();

        customersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customerDataList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    CustomerData customerData =dataSnapshot1.getValue(CustomerData.class);
                    customerDataList.add(customerData);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.month_menu, menu);

            MenuItem item = menu.findItem(R.id.month);
            spinner = (Spinner) MenuItemCompat.getActionView(item);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.months
                    , android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {

                     morningQ=eveningQ=0.0;
                    for (int K=0;K<31;K++)
                        quantity_due[K]=quantity_paid[K]=0.0;
                    for (int j = 0; j < customerDataList.size(); j++) {

                        if (customerDataList.get(j).deliverBoyName.equals(delivery_boy_name)) {

                            delivery_boy_customerslist.add(customerDataList.get(j));


                            if(customerDataList.get(j).isMorning){
                                morningQ +=Double.parseDouble(customerDataList.get(j).defaultQuantity);
                            }
                            else if(customerDataList.get(j).isEvening){
                                Log.i("quan",customerDataList.get(j).defaultQuantity);
                                eveningQ +=Double.parseDouble(customerDataList.get(j).defaultQuantity);
                            }


                        }
                    }

                    morningQun.setText("Morning : " + morningQ + " Lt");
                    eveningQun.setText("Evening : " + eveningQ + " Lt");

                    customerdataReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (int i = 0; i < delivery_boy_customerslist.size(); i++) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals(delivery_boy_customerslist.get(i).pk)) {
                                for (DataSnapshot cd_year : snapshot.getChildren()) {
                                    Log.i("year", cd_year.getKey() + " " + Calendar.getInstance().get(Calendar.YEAR) + "" + (spinner.getSelectedItemPosition() + 1));
                                    if (cd_year.getKey().equals(Calendar.getInstance().get(Calendar.YEAR) + "" + (spinner.getSelectedItemPosition() + 1))) {
                                        for (DataSnapshot cd_date : cd_year.getChildren()) {
                                            final CustomerDeliveriesReference customerDeliveriesReference =
                                                    cd_date.getValue(CustomerDeliveriesReference.class);
                                         //   Log.i("q",customerDeliveriesReference.quantity);
                                            if(customerDeliveriesReference.isPaid)
                                                quantity_paid[Integer.parseInt(cd_date.getKey())-1] += Double.parseDouble(customerDeliveriesReference.quantity);
                                            else
                                                quantity_due[Integer.parseInt(cd_date.getKey())-1] += Double.parseDouble(customerDeliveriesReference.quantity);


                                        }
                                    }
                                }
                            }
                        }
                    }

                    setQuantity();

                }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinner.setAdapter(adapter);
            spinner.setSelection(Calendar.MONTH);

            return true;

        }

    private void setQuantity() {

        one.setText(quantity_due[0] + " Lt");
        two.setText(quantity_due[1] + " Lt");
        three.setText(quantity_due[2] + " Lt");
        four.setText(quantity_due[3] + " Lt");
        five.setText(quantity_due[4] + " Lt");
        six.setText(quantity_due[5] + " Lt");
        seven.setText(quantity_due[6] + " Lt");
        eight.setText(quantity_due[7] + " Lt");
        nine.setText(quantity_due[8] + " Lt");
        ten.setText(quantity_due[9] + " Lt");
        eleven.setText(quantity_due[10] + " Lt");
        twelve.setText(quantity_due[11] + " Lt");
        thirteen.setText(quantity_due[12] + " Lt");
        fourteen.setText(quantity_due[13] + " Lt");
        fifteen.setText(quantity_due[14] + " Lt");
        sixteen.setText(quantity_due[15] + " Lt");
        seventeen.setText(quantity_due[16] + " Lt");
        eightteen.setText(quantity_due[17] + " Lt");
        nineteen.setText(quantity_due[18] + " Lt");
        twenty.setText(quantity_due[19] + " Lt");
        twenty_one.setText(quantity_due[20] + " Lt");
        twenty_two.setText(quantity_due[21] + " Lt");
        twenty_three.setText(quantity_due[22] + " Lt");
        twenty_four.setText(quantity_due[23] + " Lt");
        twenty_five.setText(quantity_due[24] + " Lt");
        twenty_six.setText(quantity_due[25] + " Lt");
        twenty_seven.setText(quantity_due[26] + " Lt");
        twenty_eight.setText(quantity_due[27] + " Lt");
        twenty_nine.setText(quantity_due[28] + " Lt");
        thirty.setText(quantity_due[29] + " Lt");
        thiety_one.setText(quantity_due[30] + " Lt");


        oneP.setText(quantity_paid[0] + " Lt");
        twoP.setText(quantity_paid[1] + " Lt");
        threeP.setText(quantity_paid[2] + " Lt");
        fourP.setText(quantity_paid[3] + " Lt");
        fiveP.setText(quantity_paid[4] + " Lt");
        sixP.setText(quantity_paid[5] + " Lt");
        sevenP.setText(quantity_paid[6] + " Lt");
        eightP.setText(quantity_paid[7] + " Lt");
        nineP.setText(quantity_paid[8] + " Lt");
        tenP.setText(quantity_paid[9] + " Lt");
        elevenP.setText(quantity_paid[10] + " Lt");
        twelveP.setText(quantity_paid[11] + " Lt");
        thirteenP.setText(quantity_paid[12] + " Lt");
        fourteenP.setText(quantity_paid[13] + " Lt");
        fifteenP.setText(quantity_paid[14] + " Lt");
        sixteenP.setText(quantity_paid[15] + " Lt");
        seventeenP.setText(quantity_paid[16] + " Lt");
        eightteenP.setText(quantity_paid[17] + " Lt");
        nineteenP.setText(quantity_paid[18] + " Lt");
        twentyP.setText(quantity_paid[19] + " Lt");
        twenty_oneP.setText(quantity_paid[20] + " Lt");
        twenty_twoP.setText(quantity_paid[21] + " Lt");
        twenty_threeP.setText(quantity_paid[22] + " Lt");
        twenty_fourP.setText(quantity_paid[23] + " Lt");
        twenty_fiveP.setText(quantity_paid[24] + " Lt");
        twenty_sixP.setText(quantity_paid[25] + " Lt");
        twenty_sevenP.setText(quantity_paid[26] + " Lt");
        twenty_eightP.setText(quantity_paid[27] + " Lt");
        twenty_nineP.setText(quantity_paid[28] + " Lt");
        thirtyP.setText(quantity_paid[29] + " Lt");
        thiety_oneP.setText(quantity_paid[30] + " Lt");


        double ttlltP=0, ttlltD=0;
        for(int r=0;r<31;r++) {
            ttlltP += quantity_paid[r];
            ttlltD += quantity_due[r];

        }

        total_litres.setText((ttlltD + ttlltP) + " Lt");
        total_cost_due.setText("₹ "+ (ttlltD*50) );
        total_cost_paid.setText("₹ "+ (ttlltP*50) );



    }


//        customersReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//
//                    CustomerData customerData = postSnapshot.getValue(CustomerData.class);
//
//                    if(customerData.deliverBoyName.equals(delivery_boy_name)){
//
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
    //    });

    public void setView(){

        total_litres=(TextView)findViewById(R.id.total_litres);
        total_cost_due=(TextView)findViewById(R.id.total_cost_due);
        total_cost_paid=(TextView)findViewById(R.id.total_cost_paid);

        one = (TextView) findViewById(R.id.one);
        two = (TextView) findViewById(R.id.two);
        three = (TextView) findViewById(R.id.three);
        four = (TextView) findViewById(R.id.four);
        five = (TextView) findViewById(R.id.five);
        six = (TextView) findViewById(R.id.six);
        seven = (TextView) findViewById(R.id.seven);
        eight = (TextView) findViewById(R.id.eight);
        nine = (TextView) findViewById(R.id.nine);
        ten = (TextView) findViewById(R.id.ten);
        eleven = (TextView) findViewById(R.id.eleven);
        twelve = (TextView) findViewById(R.id.twelve);
        thirteen = (TextView) findViewById(R.id.thirteen);
        fourteen = (TextView) findViewById(R.id.fourteen);
        fifteen = (TextView) findViewById(R.id.fifteen);
        sixteen = (TextView) findViewById(R.id.sixteen);
        seventeen = (TextView) findViewById(R.id.seventeen);
        eightteen = (TextView) findViewById(R.id.eighteen);
        nineteen = (TextView) findViewById(R.id.nineteen);
        twenty = (TextView) findViewById(R.id.twenty);
        twenty_one = (TextView) findViewById(R.id.twentyone);
        twenty_two = (TextView) findViewById(R.id.twentytwo);
        twenty_three = (TextView) findViewById(R.id.twentythree);
        twenty_four = (TextView) findViewById(R.id.twentyfour);
        twenty_five = (TextView) findViewById(R.id.twentyfive);
        twenty_six = (TextView) findViewById(R.id.twentysix);
        twenty_seven = (TextView) findViewById(R.id.twentyseven);
        twenty_eight = (TextView) findViewById(R.id.twentyeight);
        twenty_nine = (TextView) findViewById(R.id.twentynine);
        thirty = (TextView) findViewById(R.id.thirty);
        thiety_one = (TextView) findViewById(R.id.thirtyone);

        oneP = (TextView) findViewById(R.id.oneP);
        twoP = (TextView) findViewById(R.id.twoP);
        threeP = (TextView) findViewById(R.id.threeP);
        fourP = (TextView) findViewById(R.id.fourP);
        fiveP = (TextView) findViewById(R.id.fiveP);
        sixP = (TextView) findViewById(R.id.sixP);
        sevenP = (TextView) findViewById(R.id.sevenP);
        eightP = (TextView) findViewById(R.id.eightP);
        nineP = (TextView) findViewById(R.id.nineP);
        tenP = (TextView) findViewById(R.id.tenP);
        elevenP = (TextView) findViewById(R.id.elevenP);
        twelveP = (TextView) findViewById(R.id.twelveP);
        thirteenP = (TextView) findViewById(R.id.thirteenP);
        fourteenP = (TextView) findViewById(R.id.fourteenP);
        fifteenP = (TextView) findViewById(R.id.fifteenP);
        sixteenP = (TextView) findViewById(R.id.sixteenP);
        seventeenP = (TextView) findViewById(R.id.seventeenP);
        eightteenP = (TextView) findViewById(R.id.eighteenP);
        nineteenP = (TextView) findViewById(R.id.nineteenP);
        twentyP = (TextView) findViewById(R.id.twentyP);
        twenty_oneP = (TextView) findViewById(R.id.twentyoneP);
        twenty_twoP = (TextView) findViewById(R.id.twentytwoP);
        twenty_threeP = (TextView) findViewById(R.id.twentythreeP);
        twenty_fourP = (TextView) findViewById(R.id.twentyfourP);
        twenty_fiveP = (TextView) findViewById(R.id.twentyfiveP);
        twenty_sixP = (TextView) findViewById(R.id.twentysixP);
        twenty_sevenP = (TextView) findViewById(R.id.twentysevenP);
        twenty_eightP = (TextView) findViewById(R.id.twentyeightP);
        twenty_nineP = (TextView) findViewById(R.id.twentynineP);
        thirtyP = (TextView) findViewById(R.id.thirtyP);
        thiety_oneP = (TextView) findViewById(R.id.thirtyoneP);


        ll1=(LinearLayout)findViewById(R.id.ll1);
        ll2=(LinearLayout)findViewById(R.id.ll2);
        ll3=(LinearLayout)findViewById(R.id.ll3);
        ll4=(LinearLayout)findViewById(R.id.ll4);
        ll5=(LinearLayout)findViewById(R.id.ll5);
        ll6=(LinearLayout)findViewById(R.id.ll6);
        ll7=(LinearLayout)findViewById(R.id.ll7);
        ll8=(LinearLayout)findViewById(R.id.ll8);
        ll9=(LinearLayout)findViewById(R.id.ll9);
        ll10=(LinearLayout)findViewById(R.id.ll10);
        ll11=(LinearLayout)findViewById(R.id.ll11);
        ll12=(LinearLayout)findViewById(R.id.ll12);
        ll13=(LinearLayout)findViewById(R.id.ll13);
        ll14=(LinearLayout)findViewById(R.id.ll14);
        ll15=(LinearLayout)findViewById(R.id.ll15);
        ll16=(LinearLayout)findViewById(R.id.ll16);
        ll17=(LinearLayout)findViewById(R.id.ll17);
        ll18=(LinearLayout)findViewById(R.id.ll18);
        ll19=(LinearLayout)findViewById(R.id.ll19);
        ll20=(LinearLayout)findViewById(R.id.ll20);
        ll21=(LinearLayout)findViewById(R.id.ll21);
        ll22=(LinearLayout)findViewById(R.id.ll22);
        ll23=(LinearLayout)findViewById(R.id.ll23);
        ll24=(LinearLayout)findViewById(R.id.ll24);
        ll25=(LinearLayout)findViewById(R.id.ll25);
        ll26=(LinearLayout)findViewById(R.id.ll26);
        ll27=(LinearLayout)findViewById(R.id.ll27);
        ll28=(LinearLayout)findViewById(R.id.ll28);
        ll29=(LinearLayout)findViewById(R.id.ll29);
        ll30=(LinearLayout)findViewById(R.id.ll30);
        ll31=(LinearLayout)findViewById(R.id.ll31);


        morningQun = (TextView) findViewById(R.id.morningQ);
        eveningQun = (TextView) findViewById(R.id.eveningQ);
    }

//  //  public void setToDefault(){
//
//        one.setText("0 Lt");
//        two.setText("0 Lt");
//        three.setText("0 Lt");
//        four.setText("0 Lt");
//        five.setText("0 Lt");
//        six.setText("0 Lt");
//        seven.setText("0 Lt");
//        eight.setText("0 Lt");
//        nine.setText("0 Lt");
//        ten.setText("0 Lt");
//        eleven.setText("0 Lt");
//        twelve.setText("0 Lt");
//        thirteen.setText("0 Lt");
//        fourteen.setText("0 Lt");
//        fifteen.setText("0 Lt");
//        sixteen.setText("0 Lt");
//        seventeen.setText("0 Lt");
//        eightteen.setText("0 Lt");
//        nineteen.setText("0 Lt");
//        twenty.setText("0 Lt");
//        twenty_one.setText("0 Lt");
//        twenty_two.setText("0 Lt");
//        twenty_three.setText("0 Lt");
//        twenty_four.setText("0 Lt");
//        twenty_five.setText("0 Lt");
//        twenty_six.setText("0 Lt");
//        twenty_seven.setText("0 Lt");
//        twenty_eight.setText("0 Lt");
//        twenty_nine.setText("0 Lt");
//        thirty.setText("0 Lt");
//        thiety_one.setText("0 Lt");
//
//        oneP.setText("0 Lt");
//        twoP.setText("0 Lt");
//        threeP.setText("0 Lt");
//        fourP.setText("0 Lt");
//        fiveP.setText("0 Lt");
//        sixP.setText("0 Lt");
//        sevenP.setText("0 Lt");
//        eightP.setText("0 Lt");
//        nineP.setText("0 Lt");
//        tenP.setText("0 Lt");
//        elevenP.setText("0 Lt");
//        twelveP.setText("0 Lt");
//        thirteenP.setText("0 Lt");
//        fourteenP.setText("0 Lt");
//        fifteenP.setText("0 Lt");
//        sixteenP.setText("0 Lt");
//        seventeenP.setText("0 Lt");
//        eightteenP.setText("0 Lt");
//        nineteenP.setText("0 Lt");
//        twentyP.setText("0 Lt");
//        twenty_oneP.setText("0 Lt");
//        twenty_twoP.setText("0 Lt");
//        twenty_threeP.setText("0 Lt");
//        twenty_fourP.setText("0 Lt");
//        twenty_fiveP.setText("0 Lt");
//        twenty_sixP.setText("0 Lt");
//        twenty_sevenP.setText("0 Lt");
//        twenty_eightP.setText("0 Lt");
//        twenty_nineP.setText("0 Lt");
//        thirtyP.setText("0 Lt");
//        thiety_oneP.setText("0 Lt");
//
//    }


}

