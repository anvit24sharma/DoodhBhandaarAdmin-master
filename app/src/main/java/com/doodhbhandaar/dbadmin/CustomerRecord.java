package com.doodhbhandaar.dbadmin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomerRecord extends AppCompatActivity {

    DatabaseReference customerdataReference, customersReference;
    Spinner spinner;
    Button ifPaidBtn;
    String customer_name;
    String todaysDate;

    String ModeOfPayment;
    TextView oneP, twoP, threeP, fourP, fiveP, sixP, sevenP, eightP, nineP, tenP, elevenP, twelveP, thirteenP, fourteenP, fifteenP, sixteenP, seventeenP, eightteenP, nineteenP, twentyP, twenty_oneP, twenty_twoP, twenty_threeP, twenty_fourP, twenty_fiveP, twenty_sixP, twenty_sevenP, twenty_eightP, twenty_nineP, thirtyP, thiety_oneP;
    TextView one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eightteen, nineteen, twenty, twenty_one, twenty_two, twenty_three, twenty_four, twenty_five, twenty_six, twenty_seven, twenty_eight, twenty_nine, thirty, thiety_one;
    LinearLayout ll1, ll2, ll3, ll4, ll5, ll6, ll7, ll8, ll9, ll10, ll11, ll12, ll13, ll14, ll15, ll16, ll17, ll18, ll19, ll20, ll21, ll22, ll23, ll24, ll25, ll26, ll27, ll28, ll29, ll30, ll31;
    TextView total_litres, total_cost_due, total_cost_paid, total_cost_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_record);

        customer_name = getIntent().getStringExtra("name");

        setTitle(customer_name);

        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
        todaysDate = mdformat.format(Calendar.getInstance().getTime());
        customersReference = FirebaseDatabaseReference.getDatabaseInstance().getReference("CUSTOMERS");
        customerdataReference = FirebaseDatabaseReference.getDatabaseInstance().getReference("CUSTMERSDATA");

        total_litres = (TextView) findViewById(R.id.total_litres);
        total_cost_due = (TextView) findViewById(R.id.total_cost_due);
        total_cost_paid = (TextView) findViewById(R.id.total_cost_paid);
        total_cost_rate = (TextView) findViewById(R.id.textcost);
        ifPaidBtn = (Button) findViewById(R.id.PaidBtn);

        setView();
        OnClick();

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

                customersReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // final double[] total_quantity = {0.0};

                        for (final DataSnapshot d : dataSnapshot.getChildren()) {
//                    Log.i("c00","enter");

                            final CustomerData customerData = d.getValue(CustomerData.class);
                            Log.i("name", customer_name + " " + customerData.customerName);

                            if (customerData.customerName.equals(customer_name)) {
                                customerdataReference.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final double[] total_quantitypaid = {0.0};
                                        final double[] total_quantitydue = {0.0};
                                        int c = 0;
                                        double TotalCostPaid=0.0;
                                        for (DataSnapshot cd : dataSnapshot.getChildren()) {
                                            Log.i("id", cd.getKey() + " " + customerData.pk);
                                            if (cd.getKey().equals(customerData.pk)) {
                                                c = 1;
                                                for (DataSnapshot cd_year : cd.getChildren()) {
                                                    Log.i("year", cd_year.getKey() + " " + Calendar.getInstance().get(Calendar.YEAR) + "" + (spinner.getSelectedItemPosition() + 1));

                                                    if (cd_year.getKey().equals(Calendar.getInstance().get(Calendar.YEAR) + "" + (spinner.getSelectedItemPosition() + 1))) {


                                                        for (DataSnapshot cd_date : cd_year.getChildren()) {
                                                            if (cd_date.getKey().equals("TotalCostPaid")) {
                                                                for (DataSnapshot tCostpaid : cd_date.getChildren()) {
                                                                    final CostPaidReference costPaidReference = tCostpaid.getValue(CostPaidReference.class);
                                                                    TotalCostPaid += costPaidReference.CostPaid;
                                                                }
                                                            } else {
                                                                final CustomerDeliveriesReference customerDeliveriesReference =
                                                                        cd_date.getValue(CustomerDeliveriesReference.class);
                                                                if (customerDeliveriesReference.isPaid)
                                                                    total_quantitypaid[0] += Double.parseDouble(customerDeliveriesReference.quantity);
                                                                else
                                                                    total_quantitydue[0] += Double.parseDouble(customerDeliveriesReference.quantity);

                                                                Log.i("date", cd_date.getKey());
                                                                switch (cd_date.getKey()) {
                                                                    case "1":
                                                                        one.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            oneP.setText("PAID");
                                                                        else
                                                                            oneP.setText("DUE");
                                                                        break;

                                                                    case "2":
                                                                        two.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twoP.setText("PAID");
                                                                        else
                                                                            twoP.setText("DUE");
                                                                        break;
                                                                    case "3":
                                                                        three.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            threeP.setText("PAID");
                                                                        else
                                                                            threeP.setText("DUE");
                                                                        break;
                                                                    case "4":
                                                                        four.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            fourP.setText("PAID");
                                                                        else
                                                                            fourP.setText("DUE");
                                                                        break;
                                                                    case "5":
                                                                        five.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            fiveP.setText("PAID");
                                                                        else
                                                                            fiveP.setText("DUE");
                                                                        break;
                                                                    case "6":
                                                                        six.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            sixP.setText("PAID");
                                                                        else
                                                                            sixP.setText("DUE");
                                                                        break;
                                                                    case "7":
                                                                        seven.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            sevenP.setText("PAID");
                                                                        else
                                                                            sevenP.setText("DUE");
                                                                        break;
                                                                    case "8":
                                                                        eight.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            eightP.setText("PAID");
                                                                        else
                                                                            eightP.setText("DUE");
                                                                        break;
                                                                    case "9":
                                                                        nine.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            nineP.setText("PAID");
                                                                        else
                                                                            nineP.setText("DUE");
                                                                        break;
                                                                    case "10":
                                                                        ten.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            tenP.setText("PAID");
                                                                        else
                                                                            tenP.setText("DUE");
                                                                        break;
                                                                    case "11":
                                                                        eleven.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            elevenP.setText("PAID");
                                                                        else
                                                                            elevenP.setText("DUE");
                                                                        break;
                                                                    case "12":
                                                                        twelve.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twelveP.setText("PAID");
                                                                        else
                                                                            twelveP.setText("DUE");
                                                                        break;
                                                                    case "13":
                                                                        thirteen.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            thirteenP.setText("PAID");
                                                                        else
                                                                            thirteenP.setText("DUE");
                                                                        break;
                                                                    case "14":
                                                                        fourteen.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            fourteenP.setText("PAID");
                                                                        else
                                                                            fourteenP.setText("DUE");
                                                                        break;
                                                                    case "15":
                                                                        fifteen.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            fifteenP.setText("PAID");
                                                                        else
                                                                            fifteenP.setText("DUE");
                                                                        break;
                                                                    case "16":
                                                                        sixteen.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            sixteenP.setText("PAID");
                                                                        else
                                                                            sixteenP.setText("DUE");
                                                                        break;
                                                                    case "17":
                                                                        seventeen.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            seventeenP.setText("PAID");
                                                                        else
                                                                            seventeenP.setText("DUE");
                                                                        break;
                                                                    case "18":
                                                                        eightteen.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            eightteenP.setText("PAID");
                                                                        else
                                                                            eightteenP.setText("DUE");
                                                                        break;
                                                                    case "19":
                                                                        nineteen.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            nineteenP.setText("PAID");
                                                                        else
                                                                            nineteenP.setText("DUE");
                                                                        break;
                                                                    case "20":
                                                                        twenty.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twentyP.setText("PAID");
                                                                        else
                                                                            twentyP.setText("DUE");
                                                                        break;
                                                                    case "21":
                                                                        twenty_one.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twenty_oneP.setText("PAID");
                                                                        else
                                                                            twenty_oneP.setText("DUE");
                                                                        break;
                                                                    case "22":
                                                                        twenty_two.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twenty_twoP.setText("PAID");
                                                                        else
                                                                            twenty_twoP.setText("DUE");
                                                                        break;
                                                                    case "23":
                                                                        twenty_three.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twenty_threeP.setText("PAID");
                                                                        else
                                                                            twenty_threeP.setText("DUE");
                                                                        break;
                                                                    case "24":
                                                                        twenty_four.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twenty_fourP.setText("PAID");
                                                                        else
                                                                            twenty_fourP.setText("DUE");
                                                                        break;
                                                                    case "25":
                                                                        twenty_five.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twenty_fiveP.setText("PAID");
                                                                        else
                                                                            twenty_fiveP.setText("DUE");
                                                                        break;
                                                                    case "26":
                                                                        twenty_six.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twenty_sixP.setText("PAID");
                                                                        else
                                                                            twenty_sixP.setText("DUE");
                                                                        break;
                                                                    case "27":
                                                                        twenty_seven.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twenty_sevenP.setText("PAID");
                                                                        else
                                                                            twenty_sevenP.setText("DUE");
                                                                        break;
                                                                    case "28":
                                                                        twenty_eight.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twenty_eightP.setText("PAID");
                                                                        else
                                                                            twenty_eightP.setText("DUE");
                                                                        break;
                                                                    case "29":
                                                                        twenty_nine.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            twenty_nineP.setText("PAID");
                                                                        else
                                                                            twenty_nineP.setText("DUE");
                                                                        break;
                                                                    case "30":
                                                                        thirty.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            thirtyP.setText("PAID");
                                                                        else
                                                                            thirtyP.setText("DUE");
                                                                        break;
                                                                    case "31":
                                                                        thiety_one.setText(customerDeliveriesReference.quantity + " Lt");
                                                                        if (customerDeliveriesReference.isPaid)
                                                                            thiety_oneP.setText("PAID");
                                                                        else
                                                                            thiety_oneP.setText("DUE");
                                                                        break;

                                                                }


                                                            }
                                                        }
                                                            total_litres.setText(String.valueOf(total_quantitydue[0] + total_quantitypaid[0]));
                                                            total_cost_rate.setText("Total Cost Due : @" + customerData.Cost.toString());
                                                            total_cost_due.setText(String.valueOf((total_quantitydue[0] * Double.parseDouble(customerData.Cost)) - TotalCostPaid));
                                                            total_cost_paid.setText(String.valueOf((total_quantitypaid[0] * Double.parseDouble(customerData.Cost)) + TotalCostPaid));
                                                            break;
                                                        }
                                                        setToDefault();
                                                        total_cost_rate.setText("Total Cost Due : @" + customerData.Cost.toString());
                                                        total_litres.setText(String.valueOf(total_quantitydue[0] + total_quantitypaid[0]));
                                                    }
                                                    break;
                                                }


                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
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


    private void updateQuantity(final int date) {
        final double[] value = {0.0};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.update_quntity, null);
        builder.setView(dialogView);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radio);
        final RadioGroup radioGroupCB = dialogView.findViewById(R.id.radio_CB);
        Button minusButton = dialogView.findViewById(R.id.minus);
        Button plusButton = dialogView.findViewById(R.id.plus);
        final TextView textView = dialogView.findViewById(R.id.text);
        textView.setText(value[0] + "");
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value[0] <= 0)
                    return;
                value[0] -= .25;
                textView.setText(value[0] + "");
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value[0] += .25;
                textView.setText(value[0] + "");
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = radioGroup.getCheckedRadioButtonId();
                int id_CB = radioGroupCB.getCheckedRadioButtonId();
                Log.i("ok", "ok");
                if (id == R.id.paid && id_CB == R.id.Cow) {
                    updateValue(date, value[0], true, true);
                    Log.i("paid", "ok cow");
                } else if (id == R.id.paid && id_CB == R.id.Buffalo) {
                    updateValue(date, value[0], true, false);
                    Log.i("paid", "ok b");
                } else if (id == R.id.unpaid && id_CB == R.id.Cow) {
                    updateValue(date, value[0], false, true);
                    Log.i("unpaid", "ok cow");
                } else {
                    Log.i("unpaid", "ok b");
                    updateValue(date, value[0], false, false);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.create().show();
    }

    private void updateValue(final int date, final double value, final boolean ispaid, final boolean isCow) {
        Log.i("okay", "ok");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure milk quantity is " + value);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final CustomerDeliveriesReference customerDeliveriesReference = new CustomerDeliveriesReference();
                customerDeliveriesReference.quantity = value + "";
                customerDeliveriesReference.date = todaysDate + "";
                customerDeliveriesReference.isPaid = ispaid;
                customerDeliveriesReference.isCow = isCow;
                customersReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (final DataSnapshot d : dataSnapshot.getChildren()) {

                            final CustomerData customerData = d.getValue(CustomerData.class);
                            //Log.i("name", customer_name + " " + customerData.customerName);

                            if (customerData.customerName.equals(customer_name)) {
                                customerdataReference.child(customerData.pk)
                                        .child(Calendar.getInstance().get(Calendar.YEAR) + "" + (spinner.getSelectedItemPosition() + 1))
                                        .child("" + date).setValue(customerDeliveriesReference);
                                break;
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.create().show();

    }


    void updatePayValue(final Double costPaid, String MOP) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        final CostPaidReference costPaidReference = new CostPaidReference();
        costPaidReference.CostPaid= costPaid;
        costPaidReference.MoP = MOP;
        costPaidReference.DateTime = formatter.format(date);

        customersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot d : dataSnapshot.getChildren()) {
//                    Log.i("c00","enter");

                    final CustomerData customerData = d.getValue(CustomerData.class);
                    Log.i("name", customer_name + " " + customerData.customerName);

                    if (customerData.customerName.equals(customer_name)) {
                        customerdataReference.child(customerData.pk)
                                .child(Calendar.getInstance().get(Calendar.YEAR) + "" + (spinner.getSelectedItemPosition() + 1))
                                .child("TotalCostPaid").child(customerdataReference.push().getKey()).setValue(costPaidReference);
                        break;
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setView() {

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


        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        ll4 = (LinearLayout) findViewById(R.id.ll4);
        ll5 = (LinearLayout) findViewById(R.id.ll5);
        ll6 = (LinearLayout) findViewById(R.id.ll6);
        ll7 = (LinearLayout) findViewById(R.id.ll7);
        ll8 = (LinearLayout) findViewById(R.id.ll8);
        ll9 = (LinearLayout) findViewById(R.id.ll9);
        ll10 = (LinearLayout) findViewById(R.id.ll10);
        ll11 = (LinearLayout) findViewById(R.id.ll11);
        ll12 = (LinearLayout) findViewById(R.id.ll12);
        ll13 = (LinearLayout) findViewById(R.id.ll13);
        ll14 = (LinearLayout) findViewById(R.id.ll14);
        ll15 = (LinearLayout) findViewById(R.id.ll15);
        ll16 = (LinearLayout) findViewById(R.id.ll16);
        ll17 = (LinearLayout) findViewById(R.id.ll17);
        ll18 = (LinearLayout) findViewById(R.id.ll18);
        ll19 = (LinearLayout) findViewById(R.id.ll19);
        ll20 = (LinearLayout) findViewById(R.id.ll20);
        ll21 = (LinearLayout) findViewById(R.id.ll21);
        ll22 = (LinearLayout) findViewById(R.id.ll22);
        ll23 = (LinearLayout) findViewById(R.id.ll23);
        ll24 = (LinearLayout) findViewById(R.id.ll24);
        ll25 = (LinearLayout) findViewById(R.id.ll25);
        ll26 = (LinearLayout) findViewById(R.id.ll26);
        ll27 = (LinearLayout) findViewById(R.id.ll27);
        ll28 = (LinearLayout) findViewById(R.id.ll28);
        ll29 = (LinearLayout) findViewById(R.id.ll29);
        ll30 = (LinearLayout) findViewById(R.id.ll30);
        ll31 = (LinearLayout) findViewById(R.id.ll31);

    }

    public void setToDefault() {

        one.setText("0 Lt");
        two.setText("0 Lt");
        three.setText("0 Lt");
        four.setText("0 Lt");
        five.setText("0 Lt");
        six.setText("0 Lt");
        seven.setText("0 Lt");
        eight.setText("0 Lt");
        nine.setText("0 Lt");
        ten.setText("0 Lt");
        eleven.setText("0 Lt");
        twelve.setText("0 Lt");
        thirteen.setText("0 Lt");
        fourteen.setText("0 Lt");
        fifteen.setText("0 Lt");
        sixteen.setText("0 Lt");
        seventeen.setText("0 Lt");
        eightteen.setText("0 Lt");
        nineteen.setText("0 Lt");
        twenty.setText("0 Lt");
        twenty_one.setText("0 Lt");
        twenty_two.setText("0 Lt");
        twenty_three.setText("0 Lt");
        twenty_four.setText("0 Lt");
        twenty_five.setText("0 Lt");
        twenty_six.setText("0 Lt");
        twenty_seven.setText("0 Lt");
        twenty_eight.setText("0 Lt");
        twenty_nine.setText("0 Lt");
        thirty.setText("0 Lt");
        thiety_one.setText("0 Lt");

    }

    public void OnClick() {

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(1);
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(2);
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(3);
            }
        });
        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(4);
            }
        });
        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(5);
            }
        });
        ll6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(6);
            }
        });
        ll7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(7);
            }
        });
        ll8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(8);
            }
        });
        ll9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(9);
            }
        });
        ll10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(10);
            }
        });
        ll11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(11);
            }
        });
        ll12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(12);
            }
        });
        ll13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(13);
            }
        });
        ll14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(14);
            }
        });
        ll15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(15);
            }
        });
        ll16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(16);
            }
        });
        ll17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(17);
            }
        });
        ll18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(18);
            }
        });
        ll19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(19);
            }
        });
        ll20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(20);
            }
        });
        ll21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(21);
            }
        });
        ll22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(22);
            }
        });
        ll23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(23);
            }
        });
        ll24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(24);
            }
        });
        ll25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(25);
            }
        });
        ll26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(26);
            }
        });
        ll27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(27);
            }
        });
        ll28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(28);
            }
        });
        ll29.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateQuantity(29);
            }
        });
        ll30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(30);
            }
        });
        ll31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantity(31);
            }
        });

        ifPaidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaidDialog();
            }
        });

    }

    void PaidDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.paid_money_layout, null);
        builder.setView(dialogView);

        final RadioGroup rgMOP = dialogView.findViewById(R.id.MOP);
        final EditText totalAmount = dialogView.findViewById(R.id.AmountPaid);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = rgMOP.getCheckedRadioButtonId();
                // Log.i("ok,"ok");
                double CostPaid;
                CostPaid = Double.parseDouble(totalAmount.getText().toString());

                if (id == R.id.cashrb) {
                    ModeOfPayment = "Cash";
                } else if (id == R.id.paytmrb) {
                    ModeOfPayment = "Paytm";
                } else {
                    ModeOfPayment = "Phonepe";
                }
                updatePayValue(CostPaid, ModeOfPayment);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.create().show();


    }

}
