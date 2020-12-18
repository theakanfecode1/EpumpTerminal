package com.epump.epumpterminal.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epump.epumpterminal.R;

import com.epump.epumpterminal.models.StationDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import ng.com.epump.epumpterminal.GetAddressIntentService;


public class MapStationFragment extends Fragment {
    private static final int LOCATION_REQUEST = 1337;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private LocationAddressResultReceiver mAddressResultReceiver;
    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback mLocationCallback;
    private Location mLocation;
    TextView txtName, txtAddress, txtLat, txtLong, loglab, latlab, addresslab, successfeedback;
    CardView mCardViewlab;
    Button btnSubmit, gobackbutton;
    String branchId;
    Context mContext;
    Activity mActivity;
    String url = "https://api.epump.com.ng/Branch/MapStation";
    String unMapUrl = "https://api.epump.com.ng/Branch/UnmapStation";
    private int mSec = 0;
    private CountDownTimer mCountDownTimer;
    private boolean mBtnClicked;
    private long mCurrentTime;
    private boolean mShowViews;
    private RequestQueue mRequestQueue;
    private String mId;
    private static final String TAG = "ConfirmationPage";
    private AlertDialog mAlertDialogDelete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_station, container, false);
        Toolbar toolbar = view.findViewById(R.id.confirmtoolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();

            }
        });
        mContext = getContext();
        loglab = view.findViewById(R.id.longitudelabel);
        latlab = view.findViewById(R.id.latitudelabel);
        addresslab = view.findViewById(R.id.addresslabel);
        mCardViewlab = view.findViewById(R.id.cardViewconfirm);
        txtName = view.findViewById(R.id.namesubmit);
        txtAddress = view.findViewById(R.id.addresssubmit);
        txtLat = view.findViewById(R.id.latitude);
        txtLong = view.findViewById(R.id.longitude);
        btnSubmit = view.findViewById(R.id.submit);
        successfeedback = view.findViewById(R.id.successreport);
        gobackbutton = view.findViewById(R.id.gobackbutton);

        StationDetails stationDetails = MapStationFragmentArgs.fromBundle(getArguments()).getStationDetails();
        mId = MapStationFragmentArgs.fromBundle(getArguments()).getId();
        if (stationDetails != null) {
            branchId = stationDetails.getId();
            txtName.setText(stationDetails.getName());
            txtAddress.setText(stationDetails.getStreet());
            if (stationDetails.getLatitude() != null && stationDetails.getLongitude() > 0) {
                txtLat.setText(String.valueOf(stationDetails.getLatitude()));
            }
            if (stationDetails.getLongitude() != null && stationDetails.getLongitude() > 0) {
                txtLong.setText(String.valueOf(stationDetails.getLongitude()));
            }
            btnSubmit.setEnabled(true);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.setEnabled(false);
                startTimer(60000);

            }
        });
        gobackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();

            }
        });
        if (savedInstanceState != null) {
            boolean state = savedInstanceState.getBoolean("CLICKED");
            int CURRENTSEC = (int) savedInstanceState.getLong("CURRENT");
            boolean stateView = savedInstanceState.getBoolean("VIEWS");
            if (state) {
                startTimer(CURRENTSEC);
                btnSubmit.setEnabled(false);
            }
            if (stateView) {
                txtLat.setVisibility(View.INVISIBLE);
                txtLong.setVisibility(View.INVISIBLE);
                txtAddress.setVisibility(View.INVISIBLE);
                btnSubmit.setVisibility(View.INVISIBLE);
                loglab.setVisibility(View.INVISIBLE);
                latlab.setVisibility(View.INVISIBLE);
                addresslab.setVisibility(View.INVISIBLE);
                mCardViewlab.setVisibility(View.INVISIBLE);
                successfeedback.setVisibility(View.VISIBLE);
                gobackbutton.setVisibility(View.VISIBLE);
                mShowViews = true;
            }


        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        if (mBtnClicked) {
            outState.putLong("CURRENT", mCurrentTime);
            outState.putBoolean("CLICKED", true);

        }

        outState.putBoolean("VIEWS", mShowViews);

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        super.onDestroy();
    }

    private void startTimer(int time) {

        mBtnClicked = true;
        if (mCountDownTimer != null) {
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                mCurrentTime = l;
                btnSubmit.setText("Calibrating.." + (l / 1000));
            }

            @Override
            public void onFinish() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("latitude", Double.parseDouble(txtLat.getText().toString()));
                    jsonObject.put("longitude", Double.parseDouble(txtLong.getText().toString()));
                    jsonObject.put("branchId", branchId);
                    mRequestQueue = Volley.newRequestQueue(mContext);
                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            txtLat.setVisibility(View.INVISIBLE);
                            txtLong.setVisibility(View.INVISIBLE);
                            txtAddress.setVisibility(View.INVISIBLE);
                            btnSubmit.setVisibility(View.INVISIBLE);
                            loglab.setVisibility(View.INVISIBLE);
                            latlab.setVisibility(View.INVISIBLE);
                            addresslab.setVisibility(View.INVISIBLE);
                            mCardViewlab.setVisibility(View.INVISIBLE);
                            successfeedback.setVisibility(View.VISIBLE);
                            gobackbutton.setVisibility(View.VISIBLE);
                            mShowViews = true;
                            mBtnClicked = false;
                            Log.d("ConfirmationPage", "onResponse: Sent");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NetworkError) {
                                Toast.makeText(getContext(), "NetworkError", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getContext(), "Authorized User", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getContext(), "Parse Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NoConnectionError) {
                                Toast.makeText(getContext(), "No Connection Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "An unknown error occurred", Toast.LENGTH_SHORT).show();
                            }


                        }

                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            String bearer = "Bearer ".concat(SplashScreenFragment.token);
                            Map<String, String> headersSys = super.getHeaders();
                            Map<String, String> headers = new HashMap<String, String>();
                            headersSys.remove("Authorization");
                            headers.put("Authorization", bearer);
                            headers.putAll(headersSys);
                            return headers;
                        }


                        @Override
                        protected Response parseNetworkResponse(NetworkResponse response) {
                            return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };
                    mRequestQueue.add(stringRequest);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }


    private class LocationAddressResultReceiver extends ResultReceiver {

        public LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 0) {
                getAddress();
            }
            Address currentAddress = resultData.getParcelable("address_result");
            if (currentAddress != null) {
                txtLat.setText(String.valueOf(currentAddress.getLatitude()));
                txtLong.setText(String.valueOf(currentAddress.getLongitude()));
            }
        }
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == getContext().checkSelfPermission(permission));
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            }
        }
    }

    private void getMyLocation() {
        mAddressResultReceiver = new LocationAddressResultReceiver(new Handler());
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                mLocation = locationResult.getLocations().get(0);
                getAddress();
            }
        };

        startLocationUpdates();
    }

    private void getAddress() {
        Intent intent = new Intent(mContext, GetAddressIntentService.class);
        intent.putExtra("add_receiver", mAddressResultReceiver);
        intent.putExtra("add_location", mLocation);
        mContext.startService(intent);
    }

    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            mLocationProviderClient.requestLocationUpdates(locationRequest,
                    mLocationCallback,
                    null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            getMyLocation();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
                ;
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        mLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.confirmationmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.deletestation:
                if (MappableStationFragment.result) {
                    showUnmappedConfirmation();
                } else {
                    Toast.makeText(getContext(), "Go to mapped stations to unmap station.", Toast.LENGTH_SHORT).show();
                }

                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void showUnmappedConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = this.getLayoutInflater().inflate(R.layout.ummaped_dialog, null);
        builder.setView(view);
        Button No = view.findViewById(R.id.no_unmap);
        Button yes = view.findViewById(R.id.yesunmap);

        final AlertDialog alertDialog = builder.create();
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                showDeleting();
                umMapStation();


            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

    }

    private void umMapStation() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", mId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String body = jsonObject.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, unMapUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: Sucesssfully unmapped");
                mAlertDialogDelete.dismiss();
                Navigation.findNavController(getView()).popBackStack();
                Toast.makeText(getContext(), "Successfully unmapped,swipe to refresh.", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAlertDialogDelete.dismiss();
                if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), "NetworkError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getContext(), "Authorized User", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getContext(), "Parse Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(getContext(), "No Connection Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "An unknown error occurred", Toast.LENGTH_SHORT).show();
                }

            }

        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return body == null ? null : body.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String bearer = "Bearer ".concat(SplashScreenFragment.token);
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(10000, 2, 1);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    public void showDeleting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.deletingdata, null);
        builder.setView(view);
        mAlertDialogDelete = builder.create();
        mAlertDialogDelete.setCancelable(false);
        mAlertDialogDelete.setCanceledOnTouchOutside(false);
        mAlertDialogDelete.show();

    }


}