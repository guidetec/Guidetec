package guidetec.com.guidetec.framework.shareddata;

import android.content.Context;
import android.content.SharedPreferences;

import guidetec.com.guidetec.classes.Tags;

public final class SignUpValues {
    private static final String BIRTH_COUNTRY = "birthCountry";
    private static final String BIRTH_DATE = "birthDate";
    private static final String BIRTH_STATE = "birthState";
    private static final String CITY_ID = "cityId";
    private static final String CITY_STATE = "cityState";
    private static final String COUNTRY_ID = "countryId";
    private static final String COUNTRY_SHIP = "countryShip";
    private static final String DOCUMENT_INDEX = "documentIndex";
    private static final String EMAIL_USER = "emailUser";
    private static final String EXTRA_INSTRUCTIONS = "extraInstructions";
    private static final String EXT_NUMBER = "extNumber";
    private static final String FIRSTBASE_64 = "firstBase64";
    private static final String FNAME_USER = "fnameUser";
    private static final String GENDER_USER = "genderUser";
    private static final String GNAME_USER = "gnameUser";
    private static final String INT_NUMBER = "intNumber";
    private static final String INVIATION_CODE = "invitationCode";
    private static final String INVITATION_INDEX = "invitationIndex";
    private static final String LOCATION_SELECTED = "locationSelected";
    private static final String OCCUPATION_USER = "occupationUser";
    private static final String PHONE_CODE = "phoneCode";
    private static final String PHONE_USER = "phoneUser";
    private static final String PIN_USER = "pinUser";
    private static final String PREUUID_USER = "preuuidUser";
    private static final String PROFILE_B64 = "profileBase64";
    private static final String PROFILE_PATH = "profilePath";
    private static final String SECONDBASE_64 = "secondBase64";
    private static final String SMS_CODE = "smsCode";
    private static final String SNAME_USER = "snameUser";
    private static final String STATE_COUNTRY = "stateCountry";
    private static final String STATE_ID = "stateId";
    private static final String STREET_TOWN = "streetTown";
    private static final String TOWN_CITY = "townCity";
    private static final String ZIP_NUMBER = "zipNumber";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SignUpValues(Context context){
        SharedPreferences.Editor editor2 = null;
        this.sharedPreferences = context != null ? context.getSharedPreferences(Tags.NAME_PREFERENCE, 0) : null;
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        if (sharedPreferences2 != null) {
            editor2 = sharedPreferences2.edit();
        }
        this.editor = editor2;
    }

    public final String getCountry() {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        String str = "";
        if (sharedPreferences2 != null) {
            String string = sharedPreferences2.getString(COUNTRY_SHIP, str);
            if (string != null) {
                return string;
            }
        }
        return str;
    }
    public final String getEmail() {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        String str = "";
        if (sharedPreferences2 != null) {
            String string = sharedPreferences2.getString(EMAIL_USER, str);
            if (string != null) {
                return string;
            }
        }
        return str;
    }

    public final void setEmail( String str) {
        SharedPreferences.Editor editor2 = this.editor;
        if (editor2 != null) {
            editor2.putString(EMAIL_USER, str);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.commit();
        }
    }
    public final String getGender() {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        String str = "";
        if (sharedPreferences2 != null) {
            String string = sharedPreferences2.getString(GENDER_USER, str);
            if (string != null) {
                return string;
            }
        }
        return str;
    }

    public final void setGender(String str) {
        SharedPreferences.Editor editor2 = this.editor;
        if (editor2 != null) {
            editor2.putString(GENDER_USER, str);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.commit();
        }
    }
    public final String getPhone() {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        String str = "";
        if (sharedPreferences2 != null) {
            String string = sharedPreferences2.getString(PHONE_USER, str);
            if (string != null) {
                return string;
            }
        }
        return str;
    }

    public final void setPhone(String str) {
        SharedPreferences.Editor editor2 = this.editor;
        if (editor2 != null) {
            editor2.putString(PHONE_USER, str);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.commit();
        }
    }
    public final String getFname() {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        String str = "";
        if (sharedPreferences2 != null) {
            String string = sharedPreferences2.getString(FNAME_USER, str);
            if (string != null) {
                return string;
            }
        }
        return str;
    }

    public final void setFname(String str) {
        SharedPreferences.Editor editor2 = this.editor;
        if (editor2 != null) {
            editor2.putString(FNAME_USER, str);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.commit();
        }
    }

