package com.epump.epumpterminal.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.epump.epumpterminal.R;
import com.epump.epumpterminal.models.StationDetails;
import com.epump.epumpterminal.ui.adapters.MappableStationAdapter;
import com.epump.epumpterminal.viewmodel.DataViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MappableStationFragment extends Fragment {
    private static final String TAG = "MappableStationFragment";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    public static boolean result;
    AppCompatActivity mActivity;
    private ArrayList<StationDetails> mStationDetailsArrayList = new ArrayList<>();
    private String url;
    private TextView mSummary;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private MappableStationAdapter mRecyclerAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<StationDetails> mStations;
    public  TextView mTextView;
    private DataViewModel mDataViewModel;
    private Observer<List<StationDetails>> mListObserver = new Observer<List<StationDetails>>() {
        @Override
        public void onChanged(List<StationDetails> stationDetails) {
            mStations = stationDetails;
            mRecyclerAdapter = new MappableStationAdapter(mContext, stationDetails,Navigation.findNavController(getView()));
            mSwipeRefreshLayout.setRefreshing(false);
            mRecyclerView.setAdapter(mRecyclerAdapter);
            String size = Integer.toString(stationDetails.size());
            if(result){
                mSummary.setText("Total number of mapped stations: "+size);
            }
            else{
                mSummary.setText("Total number of stations : "+size);
            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mappable_station,container,false);
        url = "https://api.epump.com.ng/Branch/GetStations?mapped=";
        mActivity = (AppCompatActivity) getActivity();
        mContext = getContext();
        mSummary = view.findViewById(R.id.summarydetails);
        Toolbar toolbar = view.findViewById(R.id.listofstationtoolbar);
        mActivity.setSupportActionBar(toolbar);
        final EditText search = view.findViewById(R.id.search);
        mActivity.getSupportActionBar().setTitle("");
//        Intent intent = getIntent();
        result = MappableStationFragmentArgs.fromBundle(getArguments()).getBooleanQueryForEndpoint();
        Log.d(TAG, "onCreateView: ");
        if(result){
            mTextView = view.findViewById(R.id.titletoolbar);
            mTextView.setText("List of mapped stations");
        }
        url += result;
        mRecyclerView = view.findViewById(R.id.mappable_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mDataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        mDataViewModel.setContext(mContext);
        mDataViewModel.getLiveList().observe(getViewLifecycleOwner(),mListObserver);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setRefreshing(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                Navigation.findNavController(view).popBackStack();
            }
        });

        mDataViewModel.getData(url);
        Log.d(TAG, "onCreateView: "+result);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataViewModel.getData(url);
                mRecyclerAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mStations == null){
                    Snackbar.make(mRecyclerView,"STILL LOADING...",Snackbar.LENGTH_SHORT).show();
                }else{
                    mRecyclerAdapter.getFilter().filter(charSequence.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void closeKeyboard() {
        View view = mActivity.getCurrentFocus();
        if(view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

}
