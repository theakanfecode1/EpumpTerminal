package com.epump.epumpterminal.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epump.epumpterminal.models.StationDetails;
import com.epump.epumpterminal.ui.SplashScreenFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataViewModel extends ViewModel {
    private final String TAG = DataViewModel.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private List<StationDetails> mStations;
    MutableLiveData<List<StationDetails>> liveListStation = new MutableLiveData<>();
    MutableLiveData<String> numOfUser = new MutableLiveData<>();
    MutableLiveData<String> valueDiscount = new MutableLiveData<>();
    MutableLiveData<String> totalTransaction = new MutableLiveData<>();
    MutableLiveData<String> valueTransaction = new MutableLiveData<>();
    MutableLiveData<String> startDate = new MutableLiveData<>();
    MutableLiveData<String> endDate = new MutableLiveData<>();
    private String mYear;
    private String mMonth;
    private String mDay;
    private String mDate;
    private String mEndDate;
    private String totalNumberOfUsersUrl = "https://api.epump.com.ng/RemisDash/GetTotalUsers?";
    private String totalDiscountValueUrl = "https://api.epump.com.ng/RemisDash/GetTotalDiscountValue?";
    private String totalTransactionUrl = "https://api.epump.com.ng/RemisDash/GetTotalTransactionCount?";
    private String totalTransactionValueUrl = "https://api.epump.com.ng/RemisDash/GetTotalTransactionValue?";
    private Context mContext;

    public MutableLiveData<String> getStartDate() {
        return startDate;
    }

    public MutableLiveData<String> getEndDate() {
        return endDate;
    }

    public LiveData<String> getNumOfUser() {
        return numOfUser;
    }

    public LiveData<String> getValueDiscount() {
        return valueDiscount;
    }

    public LiveData<String> getTotalTransaction() {
        return totalTransaction;
    }

    public LiveData<String> getValueTransaction() {
        return valueTransaction;
    }


    public void setContext(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public LiveData<List<StationDetails>> getLiveList() {
        return liveListStation;
    }

    public void getData(String uri) {
        Log.d(TAG, "getData(): called" + uri);
        try {
            mStringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    mStations = gson.fromJson(response, new TypeToken<List<StationDetails>>() {
                    }.getType());
                    Collections.sort(mStations, new Comparator<StationDetails>() {
                        @Override
                        public int compare(StationDetails stationDetails, StationDetails t1) {
                            return stationDetails.getName().compareTo(t1.getName());
                        }
                    });

                    liveListStation.setValue(mStations);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        RetryPolicy retryPolicy = new DefaultRetryPolicy(10000, 2, 1);
        mStringRequest.setRetryPolicy(retryPolicy);
        mRequestQueue.add(mStringRequest);
    }

    public void getTotalUsers(String startDate, String endDate) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String total = (totalNumberOfUsersUrl + "startDate=" + startDate + "&endDate=" + endDate);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, total, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "onResponse: " + response);
                numOfUser.setValue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(10000, 2, 1);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    public void getTotalDiscount(String startDate, String endDate) {
        final String totalDiscount = (totalDiscountValueUrl + "startDate=" + startDate + "&endDate=" + endDate);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, totalDiscount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "onResponse: " + response);
                valueDiscount.setValue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(10000, 2, 1);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    public void getTotalTransactionCount(String startDate, String endDate) {
        String totalTrans = (totalTransactionUrl + "startDate=" + startDate + "&endDate=" + endDate);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, totalTrans, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "onResponse: " + response);
                totalTransaction.setValue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(10000, 2, 1);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    public void getTotalTransactionValue(String startDate, String endDate) {
        String totalValue = (totalTransactionValueUrl + "startDate=" + startDate + "&endDate=" + endDate);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, totalValue, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "onResponse: " + response);
                valueTransaction.setValue(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                valueTransaction.setValue("");
                if (error instanceof NetworkError) {
                    Toast.makeText(mContext, "NetworkError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(mContext, "Unauthorized User", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(mContext, "Parse Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext, "No Connection Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "An unknown error occurred", Toast.LENGTH_SHORT).show();
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

        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(10000, 2, 1);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }


}
