package com.epump.epumpterminal.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.Navigation;

import com.epump.epumpterminal.R;
import com.epump.epumpterminal.utils.Constants;
import com.epump.epumpterminal.viewmodel.MappingViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MappingFragment extends Fragment {
    CardView mNewStation;
    CardView mMappedStation;
    CardView mManageStation;
    Button dashboard;
    Button logOutBtn;
    Boolean isDataOn;
    private MappingViewModel mappingViewModel;
    public static final String KEY = "mapped";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapping,container,false);
        getActivity().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.white_background));
        dashboard = view.findViewById(R.id.go_to_dash);
        TextView user = view.findViewById(R.id.home_name);
        user.append(" "+SplashScreenFragment.firstName);
        mappingViewModel = new ViewModelProvider(this).get(MappingViewModel.class);
        logOutBtn = view.findViewById(R.id.log_out);
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mappingFragment_to_loginFragment);
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPref  = sharedPreferences.edit();
                sharedPref.clear();
                sharedPref.apply();
                mappingViewModel.deleteAllUser();
            }
        });
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mappingFragment_to_dashboardFragment);
            }
        });
        mNewStation = view.findViewById(R.id.new_stations);
        mMappedStation = view.findViewById(R.id.mapped_stations);
        mManageStation = view.findViewById(R.id.manage_stations);
        mManageStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.controllerFragment);
            }
        });
        mNewStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDataOn = isDataAvailable();
                if(isDataOn){
                    MappingFragmentDirections.ActionMappingFragmentToMappableStationFragment action = MappingFragmentDirections.actionMappingFragmentToMappableStationFragment(false);
                    Navigation.findNavController(view).navigate(action);
                }
                else{
                    showalertDialog();
                }

            }
        });
        mMappedStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDataOn = isDataAvailable();
                if(isDataOn){
                    MappingFragmentDirections.ActionMappingFragmentToMappableStationFragment action = MappingFragmentDirections.actionMappingFragmentToMappableStationFragment(true);
                    Navigation.findNavController(view).navigate(action);
                }else{
                    showalertDialog();
                }

            }
        });

        return view;
    }

    private void showalertDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_alert_mapping);
        WindowManager.LayoutParams ip = new WindowManager.LayoutParams();
        ip.copyFrom(dialog.getWindow().getAttributes());
        ip.width = WindowManager.LayoutParams.WRAP_CONTENT;
        ip.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button button = dialog.findViewById(R.id.closeerrormessage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(ip);
    }

    private boolean isDataAvailable() {
        if (getContext() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }

        }

        return false;
    }

}
