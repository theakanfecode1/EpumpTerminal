package com.epump.epumpterminal.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.epump.epumpterminal.R;
import com.epump.epumpterminal.viewmodel.DataViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DashboardFragment extends Fragment {
    private String mYear;
    private String mMonth;
    private String mDay;
    private String mDate;
    private String mDateOb;

    private String mEndDate;
    private TextView NumberOfUser;
    private TextView DiscountValue;
    private TextView totalTransaction;
    private TextView TransactionValue;
    ConstraintLayout mConstraintLayout;
    private AlertDialog mAlertDialog;
    private Observer<String> numOfUSerOb = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            NumberOfUser.setText(s);
        }
    };
    private Observer<String> discountValueOb = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            String string = "₦"+s;
            DiscountValue.setText(string);
        }
    };
    private Observer<String> totalNumOfTransOb = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            totalTransaction.setText(s);
        }
    };
    private Observer<String> totalNumOfTransValueOb = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            String string = s.equals("")?"":"₦"+s;
            TransactionValue.setText(string);
            mAlertDialog.dismiss();
        }
    };

    private DataViewModel mViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        NumberOfUser = view.findViewById(R.id.amountusers);
        DiscountValue = view.findViewById(R.id.discountamount);
        totalTransaction = view.findViewById(R.id.numberoftransamount);
        TransactionValue = view.findViewById(R.id.valueamount);
        mConstraintLayout = view.findViewById(R.id.dashlayout);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ImageView imageView = view.findViewById(R.id.calender);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendershow();
            }
        });
        showFetching();
        ImageView back = view.findViewById(R.id.dashback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        mDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String[] arrd = mDate.split("-");
        mYear = arrd[0];
        mMonth = arrd[1];
        mDay = arrd[2];
        if(String.valueOf(mMonth.charAt(0)).equals("0"))
        {
            mMonth = mMonth.replace("0","");
        }
        if(String.valueOf(mDay.charAt(0)).equals("0"))
        {
            mDay = mDay.replace("0","");
        }

        mDate = mYear+"/"+mMonth+"/"+mDay;
        mDateOb = mDate;
        mEndDate = mYear+"/"+mMonth+"/"+mDay;


        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        mViewModel.setContext(getContext());
        mViewModel.getNumOfUser().observe(getViewLifecycleOwner(),numOfUSerOb);
        mViewModel.getTotalTransaction().observe(getViewLifecycleOwner(),totalNumOfTransOb);
        mViewModel.getValueTransaction().observe(getViewLifecycleOwner(),totalNumOfTransValueOb);
        mViewModel.getValueDiscount().observe(getViewLifecycleOwner(),discountValueOb);
        if(savedInstanceState != null){
            mDate = savedInstanceState.getString("STARTDATE","");
            mEndDate = savedInstanceState.getString("ENDSTART","");


        }
        mViewModel.getTotalUsers(mDate,mEndDate);
        mViewModel.getTotalDiscount(mDate,mEndDate);
        mViewModel.getTotalTransactionCount(mDate,mEndDate);
        mViewModel.getTotalTransactionValue(mDate,mEndDate);


        return view;
    }
    private void calendershow() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.calender_layout);
        WindowManager.LayoutParams ip = new WindowManager.LayoutParams();
        ip.copyFrom(dialog.getWindow().getAttributes());
        ip.width = WindowManager.LayoutParams.MATCH_PARENT;
        ip.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(ip);
        final TextView startDate =  dialog.findViewById(R.id.startdate);
        final TextView Enddate =  dialog.findViewById(R.id.enddate);
        final Button button = dialog.findViewById(R.id.applycalender);
        startDate.setText(mDateOb);
        Enddate.setText(mDateOb);
        final CardView StartCard = dialog.findViewById(R.id.startDate);
        final CardView EndCard = dialog.findViewById(R.id.endDate);
        final Button okay = dialog.findViewById(R.id.closecalender);
        final ImageView calenderf = dialog.findViewById(R.id.calenderfirst);
        final ImageView calenders = dialog.findViewById(R.id.calendersecond);
        final ImageView calendert = dialog.findViewById(R.id.calenderthrid);
        final CardView cardf = dialog.findViewById(R.id.cardfirst);
        final CardView cards = dialog.findViewById(R.id.cardsecond);
        final CardView cardt =  dialog.findViewById(R.id.cardthrid);
        final TextView textf = dialog.findViewById(R.id.today);
        final TextView texts = dialog.findViewById(R.id.lastweek);
        final TextView textt = dialog.findViewById(R.id.lastmonth);

        final MaterialCalendarView mcv =  dialog.findViewById(R.id.realcalender);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcv.setVisibility(View.GONE);
                okay.setVisibility(View.GONE);
                StartCard.setVisibility(View.VISIBLE);
                EndCard.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }
        });
        StartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcv.setVisibility(View.VISIBLE);
                StartCard.setVisibility(View.GONE);
                EndCard.setVisibility(View.GONE);
                okay.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);


                String startDetails = startDate.getText().toString();
                String[] arrd = startDetails.split("/");
                String year = arrd[0];
                String month = arrd[1];
                String day = arrd[2];

                mcv.state().edit()
                        .setFirstDayOfWeek(Calendar.MONDAY)
                        .setMinimumDate(CalendarDay.from(2020, 0, 1))
                        .setMaximumDate(CalendarDay.from(2050, 6, 12))
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit();
                Calendar calendar = Calendar.getInstance();
                mcv.setDateSelected(calendar.getTime(), true);
                mcv.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                        String  month  =Integer.toString (date.getMonth()+1);
                        String day = Integer.toString(date.getDay());
                        String year = Integer.toString(date.getYear());
                        String d = year+"/"+month+"/"+day ;
                        startDate.setText(d);
                    }
                });
            }
        });

        EndCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcv.setVisibility(View.VISIBLE);
                StartCard.setVisibility(View.GONE);
                EndCard.setVisibility(View.GONE);
                okay.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                String startDetails = Enddate.getText().toString();
                String[] arrd = startDetails.split("/");
                String year = arrd[0];
                String month = arrd[1];
                String day = arrd[2];
                mcv.state().edit()
                        .setFirstDayOfWeek(Calendar.MONDAY)
                        .setMinimumDate(CalendarDay.from(2020, 0, 1))
                        .setMaximumDate(CalendarDay.from(2050, 6, 12))
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit();
                Calendar calendar = Calendar.getInstance();
                mcv.setDateSelected(calendar.getTime(), true);
                mcv.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                        String  month  =Integer.toString (date.getMonth()+1);
                        String day = Integer.toString(date.getDay());
                        String year = Integer.toString(date.getYear());
                        String d = year+"/"+month+"/"+day ;
                        Enddate.setText(d);
                    }
                });
            }
        });

        dialog.getWindow().setGravity(Gravity.CENTER);

        cardf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDate.setText(mDateOb);
                Enddate.setText(mDateOb);
                cardf.setCardBackgroundColor(getResources().getColor(R.color.colorDarkBlue));
                calenderf.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorWhite));
                textf.setTextColor(getResources().getColor(R.color.colorWhite));
                cards.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
                calenders.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorDarkBlue));
                cardt.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
                calendert.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorDarkBlue));
                texts.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                textt.setTextColor(getResources().getColor(R.color.colorDarkBlue));

            }
        });
        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy/MM/dd");

                String week = simpleDateFormat.format(calendar.getTime());
                String[] arrd = week.split("/");
                String year = arrd[0];
                String month = arrd[1];

                String  day = arrd[2];
                if(String.valueOf(month.charAt(0)).equals("0"))
                {
                    month = month.replace("0","");
                }
                if(String.valueOf(day.charAt(0)).equals("0"))
                {
                    day = day.replace("0","");
                }
                month = Integer.toString((Integer.parseInt(month)));
                week = year+"/"+month+"/"+day;
                startDate.setText(week);
                Enddate.setText(mDateOb);
                cards.setCardBackgroundColor(getResources().getColor(R.color.colorDarkBlue));
                calenders.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorWhite));
                texts.setTextColor(getResources().getColor(R.color.colorWhite));
                cardf.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
                calenderf.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorDarkBlue));
                cardt.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
                calendert.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorDarkBlue));
                textf.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                textt.setTextColor(getResources().getColor(R.color.colorDarkBlue));

            }
        });
        cardt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int thisMonth = calendar.get(Calendar.MONTH);
                int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
                int thisYear = calendar.get(Calendar.YEAR);
                if(thisMonth == 0){
                    thisMonth = 12;
                    thisYear--;
                }
                String date = Integer.toString(thisYear)+"/"+Integer.toString(thisMonth)+"/"+Integer.toString(thisDay);
                startDate.setText(date);
                Enddate.setText(mDateOb);
                cardt.setCardBackgroundColor(getResources().getColor(R.color.colorDarkBlue));
                calendert.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorWhite));
                textt.setTextColor(getResources().getColor(R.color.colorWhite));
                cards.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
                calenders.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorDarkBlue));
                cardf.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
                calenderf.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorDarkBlue));
                texts.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                textf.setTextColor(getResources().getColor(R.color.colorDarkBlue));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDate = startDate.getText().toString();
                mEndDate = Enddate.getText().toString();

                mViewModel.getTotalUsers(startDate.getText().toString(),Enddate.getText().toString());
                mViewModel.getTotalTransactionCount(startDate.getText().toString(),Enddate.getText().toString());
                mViewModel.getTotalDiscount(startDate.getText().toString(),Enddate.getText().toString());
                mViewModel.getTotalTransactionValue(startDate.getText().toString(),Enddate.getText().toString());
                showFetching();
                dialog.dismiss();

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    public void showFetching(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.gettingdata,null);
        builder.setView(view);
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.show();

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("STARTDATE",mDate);
        outState.putString("ENDDATE",mEndDate);
        super.onSaveInstanceState(outState);

    }
}