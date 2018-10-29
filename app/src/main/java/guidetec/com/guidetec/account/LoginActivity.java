package guidetec.com.guidetec.account;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import guidetec.com.guidetec.activities.MainActivity;
import guidetec.com.guidetec.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEmailField,mPasswordField;
    private Button regisButton,recButton;

    private static final String TAG = "EmailPassword";

    RelativeLayout rellay1,rellay2;

    //Dialog
    Dialog dialog;
    TextView logInFailTitle,logInFailDescription;
    ImageView logInCorrectClose,logInFailClose;
    Button logInCorrectAccept,logInFailAccept;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Views
        mEmailField=(EditText)findViewById(R.id.mEmailField);
        mPasswordField=(EditText)findViewById(R.id.mPasswordField);

        //Dialog
        dialog=new Dialog(this);

        rellay1=(RelativeLayout)findViewById(R.id.rellay1);
        rellay2=(RelativeLayout)findViewById(R.id.rellay2);

        //Buttons
        findViewById(R.id.logInButton).setOnClickListener(this);
        findViewById(R.id.regisButton).setOnClickListener(this);
        findViewById(R.id.recButton).setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        //FirebaseUser current=mAuth.getCurrentUser();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        //Toast.makeText(LoginActivity.this,"OnStart",Toast.LENGTH_LONG);
    }
    // [END on_start_check_user]



    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(!user.isEmailVerified()){
                                hideProgressDialog();
                                ShowFailPopup();
                            }
                            else{
                                hideProgressDialog();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        if(i==R.id.regisButton){
            Intent intent=new Intent(LoginActivity.this,SigninActivity.class);
            startActivity(intent);
        }
        else if (i == R.id.logInButton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
    public void ShowFailPopup(){
        dialog.setContentView(R.layout.message_login_fail);
        logInFailClose=(ImageView)dialog.findViewById(R.id.logInFailClose);
        logInFailAccept=(Button)dialog.findViewById(R.id.logInFailAccept);
        logInFailTitle=(TextView)dialog.findViewById(R.id.logInFailTitle);
        logInFailDescription=(TextView)dialog.findViewById(R.id.logInFailDescription);

        //Config
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        //Set text
        logInFailAccept.setText("Reenviar correo");
        logInFailTitle.setText("¡Inicio de sesión fallido!");
        logInFailDescription.setText("No se ha confirmado el correo electrónico. Por favor verifícalo para poder iniciar sesión.");

        logInFailClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                dialog.dismiss();
            }
        });

        logInFailAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
                mAuth.signOut();
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            /*Toast.makeText(SigninActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();*/
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            /*Toast.makeText(SigninActivity.this,
                                    "Failed to send verification email",
                                    Toast.LENGTH_SHORT).show();*/
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }
}
