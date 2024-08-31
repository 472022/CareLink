package com.example.carelink;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_library#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_library extends Fragment {

    Button b1;
    LinearLayout ab,co,pr;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_library() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_library.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_library newInstance(String param1, String param2) {
        fragment_library fragment = new fragment_library();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        // Find the button by its ID
        b1 = view.findViewById(R.id.button5);
        ab = view.findViewById(R.id.abo);
        co = view.findViewById(R.id.cont);
        pr = view.findViewById(R.id.priva);


        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to start the login activity
                Intent intent = new Intent(getActivity(), aboutus.class);
                startActivity(intent);
                // Finish the current activity

            }
        });
        co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to start the login activity
                Intent intent = new Intent(getActivity(), contact.class);
                startActivity(intent);

            }
        });
        pr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to start the login activity
                Intent intent = new Intent(getActivity(), privacy.class);
                startActivity(intent);

            }
        });


        // Set an OnClickListener to the button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to start the login activity
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
                // Finish the current activity
                getActivity().finish();
            }
        });

        // Return the inflated view
        return view;
    }

}