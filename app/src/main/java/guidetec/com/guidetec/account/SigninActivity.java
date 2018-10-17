package guidetec.com.guidetec.account;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import guidetec.com.guidetec.R;

public class SigninActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEmailFieldR,mPasswordFieldR,mNameFieldR;

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    //Dialog
    Dialog dialog;
    TextView sigInCorrectTitle,sigInCorrectDescription;
    ImageView sigInCorrectClose,sigInFailClose;
    Button sigInCorrectAccept,sigInFailAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Dialog
        dialog=new Dialog(this);

        //Views
        mEmailFieldR=(EditText)findViewById(R.id.mEmailFieldR);
        mPasswordFieldR=(EditText)findViewById(R.id.mPasswordFieldR);
        mNameFieldR=(EditText)findViewById(R.id.mNameFieldR);

        //Buttons
        findViewById(R.id.signInButton).setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Toast.makeText(SigninActivity.this, "Registro exitoso",Toast.LENGTH_SHORT).show();
                            sendEmailVerification();
                            hideProgressDialog();
                            ShowCorrectPupup();
                            //finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(SigninActivity.this, "Registro fallido",Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            ShowFailPopup();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
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

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailFieldR.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailFieldR.setError("Requerido");
            valid = false;
        } else {
            mEmailFieldR.setError(null);
        }

        String password = mPasswordFieldR.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordFieldR.setError("Requerido");
            valid = false;
        } else {
            mPasswordFieldR.setError(null);
        }

        String name = mNameFieldR.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mNameFieldR.setError("Requerido");
            valid = false;
        } else {
            mNameFieldR.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        if(i==R.id.signInButton){
            createAccount(mEmailFieldR.getText().toString(), mPasswordFieldR.getText().toString());
        }
    }
    public void ShowCorrectPupup(){
        dialog.setContentView(R.layout.message_sigin_correct);
        sigInCorrectClose=(ImageView)dialog.findViewById(R.id.sigInCorrectClose);
        sigInCorrectAccept=(Button)dialog.findViewById(R.id.sigInCorrecAccept);

        sigInCorrectAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        sigInCorrectAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void ShowFailPopup(){
        dialog.setContentView(R.layout.message_sigin_fail);
        sigInFailClose=(ImageView)dialog.findViewById(R.id.sigInFailClose);
        sigInFailAccept=(Button)dialog.findViewById(R.id.sigInFailAccept);

        sigInFailAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
