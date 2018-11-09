package guidetec.com.guidetec.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import guidetec.com.guidetec.R;
import guidetec.com.guidetec.activities.ContactSelect;
import guidetec.com.guidetec.adapter.UserAdapter;
import guidetec.com.guidetec.model.Chat;
import guidetec.com.guidetec.model.Chatlist;
import guidetec.com.guidetec.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    FloatingActionButton new_message;
    View vista;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    private List<String> userList;

    FirebaseUser fuser;
    DatabaseReference reference;

    RelativeLayout layout_message_busqueda;
    ImageButton btn_message_close, btn_message_search;
    EditText editText;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_message, container, false);

        //bind
        layout_message_busqueda=(RelativeLayout)vista.findViewById(R.id.layout_message_busqueda);
        btn_message_close=(ImageButton)vista.findViewById(R.id.btn_message_close);
        btn_message_search=(ImageButton)vista.findViewById(R.id.btn_message_search);
        editText=(EditText)vista.findViewById(R.id.edit_message_texto);

        //Btn configuration
        btn_message_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_message_busqueda.setVisibility(View.VISIBLE);
                btn_message_search.setVisibility(View.GONE);

                editText.requestFocusFromTouch();
                editText.requestFocus(); //Asegurar que editText tiene focus
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        btn_message_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_message_busqueda.setVisibility(View.GONE);
                btn_message_search.setVisibility(View.VISIBLE);
                editText.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });

        new_message=(FloatingActionButton) vista.findViewById(R.id.new_message);
        new_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ContactSelect.class));
            }
        });

        recyclerView=vista.findViewById(R.id.recycler_messages_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser=FirebaseAuth.getInstance().getCurrentUser();
        userList=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(fuser.getUid())){
                        userList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fuser.getUid())){
                        userList.add(chat.getSender());
                    }
                }

                Set<String> hs = new HashSet<>();
                hs.addAll(userList);
                userList.clear();
                userList.addAll(hs);

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return vista;
    }

private void readChats(){
        mUsers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("usuarios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    for(String id: userList){
                       if(user.getId().equals(id)){
                           mUsers.add(user);
                       }
                    }
                }
                /*
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    for(String id: userList){
                        if(mUsers.size()!=0){
                            for(User user1:mUsers){
                                if(!user.getId().equals(user1.getId())){
                                    mUsers.add(user);
                                }
                            }
                        }else{
                            mUsers.add(user);
                        }
                    }
                }
                */
                userAdapter=new UserAdapter(getContext(),mUsers,1);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
}
}
