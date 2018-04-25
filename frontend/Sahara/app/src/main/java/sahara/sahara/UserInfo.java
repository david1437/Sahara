package sahara.sahara;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class UserInfo extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    int y = -1;
    int m = -1;
    int d = -1;

    public void onDateSet(DatePicker view, int year, int month, int day) {
        y = year;
        m = month;
        d = day;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setTitle("Update User Information");
        final Button addInfo = (Button) findViewById(R.id.addinfo);
        final Button skip = (Button) findViewById(R.id.skip);
        final EditText fname = (EditText) findViewById(R.id.firstname);
        final EditText mname = (EditText) findViewById(R.id.middlename);
        final EditText lname = (EditText) findViewById(R.id.lastname);
        final Button dob = (Button) findViewById(R.id.birthday);
        final EditText address = (EditText) findViewById(R.id.address);
        final EditText phone = (EditText) findViewById(R.id.phonenumber);
        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        final EditText zipcode = (EditText) findViewById(R.id.zipcode);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        addInfo.setOnClickListener(new View.OnClickListener() {
            private void updateUserInfo(final String fn, final String mn, final String ln, final String pn, final String addr, final String zc, final long unixTime) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_UPDATEUSERINFO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")){
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Profile Update Successfull",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        if(!PreferenceManager.getInstance(getApplicationContext()).isUserLoggedIn()) {
                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                            finish();
                                        }
                                        else {
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            finish();
                                        }
                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                jsonObject.getString("message"),
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() { // If listener listens, then we had an error and we will display the msg from db.
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(error.getMessage() != null) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Check your connection or contact the administrator.",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {  // Script returned a message, now we make does variables.
                        Map<String, String> params = new HashMap<>();
                        params.put("u_email", getIntent().getStringExtra("EMAIL"));
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("u_fn", fn);
                            jo.put("u_mn", mn);
                            jo.put("u_ln", ln);
                            jo.put("u_zcode", zc);
                            jo.put("u_address", addr);
                            jo.put("u_dob", Long.toString(unixTime));
                            jo.put("u_phone_number", pn);
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                        params.put("parameters", jo.toString());
                        return params;
                    }
                };
                // This request handler keeps the internet connection until logout... Instead of attempt everytime.
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
            @Override
            public void onClick(View v) {
                if(isEmpty(fname.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "First name can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(isEmpty(mname.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Middle name can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(isEmpty(lname.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Last name can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(isEmpty(address.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Address can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(isEmpty(phone.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Phone number can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(phone.getText().toString().trim().length() > 10) {
                    Toast.makeText(getApplicationContext(), "Phone number can't be longer than 10 digits!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(isEmpty(zipcode.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Zipcode can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(zipcode.getText().toString().trim().length() > 5) {
                    Toast.makeText(getApplicationContext(), "Zipcode can't be greater than 5 digits!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(y == -1) {
                    Toast.makeText(getApplicationContext(), "Date of birth can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateUserInfo(fname.getText().toString().trim(), mname.getText().toString().trim(), lname.getText().toString().trim(),
                        phone.getText().toString().trim(), address.getText().toString().trim(), zipcode.getText().toString().trim(), new Date(y-1900,m,d).getTime() / 1000L);
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
