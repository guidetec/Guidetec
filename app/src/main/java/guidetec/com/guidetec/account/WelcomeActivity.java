package guidetec.com.guidetec.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import guidetec.com.guidetec.R;
import guidetec.com.guidetec.activities.SlideAdapter;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager slideViewPager;
    private LinearLayout dotsLayout;

    private SlideAdapter slideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        slideViewPager=(ViewPager)findViewById(R.id.slideViewPager);
        dotsLayout=(LinearLayout)findViewById(R.id.dotsLayout);

        slideAdapter=new SlideAdapter(this);
        slideViewPager.setAdapter(slideAdapter);

        Intent i=new Intent(this,LoginActivity.class);
        //startActivity(i);
    }
}
