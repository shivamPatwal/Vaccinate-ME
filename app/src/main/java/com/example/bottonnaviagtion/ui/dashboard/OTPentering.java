package com.example.bottonnaviagtion.ui.dashboard;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottonnaviagtion.R;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPentering extends Fragment {
    public Button getApiBtn, postApiBtn, getbin;
    private EditText resultTextView;
    ProgressDialog progressDialog;
    public boolean correct = true;
    public Dialog dialog;
    TextView information;
    String refno;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    String hashValue;
    RequestQueue requestQueue;
    static String Response;
    String token;
    String url;
    View root;
    long id;
    String ur;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.enteringotp, container, false);

        dialog = new Dialog(getActivity());
        information = root.findViewById(R.id.textView13);
        resultTextView = (EditText) root.findViewById(R.id.editTextPhone2);
        postApiBtn = (Button) root.findViewById(R.id.button2);
        getApiBtn = (Button) root.findViewById(R.id.button3);
        postApiBtn = (Button) root.findViewById(R.id.button2);
        getbin = (Button) root.findViewById(R.id.button4);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        getApiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                String edit = resultTextView.getText().toString();
                information.setVisibility(View.INVISIBLE);
                hashValue = Sha256.getSha(edit).toLowerCase();

                getData();
            }
        });
        postApiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                information.setVisibility(View.INVISIBLE);
                postData();


            }
        });
        getbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refno = resultTextView.getText().toString();
                if (refno.length() != 14) {
                    correct = false;
                    openDialog();


                    getbin.setVisibility(View.VISIBLE);

                } else {
                    getrefer();
                }
            }
        });
        return root;
    }

    private void getrefer() {
        String url = "https://cdn-api.co-vin.in/api/v2/registration/certificate/public/download";


        Uri baseUrl = Uri.parse(url);
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendQueryParameter("beneficiary_reference_id", refno);

        ur = uriBuilder.toString();
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        } else {

            DownloadBooks(ur, refno);

        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DownloadBooks(ur, refno);

                } else {

                    Toast.makeText(getContext(), "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    public void DownloadBooks(String url, String title) {

        progressDialog = new ProgressDialog(getActivity(), R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setMessage("Downloading...Please wait.");
        progressDialog.show();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //request.setDescription("goo");
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + ".pdf");

        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Log.i("inn", downloadManager.toString());
        request.setMimeType("application/pdf");
        request.addRequestHeader("Authorization", "Bearer " + token);
        request.allowScanningByMediaScanner();

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        id = downloadManager.enqueue(request);
        getActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long did = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == did) {
                progressDialog.cancel();

                Toast.makeText(getContext(), "Download Completed", Toast.LENGTH_LONG).show();
            }

        }

        ;


    };


    public void openDialog() {
        if (correct == true) {
            dialog.setContentView(R.layout.dialog);
        } else {
            dialog.setContentView(R.layout.dialogwrong);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView = dialog.findViewById(R.id.imageView);
        Button button = dialog.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (correct == true) {
                    getApiBtn.setVisibility(View.VISIBLE);
                    postApiBtn.setVisibility(View.INVISIBLE);
                    getbin.setVisibility(View.INVISIBLE);
                    resultTextView.setText("");
                    resultTextView.setHint("ENTER OTP");
                    dialog.dismiss();
                } else {
                    correct = true;
                    dialog.dismiss();
                }


            }
        });
        dialog.show();
    }

    // Post Request For JSONObject
    public void postData() {
        if (resultTextView.getText().toString().length() != 10) {
            correct = false;
            openDialog();
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            JSONObject object = new JSONObject();
            try {
                object.put("mobile", resultTextView.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            url = "https://cdn-api.co-vin.in/api/v2/auth/public/generateOTP";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("mAIn", response.toString());

                            Response = response.toString().substring(10, 46);
                            openDialog();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    correct = false;
                    openDialog();

                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JSONObject object = new JSONObject();
        try {
            object.put("otp", hashValue);
            object.put("txnId", Response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url = "https://cdn-api.co-vin.in/api/v2/auth/public/confirmOTP";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respon) {
                        Log.i("info", respon.toString());
                        token = respon.toString().substring(10, respon.toString().length() - 2);

                        Log.i("info", token);
                        getApiBtn.setVisibility(View.INVISIBLE);
                        postApiBtn.setVisibility(View.INVISIBLE);
                        getbin.setVisibility(View.VISIBLE);
                        resultTextView.setText("");
                        information.setVisibility(View.VISIBLE);
                        setText();

                        resultTextView.setHint("ref no");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                correct = false;
                openDialog();
                getApiBtn.setVisibility(View.VISIBLE);
                postApiBtn.setVisibility(View.INVISIBLE);
                getbin.setVisibility(View.INVISIBLE);
                resultTextView.setHint("PHONE NO");
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    void setText() {
        information.setText("\n\n" + " ● " + "You must have already received at least one dose of Vaccine.\n\n" +
                " ● " + "The Beneficiary Reference ID is provided at the time  of registering for the Vaccine.\n\n" +
                " ● " + "You must have the 13/14 Digit Beneficiary Reference  ID with you.\n\n" +
                " ● " + "You need to be registered on CoWIN platform to  download the certificate." +
                " If the mobile number used  to register on CoWIN platform is different from this  number " +
                "then you will need to validate it. Please ensure,  that phone is available with you.\n\n");

    }


}