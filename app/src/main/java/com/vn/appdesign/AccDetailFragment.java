package com.vn.appdesign;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vn.utility.ShowDropDownMenuNoti;

public class AccDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseUser mAuth;
    private String mParam1;
    private String mParam2;
    TextView userIdDisplay;
    TextView userNameDisplay;
    ImageView userAvataDisplay;
    Button notiBtn;
    Button btnBack;

    public AccDetailFragment() {
        // Required empty public constructor
    }

    public static AccDetailFragment newInstance(String param1, String param2) {
        AccDetailFragment fragment = new AccDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acc_detail, container, false);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        userIdDisplay = view.findViewById(R.id.user_id_text_view);
        userNameDisplay = view.findViewById(R.id.name_input_text_view);
        userAvataDisplay = view.findViewById(R.id.avata_user_img_view);
        notiBtn = view.findViewById(R.id.btn_open_notification_account_detail);
        notiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDropDownMenuNoti.showDropdownMenu(v);
            }
        });
        btnBack = view.findViewById(R.id.btn_back_to_acc);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToAccFragment();
            }
        });
        if(mAuth!=null) {
            String userID = mAuth.getUid();
            String userName = mAuth.getDisplayName();
            Uri userAvata = mAuth.getPhotoUrl();
            if (!userID.isEmpty()) {
                userIdDisplay.setText(userID);
            }
            if (!userName.isEmpty()) {
                userNameDisplay.setText(userName);
            }
            Glide.with(view).load(userAvata).error(R.drawable.baseline_account_circle_24).into(userAvataDisplay);
        }
        return view;
    }
    private void navigateBackToAccFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}