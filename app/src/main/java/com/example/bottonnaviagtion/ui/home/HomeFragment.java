package com.example.bottonnaviagtion.ui.home;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bottonnaviagtion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    View view;
    String date;
    ImageView noInternet;
    ImageView noData, searchicon;
    Button Refresh;
    String s;
    private ProgressBar mProgress;
    RecyclerView recyclerView;
    ArrayList<vaccine> arrayList = new ArrayList<vaccine>();
    EditText editText;
    vaccineAdapter adapter;
    DatePickerDialog datePickerDialog;
    String day, month, year;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragmentrecycle, container, false);
        editText = (EditText) view.findViewById(R.id.search);
        mProgress = (ProgressBar) view.findViewById(R.id.loading_indicator);

        noInternet = (ImageView) view.findViewById(R.id.imageView9);
        noData = (ImageView) view.findViewById(R.id.imageView10);
        searchicon = (ImageView) view.findViewById(R.id.searchicon);


        Refresh = (Button) view.findViewById(R.id.button5);
        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                noInternet.setVisibility(View.INVISIBLE);
                Refresh.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.INVISIBLE);
                arrayList.clear();
                function();
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            function();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                noInternet.setVisibility(View.INVISIBLE);
                Refresh.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.INVISIBLE);
                arrayList.clear();

            }
        });


        return view;
    }

    private void function() {
        String editTextValue = editText.getText().toString();

        editText.setText(editTextValue);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        setDate(dayOfMonth, monthOfYear, year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }


    private void fetchData(String s) {


        recyclerView.setVisibility(View.VISIBLE);

        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin";
        String pincode = editText.getText().toString();
        //date=getDay()+"-"+getMonth()+"-"+getYear();
        // Log.i("info",date);
        if (pincode.length() != 6) {
            mProgress.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getActivity(), "Wrong Pincode", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else {

            Uri baseUrl = Uri.parse(url);
            Uri.Builder uriBuilder = baseUrl.buildUpon();
            uriBuilder.appendQueryParameter("pincode", pincode);
            uriBuilder.appendQueryParameter("date", s);
            String ur = uriBuilder.toString();
            Log.i("info", ur);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ur, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray jsonArray = response.getJSONArray("sessions");
                        if (jsonArray.length() == 0) {

                            noData.setVisibility(View.VISIBLE);

                            Toast toast = Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject vaccineJsonObj = jsonArray.getJSONObject(i);

                                vaccine vacc = new vaccine(
                                        vaccineJsonObj.getString("name"),
                                        vaccineJsonObj.getString("address"),
                                        vaccineJsonObj.getString("fee_type"),
                                        vaccineJsonObj.getInt("available_capacity_dose1"),
                                        vaccineJsonObj.getInt("available_capacity_dose2"),
                                        vaccineJsonObj.getInt("min_age_limit"),
                                        vaccineJsonObj.getString("vaccine")
                                );
                                arrayList.add(vacc);
                            }
                        }

                    } catch (JSONException e) {

                        Toast.makeText(getActivity(), "Failed to get data..", Toast.LENGTH_SHORT).show();
                    }
                    mProgress.setVisibility(View.GONE);
                    adapter = new vaccineAdapter(getActivity(), arrayList);
                    recyclerView.setAdapter(adapter);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.setVisibility(View.GONE);
                    noInternet.setVisibility(View.VISIBLE);
                    Refresh.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);

                    Refresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noInternet.setVisibility(View.INVISIBLE);
                            Refresh.setVisibility(View.INVISIBLE);
                            mProgress.setVisibility(View.VISIBLE);
                            fetchData(s);
                        }
                    });

                    Toast.makeText(getActivity(), "Failed:Check Internet", Toast.LENGTH_SHORT).show();
                }
            });

            MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
        }
    }

    private void setDate(int dayOfMonth, int monthOfYear, int yea) {
        if (dayOfMonth < 10) {
            day = "0" + Integer.toString(dayOfMonth);
        } else {
            day = Integer.toString(dayOfMonth);
        }
        if (monthOfYear < 10) {
            month = "0" + Integer.toString(monthOfYear + 1);
        } else {
            month = Integer.toString(monthOfYear + 1);
        }
        year = Integer.toString(yea);
        s = day + "-" + month + "-" + year;
        Log.i("info", s);
        mProgress.setVisibility(View.VISIBLE);

        fetchData(s);
    }

}
