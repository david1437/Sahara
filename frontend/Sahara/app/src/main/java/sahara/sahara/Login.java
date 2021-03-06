package sahara.sahara;

import android.app.SearchManager;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class Login extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.producer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.producer_login:
                startActivity(new Intent(getApplicationContext(), ProducerLogin.class));
                return true;
            case R.id.producer_register:
                startActivity(new Intent(getApplicationContext(), ProducerRegister.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("User Login");
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final Button login = (Button) findViewById(R.id.login);
        final FloatingActionButton register = (FloatingActionButton) findViewById(R.id.register);
        final Button forgot_pass = (Button) findViewById(R.id.forgot_password);

        if (PreferenceManager.getInstance(getApplicationContext()).getProducer() == 1) {
            startActivity(new Intent(getApplicationContext(), ProducerMainActivity.class));
            finish();
        }

        if (PreferenceManager.getInstance(getApplicationContext()).isUserLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
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
                                                            PreferenceManager.getInstance(getApplicationContext()).userLogin(userEmail);
                                                            Toast.makeText(
                                                                    getApplicationContext(),
                                                                    "Login Successfull",
                                                                    Toast.LENGTH_SHORT
                                                            ).show();
                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                            finish();
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
