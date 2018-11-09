package guidetec.com.guidetec.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import guidetec.com.guidetec.R;
import guidetec.com.guidetec.adapter.MessageAdapter;
import guidetec.com.guidetec.model.Chat;
import guidetec.com.guidetec.model.User;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView name;

    FirebaseUser fuser;
    DatabaseReference reference;

    Intent intent;

    ImageButton btn_message_back;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar =findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("");

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_message_back=(ImageButton)findViewById(R.id.btn_message_back);
        btn_message_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile_image=findViewById(R.id.ci_message_profile);
        name=findViewById(R.id.tv_message_name);

        btn_send=(ImageButton)findViewById(R.id.btn_message_send);
        text_send=(EditText)findViewById(R.id.edit_message_send);

        intent=getIntent();
        String userid=intent.getStringExtra("userid");

        fuser=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("usuarios").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                name.setText(user.getName());

                if(user.getImage_url().equals("default")){
                    profile_image.setImageResource(R.drawable.ic_hat);
                }
                else{
                    Glide.with(MessageActivity.this).load(user.getImage_url()).into(profile_image);
                }
                readMessages(fuser.getUid(),user.getId(),user.getImage_url());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(),userid,msg);
                    text_send.setText("");
                }
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("chats").push().setValue(hashMap);
    }

    private void readMessages(final String myid, final String userid,final String imageurl){
        chats=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid)&& chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid)&&chat.getSender().equals(myid)){
                        chats.add(chat);
                    }
                    messageAdapter=new MessageAdapter(MessageActivity.this,chats,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
