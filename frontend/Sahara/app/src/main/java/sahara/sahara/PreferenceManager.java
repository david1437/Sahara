package sahara.sahara;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by david on 3/24/18.
 */

public class PreferenceManager {
    private static PreferenceManager mInstance;
    private static Context mCtx;
    private static final String KEY_USER_EMAIL = "email";
    private static final String SHARED_PREF_NAME = "mysharedpreferences";

    private PreferenceManager(Context context) {
        mCtx = context;
    }

    public static synchronized PreferenceManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferenceManager(context);
        }
        return mInstance;
    }

    public String getLogin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public boolean userLogin(String email) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
        return true;
    }

    public boolean isUserLoggedIn() {
        SharedPreferences sp = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return (sp.getString(KEY_USER_EMAIL, null) != null);
    }

}
