package sahara.sahara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final Button login = (Button) findViewById(R.id.login);
        final Button register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(email.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Email field can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Email must be more than 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().trim().length() > 50) {
                    Toast.makeText(getApplicationContext(), "Email must be less than 50 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmpty(password.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Password field can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be more than 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().trim().length() > 50) {
                    Toast.makeText(getApplicationContext(), "Password must be less than 50 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                register(email.getText().toString().trim(), password.getText().toString().trim());
            }
            private void register(final String userEmail, final String userPassword) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_REGISTER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")){
                                        startActivity(new Intent(getApplicationContext(), UserInfo.class));
                                        PreferenceManager.getInstance(getApplicationContext()).userLogin(userEmail);
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Registration Successfull",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        finish();
                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Registration Unsucessfull!",
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
                        params.put("u_email", userEmail);
                        params.put("u_pword", userPassword);
                        return params;
                    }
                };
                // This request handler keeps the internet connection until logout... Instead of attempt everytime.
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getInstance(getApplicationContext()).isUserLoggedIn()) {
                    //startActivity(new Intent(getApplicationContext(), Register.class));
                    //finish();
                }
                if (isEmpty(email.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Email field can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Email must be more than 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().trim().length() > 50) {
                    Toast.makeText(getApplicationContext(), "Email must be less than 50 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmpty(password.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Password field can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be more than 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().trim().length() > 50) {
                    Toast.makeText(getApplicationContext(), "Password must be less than 50 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                login(email.getText().toString().trim(), password.getText().toString().trim());
            }

            private void login(final String userEmail, final String userPassword) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")){
                                        // startActivity(new Intent(getApplicationContext(), Login.class));
                                        PreferenceManager.getInstance(getApplicationContext()).userLogin(userEmail);
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Login Successfull",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        // finish();
                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Login Un-Successfull Username or Password Incorrect",
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
                        params.put("u_email", userEmail);
                        params.put("u_pword", userPassword);
                        return params;
                    }
                };
                // This request handler keeps the internet connection until logout... Instead of attempt everytime.
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
    }
}
