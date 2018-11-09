package guidetec.com.guidetec.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import guidetec.com.guidetec.R;
import guidetec.com.guidetec.activities.MessageActivity;
import guidetec.com.guidetec.model.Chat;
import guidetec.com.guidetec.model.User;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context context;
    private List<Chat> chats;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context context,List<Chat> chats,String imageurl){
        this.chats=chats;
        this.context=context;
        this.imageurl=imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
            View view=LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return  new MessageAdapter.ViewHolder(view);
        }else{
            View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return  new MessageAdapter.ViewHolder(view);
        }
    }

    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=chats.get(position);
        holder.show_message.setText(chat.getMessage());
        if(imageurl.equals("default")){
            holder.image.setImageResource(R.drawable.ic_hat);
        }
        else{
            Glide.with(context).load(imageurl).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView image;

        public ViewHolder(View itemView){
            super(itemView);

            show_message=itemView.findViewById(R.id.show_message);
            image=itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
