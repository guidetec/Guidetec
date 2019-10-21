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

import java.util.List;

import guidetec.com.guidetec.R;
import guidetec.com.guidetec.activities.MessageActivity;
import guidetec.com.guidetec.classes.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    int tipo;

    public UserAdapter(Context context,List<User> users){
        this.users=users;
        this.context=context;
    }
    public UserAdapter(Context context,List<User> users,int tipo){
        this.users=users;
        this.context=context;
        this.tipo=tipo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return  new UserAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=users.get(position);
        holder.nombre.setText(user.getName());
        if(user.getImageUrl().equals("default")){
            holder.image.setImageResource(R.drawable.ic_hat);
        }else{
            Glide.with(context).load(user.getImageUrl()).into(holder.image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MessageActivity.class);
                intent.putExtra("userid",user.getUuid());
                context.startActivity(intent);
                if(tipo==0){
                    ((Activity)context).finish();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nombre;
        public ImageView image;

        public ViewHolder(View itemView){
            super(itemView);

            nombre=itemView.findViewById(R.id.tv_uitem_name);
            image=itemView.findViewById(R.id.ci_uitem_image);
        }
    }
}
