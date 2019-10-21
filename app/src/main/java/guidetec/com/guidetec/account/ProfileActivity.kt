package guidetec.com.guidetec.account

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.DownloadManager
import android.app.PendingIntent.getActivity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import android.widget.*
import kotlinx.android.synthetic.main.activity_profile.*
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.util.Log
import android.view.DragAndDropPermissions
import android.view.DragEvent
import android.webkit.MimeTypeMap
import com.google.android.gms.tasks.*
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import io.grpc.Context
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap
import android.util.Base64
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.tapadoo.alerter.Alerter
import guidetec.com.guidetec.framework.shareddata.SignUpValues
import guidetec.com.guidetec.framework.shareddata.UserSharedPreference
import guidetec.com.guidetec.R
import guidetec.com.guidetec.activities.StartActivity
import guidetec.com.guidetec.classes.User
import guidetec.com.guidetec.framework.utils.PackageUtilsKt

class ProfileActivity : AppCompatActivity() {

    private lateinit var back: ImageButton
    private lateinit var footer:RelativeLayout
    private lateinit var signUpValues: SignUpValues
    private lateinit var userSharedPreference: UserSharedPreference

    private lateinit var name:EditText
    private lateinit var sname:EditText
    private lateinit var fname:EditText
    private lateinit var button_save:Button
    private lateinit var backB:ImageButton

    private lateinit var nameS:String
    private lateinit var snameS:String
    private lateinit var fnameS:String

    //To change profile picture
    private final var RESULT_OK=-1
    private final var REQUEST_CODE=1
    private lateinit var imageProfile: CardView
    private lateinit var pr_image:ImageView


    private val PReqCode:Int=1
    private lateinit var pickedImgUri:Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initPreferences()
        initComponets()
        initMethods()
    }

    public override fun onStart() {
        super.onStart()
    }
    private fun initPreferences() {
        val context = this
        //this.accountPreferences = AccountPreferences(context)
        //this.sessionPreferences = SessionPreferences(context)
        this.userSharedPreference = UserSharedPreference(context)
        //this.cardDetailPreferences = CardDetailPreferences(context)
        this.signUpValues = SignUpValues(context)
    }
    private fun initMethods(){
        footer.setOnClickListener{view->

            FirebaseAuth.getInstance().signOut()
            UserSharedPreference(this).deleteUser()
            SignUpValues(this).clearSignupVariables()
            finishAffinity()
            startActivity(Intent(baseContext, StartActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
            finish()
        }
        backB.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                finish()
            }

        })
        button_save.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                if(button_save.isEnabled)
                    saveUserData()
            }

        })
        name.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                EnableSaveItemButton()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        sname.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                EnableSaveItemButton()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        fname.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                EnableSaveItemButton()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        imageProfile.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                if(Build.VERSION.SDK_INT>=22){
                    checkAndRequestForPermission()
                }else{
                    openGallery()
                }
            }

        })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    fun checkAndRequestForPermission(){
        if(ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(READ_EXTERNAL_STORAGE),
                PReqCode)

            /*if(ActivityCompat.shouldShowRequestPermissionRationale(this@ProfileActivity,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this,"Acepta el permiso necesario",Toast.LENGTH_LONG).show()
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PReqCode)
            }*/
        }
        else{
            openGallery()
        }

    }


    private fun initComponets(){
        //Set values
        var user: User =userSharedPreference.getUser();
        findViewById<TextView>(R.id.email_top).setText(user.email)

        name=findViewById<EditText>(R.id.nombre)
        name.setText(user.name)
        sname=findViewById<EditText>(R.id.sname)
        sname.setText(user.sname)
        fname=findViewById<EditText>(R.id.fname)
        fname.setText(user.fname)

        nameS=user.name
        snameS=user.sname
        fnameS=user.fname

        backB=findViewById(R.id.back)
        button_save=findViewById(R.id.btn_save)
        footer=findViewById(R.id.footer)
        button_save.isEnabled=false

        //For storage
        //reference=FirebaseDatabase.getInstance().getReference("users").child(userSharedPreference.user.uuid)
        //storageReference=FirebaseStorage.getInstance().getReference("profileimg")
        imageProfile=findViewById(R.id.imageProfile)
        pr_image=findViewById(R.id.pr_image)

        //if(!user.imageUrl.equals(""))
          //  loadImageFromUrl(user.imageUrl,pr_image)



        if(!signUpValues.profileB64.equals("")){
            var imageBytes = Base64.decode(signUpValues.profileB64, Base64.DEFAULT);
            var decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
            pr_image.setImageBitmap(decodedImage);
        }else if(!user.base64.equals("")) {
            var imageBytes=Base64.decode(user.base64,Base64.DEFAULT)
            var decodedImage=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
            pr_image.setImageBitmap(decodedImage)

        }

    }

    fun EnableSaveItemButton() {
        val b=name.text.toString()!=nameS
        val b1=sname.text.toString()!=snameS
        val b2=fname.text.toString()!=fnameS

        if(b or b1 or b2){
            btn_save.setTextColor(Color.parseColor("#FFFFFF"))
            btn_save.setBackgroundResource(R.drawable.button_blue_round)
            button_save.isEnabled=true
        }else{
            btn_save.setTextColor(Color.parseColor("#4b5d73"))
            btn_save.setBackgroundResource(R.drawable.button_blue_gray)
            button_save.isEnabled=false
        }
    }
    fun saveUserData(){

        Alerter.create(this)
            .setTitle("Actualizando")
            .setText("¡Ya casi teminamos!")
            .enableProgress(true)
            .setProgressColorRes(R.color.darkGreen)
            .setBackgroundColorRes(R.color.colorBlue)
            .show()

        nameS=name.text.toString()
        snameS=sname.text.toString()
        fnameS=fname.text.toString()
        if(!TextUtils.isEmpty(nameS) or !TextUtils.isEmpty(snameS) or !TextUtils.isEmpty(fnameS)){
            var user:User=userSharedPreference.getUser();
            user.name=nameS
            user.sname=snameS
            user.fname=fnameS

            userSharedPreference.saveOrUpdateUser(user)

            Alerter.hide()
            Alerter.create(this@ProfileActivity)
                .setTitle("¡Está hecho!")
                .setText("Se ha actualizado tu información")
                .setIcon(R.drawable.ic_complete)
                .setEnterAnimation(R.anim.abc_slide_in_top)
                .setExitAnimation(R.anim.alerter_exit)
                .setIconColorFilter(0) // Optional - Removes white tint
                .setBackgroundColorRes(R.color.colorBlue)
                .show()


            EnableSaveItemButton()
        }
        else{
            if(TextUtils.isEmpty(nameS))
                name.setError("Requerido")
            else
                name.setError(null)

            if(TextUtils.isEmpty(snameS))
                sname.setError("Requerido")
            else
                sname.setError(null)

            if(TextUtils.isEmpty(fnameS))
                fname.setError("Requerido")
            else
                fname.setError(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK && requestCode==REQUEST_CODE && data!=null){
            pickedImgUri=data.data
            //pr_image.setImageURI(pickedImgUri)

            //updateUserImage(pickedImgUri)
            saveImgage(pickedImgUri)

        }else{
            Toast.makeText(this,"No se ha seleccionado imagen",Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PReqCode -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Se ha denegado el permiso",Toast.LENGTH_LONG).show()
                } else {
                    openGallery()
                }
            }
        }
    }
