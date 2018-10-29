package guidetec.com.guidetec.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import guidetec.com.guidetec.activities.MainActivity;
import guidetec.com.guidetec.account.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //get current user
        FirebaseUser currentUser = auth.getCurrentUser();

        Intent intentMain=new Intent(SplashActivity.this,MainActivity.class);
        Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);

        Intent intent =getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        if(currentUser!=null) {
            startActivity(intentMain);
            finish();
        }
        else{
            startActivity(intentLogin);
            finish();
        }
    }
}
