package guidetec.com.guidetec.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.squareup.picasso.Picasso
import guidetec.com.guidetec.R
import guidetec.com.guidetec.classes.Commerce


public class CommerceAdapter(mContext:Context, data:List<Commerce>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


  val mContext: Context=mContext
  val data:List<Commerce> = data

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

      var viewHolder:PartnerViewHolder=holder as PartnerViewHolder
      viewHolder.setIsRecyclable(false)
      var item:Commerce=data.get(position)
      viewHolder.itemName.text=item.name
      viewHolder.itemClas.text=item.clasif
      viewHolder.itemDirec.text=item.direc
      viewHolder.itemStatus.text=item.status

      viewHolder.container.animation=AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation)


  }


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
      val layout:View=LayoutInflater.from(mContext).inflate(R.layout.item_commerce,parent,false)
      return PartnerViewHolder(layout)
  }

  override fun getItemCount(): Int {
    return data.size
  }

  public class PartnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var container:RelativeLayout=itemView.findViewById(R.id.item_text)
    var itemName:TextView=itemView.findViewById(R.id.commerce_name)
    var itemClas:TextView=itemView.findViewById(R.id.clasif)
      var itemDirec:TextView=itemView.findViewById(R.id.direc)
      var itemStatus:TextView=itemView.findViewById(R.id.stat)

  }
}
