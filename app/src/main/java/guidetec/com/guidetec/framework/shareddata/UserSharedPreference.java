package guidetec.com.guidetec.framework.shareddata;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import guidetec.com.guidetec.classes.Tags;
import guidetec.com.guidetec.classes.User;

public class UserSharedPreference {

    private final String BIRTHDATE_USER;
    private final String BIRTHSTATE_USER;
    private final String BIRTH_COUNTRY_USER;
    private final String EMAIL_USER;
    private final String FNAME_USER;
    private final String GENDER_USER;
    private final String IS_EMAIL_VERIFIED;
    private final String NAME_USER;
    private final String OCCUPATION_USER;
    private final String PHONE_USER;
    private final String PH_COUNTRY_CODE_USER;
    private final String SNAME_USER;
    private final String UUID_USER;
    private final String IMAGE_URL;
    private final String BASE64;
    private final Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    public UserSharedPreference(Context context){
        this.context = context;
        this.UUID_USER = "uuidUser";
        this.NAME_USER = "nameUser";
        this.EMAIL_USER = "emailUser";
        this.FNAME_USER = "fnameUser";
        this.SNAME_USER = "snameUser";
        this.PHONE_USER = "phoneUser";
        this.GENDER_USER = "genderUser";
        this.BIRTHSTATE_USER = "birthstateUser";
        this.BIRTHDATE_USER = "birthDateUser";
        this.OCCUPATION_USER = "occupationUser";
        this.PH_COUNTRY_CODE_USER = "phoneCode";
        this.BIRTH_COUNTRY_USER = "birthCountry";
        this.IS_EMAIL_VERIFIED = "isEmailVerified";
        this.IMAGE_URL = "imageUrl";
        this.BASE64="base64";
        Context context3 = this.context;
        SharedPreferences.Editor editor2 = null;
        
        this.sharedPreferences = context != null ? context.getSharedPreferences(Tags.NAME_PREFERENCE, 0) : null;
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        if (sharedPreferences2 != null) {
            editor2 = sharedPreferences2.edit();
        }
        this.editor = editor2;
    }
    public final User getUser(){
        User user=new User();
        if(sharedPreferences!=null){
            user.setName(sharedPreferences.getString(NAME_USER, ""));
            user.setSname(sharedPreferences.getString(SNAME_USER, ""));
            user.setFname(sharedPreferences.getString(FNAME_USER, ""));
            user.setBirthCountry(sharedPreferences.getString(BIRTH_COUNTRY_USER, ""));
            user.setBirthDate(sharedPreferences.getString(BIRTHDATE_USER, ""));
            user.setBirthState(sharedPreferences.getString(BIRTHSTATE_USER, ""));
            user.setEmail(sharedPreferences.getString(EMAIL_USER, ""));
            user.setGender(sharedPreferences.getString(GENDER_USER, ""));
            user.setOcupation(sharedPreferences.getString(OCCUPATION_USER, ""));
            user.setPhone(sharedPreferences.getString(PHONE_USER, ""));
            user.setPhoneCode(sharedPreferences.getString(PH_COUNTRY_CODE_USER, ""));
            user.setUuid(sharedPreferences.getString(UUID_USER, ""));
            user.setImageUrl(sharedPreferences.getString(IMAGE_URL, ""));
            user.setBase64(sharedPreferences.getString(BASE64,""));
            //user.setVerified(sharedPreferences.getString(IS_EMAIL_VERIFIED, ""));
        }
       return user;
    }
    public final void saveOrUpdateUser(User user) {
        SharedPreferences.Editor editor1 = this.editor;
        String str1 = null;
        if (editor1 != null) {
            editor1.putString(this.UUID_USER, user != null ? user.getUuid() : null);
        }
        SharedPreferences.Editor editor2 = this.editor;
        String str = null;
        if (editor2 != null) {
            editor2.putString(this.IMAGE_URL, user != null ? user.getImageUrl() : null);
        }
        SharedPreferences.Editor edito2 = this.editor;
        if (edito2 != null) {
            edito2.putString(this.BASE64, user != null ? user.getBase64() : null);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.putString(this.NAME_USER, user != null ? user.getName() : null);
        }
        SharedPreferences.Editor editor4 = this.editor;
        if (editor4 != null) {
            editor4.putString(this.EMAIL_USER, user != null ? user.getEmail() : null);
        }
        SharedPreferences.Editor editor5 = this.editor;
        if (editor5 != null) {
            editor5.putString(this.FNAME_USER, user != null ? user.getFname() : null);
        }
        SharedPreferences.Editor editor6 = this.editor;
        if (editor6 != null) {
            editor6.putString(this.SNAME_USER, user != null ? user.getSname() : null);
        }
        SharedPreferences.Editor editor7 = this.editor;
        if (editor7 != null) {
            editor7.putString(this.PHONE_USER, user != null ? user.getPhone() : null);
        }
        SharedPreferences.Editor editor8 = this.editor;
        if (editor8 != null) {
            editor8.putString(this.GENDER_USER, user != null ? user.getGender() : null);
        }
        SharedPreferences.Editor editor9 = this.editor;
        if (editor9 != null) {
            editor9.putString(this.BIRTHSTATE_USER, user != null ? user.getBirthState() : null);
        }
        SharedPreferences.Editor editor10 = this.editor;
        if (editor10 != null) {
            editor10.putString(this.BIRTHDATE_USER, user != null ? user.getBirthDate() : null);
        }
        SharedPreferences.Editor editor11 = this.editor;
        if (editor11 != null) {
            editor11.putString(this.OCCUPATION_USER, user != null ? user.getOcupation() : null);
        }
        SharedPreferences.Editor editor12 = this.editor;
        if (editor12 != null) {
            editor12.putString(this.PH_COUNTRY_CODE_USER, user != null ? user.getPhoneCode() : null);
        }
        SharedPreferences.Editor editor13 = this.editor;
        if (editor13 != null) {
            String str2 = this.BIRTH_COUNTRY_USER;
            if (user != null) {
                str = user.getBirthCountry();
            }
            editor13.putString(str2, str);
        }
        SharedPreferences.Editor editor14 = this.editor;
        if (editor14 != null) {
            editor14.putBoolean(this.IS_EMAIL_VERIFIED, user != null ? user.isVerified() : false);
        }
        SharedPreferences.Editor editor15 = this.editor;
        if (editor15 != null) {
            editor15.commit();
        }

        DatabaseReference updateData = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getUuid());
        updateData.setValue(user);

    }
    public final void deleteUser() {
        SharedPreferences.Editor editor1 = this.editor;
        if (editor1 != null) {
            editor1.putString(this.IMAGE_URL, null);
        }

        SharedPreferences.Editor editor2 = this.editor;
        if (editor2 != null) {
            editor2.putString(this.UUID_USER, null);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.putString(this.NAME_USER, null);
        }
        SharedPreferences.Editor editor4 = this.editor;
        if (editor4 != null) {
            editor4.putString(this.EMAIL_USER, null);
        }
        SharedPreferences.Editor editor5 = this.editor;
        if (editor5 != null) {
            editor5.putString(this.FNAME_USER, null);
        }
        SharedPreferences.Editor editor6 = this.editor;
        if (editor6 != null) {
            editor6.putString(this.SNAME_USER, null);
        }
        SharedPreferences.Editor editor7 = this.editor;
        if (editor7 != null) {
            editor7.putString(this.PHONE_USER, null);
        }
        SharedPreferences.Editor editor8 = this.editor;
        if (editor8 != null) {
            editor8.putString(this.GENDER_USER, null);
        }
        SharedPreferences.Editor editor9 = this.editor;
        if (editor9 != null) {
            editor9.putString(this.BIRTHSTATE_USER, null);
        }
        SharedPreferences.Editor editor10 = this.editor;
        if (editor10 != null) {
            editor10.putString(this.BIRTHDATE_USER, null);
        }
        SharedPreferences.Editor editor11 = this.editor;
        if (editor11 != null) {
            editor11.putString(this.OCCUPATION_USER, null);
        }
        SharedPreferences.Editor editor12 = this.editor;
        if (editor12 != null) {
            editor12.putString(this.PH_COUNTRY_CODE_USER, null);
        }
        SharedPreferences.Editor editor13 = this.editor;
        if (editor13 != null) {
            editor13.putString(this.BIRTH_COUNTRY_USER, null);
        }
        SharedPreferences.Editor editor14 = this.editor;
        if (editor14 != null) {
            editor14.putBoolean(this.IS_EMAIL_VERIFIED, false);
        }
        SharedPreferences.Editor editor15 = this.editor;
        if (editor15 != null) {
            editor15.commit();
        }
    }
}
