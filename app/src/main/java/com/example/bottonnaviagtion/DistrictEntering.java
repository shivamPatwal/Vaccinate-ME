package com.example.bottonnaviagtion;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.bottonnaviagtion.ui.home.MySingleton;
import com.example.bottonnaviagtion.ui.home.vaccine;
import com.example.bottonnaviagtion.ui.home.vaccineAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DistrictEntering extends Fragment {

    AutoCompleteTextView searchView, searchView1;
    vaccineAdapter adapter;
    String date;
    ArrayList<String> a = new ArrayList<String>();
    ArrayList<String> b = new ArrayList<String>();
    HashMap<String, Integer> c = new HashMap<String, Integer>();
    String state, district;
    ArrayList<vaccine> arrayList = new ArrayList<vaccine>();
    private ProgressBar mProgress;
    int stateId, districtId;
    ImageView noInternet;
    ImageView noData, searchicon;
    Button Refresh;
    String day, month, year;
    RecyclerView recyclerView;
    DatePickerDialog datePickerDialog;
    View view;

    public DistrictEntering() {
    }

    public static DistrictEntering newInstance(String param1, String param2) {
        DistrictEntering fragment = new DistrictEntering();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fetchState("");
        view = inflater.inflate(R.layout.recycle_district, container, false);
        searchView = view.findViewById(R.id.state);
        searchView1 = view.findViewById(R.id.district);
        mProgress = (ProgressBar) view.findViewById(R.id.loading_indicator);


        noInternet = (ImageView) view.findViewById(R.id.imageView9);
        noData = (ImageView) view.findViewById(R.id.imageView10);


        Refresh = (Button) view.findViewById(R.id.button5);
        searchView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.drop_down_item, a));

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.setVisibility(View.INVISIBLE);
                noInternet.setVisibility(View.INVISIBLE);
                Refresh.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.INVISIBLE);
                arrayList.clear();
                c.clear();
                b.clear();


                searchView1.setText("");
                state = (String) parent.getItemAtPosition(position);
                stateId = a.indexOf(state);
                if (stateId < 9) {
                    stateId = stateId + 1;
                }
                if (state.equals("Daman and Diu")) {
                    stateId = 37;
                }
                fetchDistrict();

            }
        });

        searchView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.setVisibility(View.INVISIBLE);
                noInternet.setVisibility(View.INVISIBLE);
                Refresh.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.INVISIBLE);
                arrayList.clear();

                district = (String) parent.getItemAtPosition(position);

                districtId = c.get(district);
                function();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    private void function() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
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

    private void fetchState(String selectState) {


        String url = "https://cdn-api.co-vin.in/api/v2/admin/location/states";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("states");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject vaccineJsonObj = jsonArray.getJSONObject(i);

                        a.add(vaccineJsonObj.getString("state_name"));
                        Log.i("info", vaccineJsonObj.getString("state_name"));


                    }


                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Failed to get data..", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.setVisibility(View.GONE);
                noInternet.setVisibility(View.VISIBLE);
                Refresh.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);

                fetchState("");
                Refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noInternet.setVisibility(View.INVISIBLE);
                        Refresh.setVisibility(View.INVISIBLE);

                        fetchState("");
                    }
                });
            }
        });

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }


    void fetchDistrict() {
        String ul = "https://cdn-api.co-vin.in/api/v2/admin/location/districts" + "/" + stateId;
        Log.i("info", ul);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ul, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("districts");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject vaccineJsonObj = jsonArray.getJSONObject(i);

                        c.put(vaccineJsonObj.getString("district_name"), vaccineJsonObj.getInt("district_id"));
                        Log.i("info", vaccineJsonObj.getString("district_name"));
                        Log.i("info", vaccineJsonObj.getString("district_id"));

                    }
                    b.addAll(c.keySet());
                    searchView1.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.drop_down_item, b));

                } catch (JSONException e) {


                    Toast.makeText(getActivity(), "Failed to get data..", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgress.setVisibility(View.GONE);
                noInternet.setVisibility(View.VISIBLE);
                Refresh.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                fetchDistrict();

                Refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noInternet.setVisibility(View.INVISIBLE);
                        Refresh.setVisibility(View.INVISIBLE);
                        // Toast.makeText(getActivity(),"SELECT DISTRICT",Toast.LENGTH_SHORT).show();
                        fetchDistrict();
                    }
                });
            }
        });

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
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
        String s = day + "-" + month + "-" + year;
        Log.i("info", s);
        mProgress.setVisibility(View.VISIBLE);

        fetchDistrictWise(s);
    }

    private void fetchDistrictWise(String s) {


        recyclerView.setVisibility(View.VISIBLE);

        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict";

        Uri baseUrl = Uri.parse(url);
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendQueryParameter("district_id", Integer.toString(districtId));
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
                    Toast.makeText(getActivity(), "Failed to get data..", Toast.LENGTH_LONG).show();
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
                        fetchDistrictWise(s);
                    }
                });
                Toast.makeText(getActivity(), "Failed:Check Internet", Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
}