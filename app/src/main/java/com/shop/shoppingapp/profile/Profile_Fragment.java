package com.shop.shoppingapp.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.authentification.Sign_In;

public class Profile_Fragment extends Fragment {

    ConstraintLayout cProfile , cSettings , cLogOut , cOrders , cSellOnThisApp ;
    TextView tUserName , tEmail ;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    SharedPreferences sharedPreferences ;
    String sUserName , sEmail ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    public Profile_Fragment(SharedPreferences sharedPreferences) {
        // Required empty public constructor
        this.sharedPreferences = sharedPreferences ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        //FindViews
        cProfile = view.findViewById(R.id.con_profile);
        cSettings = view.findViewById(R.id.con_settings);
        cLogOut = view.findViewById(R.id.con_log_out);
        cOrders = view.findViewById(R.id.con_orders);
        cSellOnThisApp = view.findViewById(R.id.con_sell_on_this_app);
        tUserName = view.findViewById(R.id.textview_username);
        tEmail = view.findViewById(R.id.textview_email);

        //Data work
        sUserName = sharedPreferences.getString("name","Please resign up data is lost!");
        sEmail = sharedPreferences.getString("email" , "Please resign up data is lost!");

        tUserName.setText(sUserName);
        tEmail.setText(sEmail);

        //SetOnClicks
        cProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct(Settings_Profile.class);
            }
        });

        cSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct(Security_Change.class);
            }
        });

        cLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), Sign_In.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        cOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct(MyOrders_Change.class);
            }
        });

        cSellOnThisApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.shop.VendorApp")));
            }
        });

        return view ;
    }

    public void  startAct(Class mClass){
        Intent intent = new Intent(getActivity(),mClass);
        startActivity(intent);
    }

}