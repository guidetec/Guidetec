package guidetec.com.guidetec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    //Arrays
    public int[] slide_images={
            R.drawable.ws_guidetec,
            R.drawable.ws_maps,
            R.drawable.ws_route,
            R.drawable.ws_augmented,
            R.drawable.ws_commerce
    };

    public String[] slide_headings={
      "Guidetec",
      "Cientos de sitios",
      "Rutas disponibles",
      "Interactúa con los sitios",
      "Localiza comercios"
    };
    public String[] slide_decs={
            "Turismo al alcance de tu dispositivo",
            "Consulta información de una gran cantidad de sitios turísticos de acceso gratuito o asociados a tu " +
                    "agencia de viajes. Ser un turista informado te ayudará a disfutar aún más tu viaje.",
            "Consulta las rutas que puedes seguir para disfrutar aún más tu viaje. Las rutas te permitirán conectar con los" +
                    " sitios y planear mejor tu itinerario.",
            "Interactúa con los sitios turísticos a través de Realidad Aumentada. De esta manera, tus viajes" +
                    " nunca serán aburridos.",
            "Consulta información de los comercios como restaurantes, bares, hoteles y otros, ubicados cerca de tí." +
                    " Así ahorrarás tiempo en la búsqueda y disfrutarás tu viaje."
    };

    public SlideAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView=(ImageView)view.findViewById(R.id.slide_image);
        TextView slideHeadding=(TextView)view.findViewById(R.id.slide_headding);
        TextView slideDescription=(TextView)view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeadding.setText(slide_headings[position]);
        slideDescription.setText(slide_decs[position]);


        if(position==0){
           // view=layoutInflater.inflate(R.layout.welcome_one,container,false);
        }else{

        }

        /*
        if(position==0){
            slideHeadding.setVisibility(View.INVISIBLE);
            slideDescription.setVisibility(View.INVISIBLE);
        }
        else
        {
            slideHeadding.setVisibility(View.VISIBLE);
            slideDescription.setVisibility(View.VISIBLE);
        }*/

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