fun updateUserImage(image:Uri){
    var mStorage= FirebaseStorage.getInstance().getReference().child("users_photos")
    var imageFilePath=mStorage.child(userSharedPreference.user.uuid)
    imageFilePath.putFile(image).addOnSuccessListener(object:OnSuccessListener<UploadTask.TaskSnapshot>{
        override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
            imageFilePath.downloadUrl.addOnSuccessListener(object:OnSuccessListener<Uri>{
                override fun onSuccess(p0: Uri?) {
                    var user=UserSharedPreference(this@ProfileActivity).user
                    user.imageUrl= imageFilePath.downloadUrl.toString()
                    UserSharedPreference(this@ProfileActivity).saveOrUpdateUser(user)
                }

            })
        }
    })
}

    fun saveImgage(uri:Uri){
        if (uri != null) {
            var file = File(uri.getPath());
            if (!file.exists() || !file.isFile()) {
                var path = PackageUtilsKt.getPathFromUri(this,uri)
                if (path == null) {
                    path = "";
                }
                var  b=File(path).exists()
                var c=File(path).isFile
                if (b && c) {
                    var decodeFile = BitmapFactory.decodeFile(path);
                    if (decodeFile != null) {
                        var bitmapOptimized = PackageUtilsKt.bitmapOptimized(decodeFile);
                        pr_image.setImageBitmap(getScaledBitmap(bitmapOptimized));
                        var signUpValues2 = this.signUpValues;
                        if (signUpValues2 == null) {
                            //Intrinsics.throwUninitializedPropertyAccessException(signvalues);
                        }
                        signUpValues.setProfilePic(PackageUtilsKt.bitmapToBase64(bitmapOptimized));
                        var usr=userSharedPreference.user
                        usr.base64=PackageUtilsKt.bitmapToBase64(bitmapOptimized)
                        userSharedPreference.saveOrUpdateUser(usr)
                    }
                }
                return;
            }
            var decodeFile2 = BitmapFactory.decodeFile(file.getPath());
            if (decodeFile2 != null) {
                var bitmapOptimized2 = PackageUtilsKt.bitmapOptimized(decodeFile2);
                this.signUpValues.setProfilePic(PackageUtilsKt.bitmapToBase64(bitmapOptimized2));
            }
        }

    }
    fun getScaledBitmap(bitmap:Bitmap): Bitmap {
        return PackageUtilsKt.getCroppedBitmap(bitmap, PackageUtilsKt.convertDpToPixel(64.0f, this).toInt());
    }
}
