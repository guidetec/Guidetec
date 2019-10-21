package guidetec.com.guidetec.classes;

public final class User {
    private String birthCountry;
    private String birthDate;
    private String birthState;
    private String email;
    private String fname;
    private String gender;
    private boolean isVerified;
    private String name;
    private String ocupation;
    private String phone;
    private String phoneCode;
    private String sname;
    private String uuid;
    private String imageUrl;
    private String base64;


    public final String getImageUrl() {
        return this.imageUrl;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public final void setImageUrl(String str) {
        this.imageUrl = str;
    }


    public final String getUuid() {
        return this.uuid;
    }

    public final void setUuid(String str) {
        this.uuid = str;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String str) {
        this.name = str;
    }

    public final String getFname() {
        return this.fname;
    }

    public final void setFname( String str) {
        this.fname = str;
    }

    public final String getSname() {
        return this.sname;
    }

    public final void setSname(String str) {
        this.sname = str;
    }

    public final String getPhone() {
        return this.phone;
    }

    public final void setPhone(String str) {
        this.phone = str;
    }

    public final String getPhoneCode() {
        return this.phoneCode;
    }

    public final void setPhoneCode( String str) {
        this.phoneCode = str;
    }

    public final String getGender() {
        return this.gender;
    }

    public final void setGender(String str) {
        this.gender = str;
    }

    public final String getBirthState() {
        return this.birthState;
    }

    public final void setBirthState( String str) {
        this.birthState = str;
    }


    public final String getBirthCountry() {
        return this.birthCountry;
    }

    public final void setBirthCountry(String str) {
        this.birthCountry = str;
    }


    public final String getBirthDate() {
        return this.birthDate;
    }

    public final void setBirthDate(String str) {
        this.birthDate = str;
    }

    public final String getEmail() {
        return this.email;
    }

    public final void setEmail(String str) {
        this.email = str;
    }

    public final String getOcupation() {
        return this.ocupation;
    }

    public final void setOcupation(String str) {
        this.ocupation = str;
    }

    public final boolean isVerified() {
        return this.isVerified;
    }

    public final void setVerified(boolean z) {
        this.isVerified = z;
    }

    public User() {
        String str = "";
        this.uuid = str;
        this.name = str;
        this.fname = str;
        this.sname = str;
        this.phone = str;
        this.phoneCode = str;
        this.gender = str;
        this.birthState = str;
        this.birthCountry = str;
        this.birthDate = str;
        this.email = str;
        this.ocupation = str;
        this.imageUrl=str;
        this.base64=str;
    }

    public User(String uuid, String name, String fname, String sname, String phone, String phoneCode, String gender,
                String birthState, String birthCountry, String birthDate, String email, String ocupation) {
        String empty = "";
        this.uuid = empty;
        this.name = empty;
        this.fname = empty;
        this.sname = empty;
        this.phone = empty;
        this.phoneCode = empty;
        this.gender = empty;
        this.birthState = empty;
        this.birthCountry = empty;
        this.birthDate = empty;
        this.email = empty;
        this.ocupation = empty;

        this.uuid = uuid;
        this.name = name;
        this.fname = fname;
        this.sname = sname;
        this.gender = gender;
        this.phone = phone;
        this.birthState = birthState;
        this.birthDate = birthDate;
        this.email = email;
        this.ocupation = ocupation;
        this.phoneCode = phoneCode;
        this.birthCountry = birthCountry;
    }

   /* public User(JSONObject jSONObject) {
        String str = "";
        this.uuid = str;
        this.name = str;
        this.fname = str;
        this.sname = str;
        this.phone = str;
        this.phoneCode = str;
        this.gender = str;
        this.birthState = str;
        this.birthCountry = str;
        this.birthDate = str;
        this.email = str;
        this.ocupation = str;
        this.uuid = ExtensionsKt.optStringNull(jSONObject, "uuid");
        this.name = ExtensionsKt.optStringNull(jSONObject, "gname");
        this.fname = ExtensionsKt.optStringNull(jSONObject, "fname");
        this.sname = ExtensionsKt.optStringNull(jSONObject, "sfname");
        this.gender = ExtensionsKt.optStringNull(jSONObject, "gender");
        this.phone = ExtensionsKt.optStringNull(jSONObject, "phone_primary");
        this.birthState = ExtensionsKt.optStringNull(jSONObject, "birth_state");
        this.birthCountry = ExtensionsKt.optStringNull(jSONObject, "birth_country_code");
        this.birthDate = ExtensionsKt.optStringNull(jSONObject, "birth_date");
        this.email = ExtensionsKt.optStringNull(jSONObject, "email_primary");
        this.ocupation = ExtensionsKt.optStringNull(jSONObject, "occupation");
        this.phoneCode = ExtensionsKt.optStringNull(jSONObject, "phone_country_code");
        this.isVerified = jSONObject != null ? jSONObject.optBoolean("email_verified") : false;
    }*/
}
