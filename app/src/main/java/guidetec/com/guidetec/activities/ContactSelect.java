package guidetec.com.guidetec.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import guidetec.com.guidetec.R;
import guidetec.com.guidetec.adapter.UserAdapter;
import guidetec.com.guidetec.classes.User;

public class ContactSelect extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageButton btn_contact_search,btn_contact_close,btn_contact_refresh,btn_contact_back;
    private RelativeLayout layout_contact_busqueda;
    private EditText edit_contact_texto;
    private TextView tv_contact_new;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_select);

        toolbar=(Toolbar)findViewById(R.id.toolbar_contacts);
        setSupportActionBar(toolbar);

        btn_contact_search=(ImageButton)findViewById(R.id.btn_contact_search);
        btn_contact_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_contact_busqueda.setVisibility(View.VISIBLE);
                btn_contact_search.setVisibility(View.GONE);
                btn_contact_refresh.setVisibility(View.GONE);
                btn_contact_back.setVisibility(View.GONE);

                edit_contact_texto.requestFocusFromTouch();
                edit_contact_texto.requestFocus(); //Asegurar que editText tiene focus
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edit_contact_texto, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        btn_contact_close=(ImageButton)findViewById(R.id.btn_contact_close);
        btn_contact_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_contact_busqueda.setVisibility(View.GONE);
                btn_contact_search.setVisibility(View.VISIBLE);
                btn_contact_refresh.setVisibility(View.VISIBLE);
                btn_contact_back.setVisibility(View.VISIBLE);
                edit_contact_texto.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_contact_texto.getWindowToken(), 0);
            }
        });

        btn_contact_refresh=(ImageButton)findViewById(R.id.btn_contact_refresh);
        btn_contact_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ContactSelect.this,"Refresh",Toast.LENGTH_LONG).show();
            }
        });

        btn_contact_back=(ImageButton)findViewById(R.id.btn_contact_back);
        btn_contact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout_contact_busqueda=(RelativeLayout)findViewById(R.id.layout_contact_busqueda);

        edit_contact_texto=(EditText)findViewById(R.id.edit_contact_texto);

        //Users
        recyclerView=findViewById(R.id.recicler_contact);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        users = new ArrayList<>();
        readUsers();
    }

    private void readUsers(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    assert user!=null;
                    assert firebaseUser!=null;
                    if(!user.getUuid().equals(firebaseUser.getUid())){
                        users.add(user);
                    }
                }
                userAdapter=new UserAdapter(ContactSelect.this,users,0);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
