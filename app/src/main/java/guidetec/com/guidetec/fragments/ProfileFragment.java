package guidetec.com.guidetec.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import guidetec.com.guidetec.R;
import guidetec.com.guidetec.account.LoginActivity;
import guidetec.com.guidetec.activities.GuiboActivity;
import guidetec.com.guidetec.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Button logout,btn_profile_guibo;
    FirebaseAuth auth;
    View vista;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista =inflater.inflate(R.layout.fragment_profile, container, false);

        logout=(Button)vista.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });
        btn_profile_guibo=(Button)vista.findViewById(R.id.btn_profile_guibo);
        btn_profile_guibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(getActivity(),GuiboActivity.class)));
            }
        });

        // Inflate the layout for this fragment
        return vista;
    }

}
