package guidetec.com.guidetec.account

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import guidetec.com.guidetec.R
import guidetec.com.guidetec.classes.PasswordStrength
import guidetec.com.guidetec.classes.User

class SignupActivity : AppCompatActivity(),TextWatcher {
    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        updatePasswordStrengthView(p0.toString());
    }

    private lateinit var signupName:EditText
    private lateinit var signupPhone:EditText
    private lateinit var signupEmail:EditText
    private lateinit var signupPassword:EditText
    private lateinit var signupProgressBar:ProgressBar
    private lateinit var signupButton:Button

    private lateinit var dialog: Dialog
    private lateinit var sigInCorrectTitle: TextView
    private lateinit var sigInCorrectDescription:TextView
    private lateinit var sigInCorrectClose: ImageView
    private lateinit var sigInFailClose:ImageView
    private lateinit var sigInCorrectAccept: Button
    private lateinit var sigInFailAccept:Button

    private lateinit var dbReference: DatabaseReference
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth

    private lateinit var name:String
    private lateinit var email:String
    private lateinit var password: String
    private lateinit var phone:String
    private lateinit var uuid:String
    private lateinit var rlProgressBar: RelativeLayout

    private lateinit var back:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initComponents()
        initMethods()

    }

    fun initComponents(){
        signupName=findViewById(R.id.signupFirstName)
        signupPhone=findViewById(R.id.signupPhone)
        signupEmail=findViewById(R.id.signupEmail)
        signupPassword=findViewById(R.id.signupPassword)
        rlProgressBar=findViewById(R.id.rlProgressBar)
        signupButton=findViewById(R.id.signupButton)

        back=findViewById(R.id.back)

        //Dialog
        dialog = Dialog(this)



        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()
        dbReference=database.reference.child("users")
    }

    fun initMethods(){
        signupButton.setOnClickListener(View.OnClickListener {
            createNewAccount()
        })
        signupPassword.addTextChangedListener(this)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun createNewAccount(){
        name=signupName.text.toString()
        phone=signupPhone.text.toString()
        email=signupEmail.text.toString()
        password=signupPassword.text.toString()

        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            showProgressDialog()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                    task->

                    if(task.isComplete){
                        val user:FirebaseUser?=auth.currentUser
                        verifyEmail(user)

                        val us= User()
                        us.uuid= user!!.uid
                        us.name =name
                        us.email=email
                        us.phone=phone
                        us.isVerified=false

                        dbReference.child(user.uid).setValue(us)
                        deleteProgressDialog()
                        succes()
                    }
                }
        }else{
            if(TextUtils.isEmpty(email))
                signupEmail.setError("Requerido")
            else
                signupEmail.setError(null)

            if(TextUtils.isEmpty(password))
                signupPassword.setError("Requerido")
            else
                signupPassword.setError(null)

            if(TextUtils.isEmpty(name))
                signupName.setError("Requerido")
            else
                signupName.setError(null)

            if(TextUtils.isEmpty(phone))
                signupPhone.setError("Requerido")
            else
                signupPhone.setError(null)

        }
    }
    private fun succes(){
        showCorrectPupup()
        //startActivity(Intent(this,LoginActivity::class.java))
        //finish()
    }
    private fun verifyEmail(user:FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task->

                if(task.isComplete){
                    //Toast.makeText(this,"Correo enviado",Toast.LENGTH_LONG).show()
                }else{
                    //Toast.makeText(this,"Correo no enviado",Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showCorrectPupup() {
        dialog.setContentView(R.layout.message_sigin_correct)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        sigInCorrectClose = dialog.findViewById(R.id.sigInCorrectClose) as ImageView
        sigInCorrectAccept = dialog.findViewById(R.id.sigInCorrecAccept) as Button

        sigInCorrectAccept.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            auth.signOut()
            //startActivity(Intent(this,LoginActivity::class.java))
            finish()
        })
        sigInCorrectClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            auth.signOut()
            //startActivity(Intent(this,LoginActivity::class.java))
            finish()
        })
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
    fun showProgressDialog(){
        rlProgressBar.visibility= View.VISIBLE
    }
    fun deleteProgressDialog(){
        rlProgressBar.visibility= View.GONE
    }
    private fun updatePasswordStrengthView(password: String) {

        val images=ArrayList<ImageView>()
        images.add(findViewById<View>(R.id.lp1) as ImageView)
        images.add(findViewById<View>(R.id.lp2) as ImageView)
        images.add(findViewById<View>(R.id.lp3) as ImageView)
        images.add(findViewById<View>(R.id.lp4) as ImageView)

        for (x in images)
            x.setImageDrawable(resources.getDrawable(R.drawable.pc_none))

        if (password.equals("")) {
            for (x in images)
                x.setImageDrawable(resources.getDrawable(R.drawable.pc_none))
            return
        }

        val str = PasswordStrength.calculateStrength(password)

        if (str.getText(this).equals("Debil")) {
            images[0].setImageDrawable(resources.getDrawable(R.drawable.pc_weak))
        } else if (str.getText(this).equals("Media")) {
            images[0].setImageDrawable(resources.getDrawable(R.drawable.pc_medium))
            images[1].setImageDrawable(resources.getDrawable(R.drawable.pc_medium))
        } else if (str.getText(this).equals("Fuerte")) {
            images[0].setImageDrawable(resources.getDrawable(R.drawable.pc_strong))
            images[1].setImageDrawable(resources.getDrawable(R.drawable.pc_strong))
            images[2].setImageDrawable(resources.getDrawable(R.drawable.pc_strong))
        } else {
            for (x in images)
                x.setImageDrawable(resources.getDrawable(R.drawable.pc_strong))
        }
    }
}