    public final void removeSignUpValues() {
        SharedPreferences.Editor editor2 = this.editor;
        String str = "";
        if (editor2 != null) {
            editor2.putString(PHONE_CODE, str);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.putString(INVIATION_CODE, str);
        }
        SharedPreferences.Editor editor4 = this.editor;
        if (editor4 != null) {
            editor4.putString(EMAIL_USER, str);
        }
        SharedPreferences.Editor editor5 = this.editor;
        if (editor5 != null) {
            editor5.putString(GNAME_USER, str);
        }
        SharedPreferences.Editor editor6 = this.editor;
        if (editor6 != null) {
            editor6.putString(FNAME_USER, str);
        }
        SharedPreferences.Editor editor7 = this.editor;
        if (editor7 != null) {
            editor7.putString(SNAME_USER, str);
        }
        SharedPreferences.Editor editor8 = this.editor;
        if (editor8 != null) {
            editor8.putString(GENDER_USER, str);
        }
        SharedPreferences.Editor editor9 = this.editor;
        if (editor9 != null) {
            editor9.putString(BIRTH_DATE, str);
        }
        SharedPreferences.Editor editor10 = this.editor;
        if (editor10 != null) {
            editor10.putString(BIRTH_STATE, str);
        }
        SharedPreferences.Editor editor11 = this.editor;
        if (editor11 != null) {
            editor11.putString(BIRTH_COUNTRY, str);
        }
        SharedPreferences.Editor editor12 = this.editor;
        if (editor12 != null) {
            editor12.putString(LOCATION_SELECTED, str);
        }
        SharedPreferences.Editor editor13 = this.editor;
        if (editor13 != null) {
            editor13.putString(OCCUPATION_USER, str);
        }
        SharedPreferences.Editor editor14 = this.editor;
        if (editor14 != null) {
            editor14.putString(SMS_CODE, str);
        }
        SharedPreferences.Editor editor15 = this.editor;
        if (editor15 != null) {
            editor15.putString(PHONE_USER, str);
        }
        SharedPreferences.Editor editor16 = this.editor;
        if (editor16 != null) {
            editor16.putString(PREUUID_USER, str);
        }
        SharedPreferences.Editor editor17 = this.editor;
        if (editor17 != null) {
            editor17.putString(COUNTRY_SHIP, str);
        }
        SharedPreferences.Editor editor18 = this.editor;
        if (editor18 != null) {
            editor18.putString(COUNTRY_ID, str);
        }
        SharedPreferences.Editor editor19 = this.editor;
        if (editor19 != null) {
            editor19.putString(STATE_COUNTRY, str);
        }
        SharedPreferences.Editor editor20 = this.editor;
        if (editor20 != null) {
            editor20.putString(STATE_ID, str);
        }
        SharedPreferences.Editor editor21 = this.editor;
        if (editor21 != null) {
            editor21.putString(CITY_STATE, str);
        }
        SharedPreferences.Editor editor22 = this.editor;
        if (editor22 != null) {
            editor22.putString(CITY_ID, str);
        }
        SharedPreferences.Editor editor23 = this.editor;
        if (editor23 != null) {
            editor23.putString(TOWN_CITY, str);
        }
        SharedPreferences.Editor editor24 = this.editor;
        if (editor24 != null) {
            editor24.putString(STREET_TOWN, str);
        }
        SharedPreferences.Editor editor25 = this.editor;
        if (editor25 != null) {
            editor25.putString(EXT_NUMBER, str);
        }
        SharedPreferences.Editor editor26 = this.editor;
        if (editor26 != null) {
            editor26.putString(INT_NUMBER, str);
        }
        SharedPreferences.Editor editor27 = this.editor;
        if (editor27 != null) {
            editor27.putString(ZIP_NUMBER, str);
        }
        SharedPreferences.Editor editor28 = this.editor;
        if (editor28 != null) {
            editor28.putString(PIN_USER, str);
        }
        SharedPreferences.Editor editor29 = this.editor;
        if (editor29 != null) {
            editor29.putString(DOCUMENT_INDEX, str);
        }
        SharedPreferences.Editor editor30 = this.editor;
        if (editor30 != null) {
            editor30.putString(INVITATION_INDEX, str);
        }
        SharedPreferences.Editor editor31 = this.editor;
        if (editor31 != null) {
            editor31.putString(FIRSTBASE_64, str);
        }
        SharedPreferences.Editor editor32 = this.editor;
        if (editor32 != null) {
            editor32.putString(SECONDBASE_64, str);
        }
        SharedPreferences.Editor editor33 = this.editor;
        if (editor33 != null) {
            editor33.commit();
        }
    }

    public final void setProfilePic(String str) {
        SharedPreferences.Editor editor2 = this.editor;
        if (editor2 != null) {
            editor2.putString(PROFILE_B64, str);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.commit();
        }
    }
    public final void removeProfilePicture() {
        SharedPreferences.Editor editor2 = this.editor;
        String str = "";
        if (editor2 != null) {
            editor2.putString(PROFILE_B64, str);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.putString(PROFILE_PATH, str);
        }
        SharedPreferences.Editor editor4 = this.editor;
        if (editor4 != null) {
            editor4.commit();
        }
    }
    public final String getProfileB64() {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        String str = "";
        if (sharedPreferences2 != null) {
            String string = sharedPreferences2.getString(PROFILE_B64, str);
            if (string != null) {
                return string;
            }
        }
        return str;
    }

    public final void setProfilePath(String str) {
        SharedPreferences.Editor editor2 = this.editor;
        if (editor2 != null) {
            editor2.putString(PROFILE_PATH, str);
        }
        SharedPreferences.Editor editor3 = this.editor;
        if (editor3 != null) {
            editor3.commit();
        }
    }
    public final String getProfilePath() {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        String str = "";
        if (sharedPreferences2 != null) {
            String string = sharedPreferences2.getString(PROFILE_PATH, str);
            if (string != null) {
                return string;
            }
        }
        return str;
    }
    public final void clearSignupVariables() {
        String str = "";
        //setPhCode(str);
        //setInvitationCode(str);
        //setGname(str);
        setFname(str);
        //setSFname(str);
        setGender(str);
        //setBstate(str);
        //setBdate(str);
        //setLocationSelected(str);
       // setOccupation(str);
        setPhone(str);
        /*setSmScode(str);
        setZip(str);
        setCountry(str);
        setState(str);
        setCity(str);
        setTown(str);
        setStreet(str);
        setNumext(str);
        setNumint(str);*/
        setProfilePath(str);
        setProfilePic(str);
    }
}
