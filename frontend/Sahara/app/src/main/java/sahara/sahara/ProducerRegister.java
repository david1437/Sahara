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

public class ProducerRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_register);
        setTitle("Producer Registration");

        Button register = (Button) findViewById(R.id.register);
        final EditText email = (EditText) findViewById(R.id.producer_email);
        final EditText password = (EditText) findViewById(R.id.producer_password);
        final EditText passwordAgain = (EditText) findViewById(R.id.producer_password2);

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
                if (isEmpty(password.getText().toString().trim()) || isEmpty(passwordAgain.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Password field can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().trim().length() < 8 || password.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be more than 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().trim().length() > 50 || passwordAgain.getText().toString().trim().length() > 50) {
                    Toast.makeText(getApplicationContext(), "Password must be less than 50 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!password.getText().toString().trim().equals(passwordAgain.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Passwords must be the same!", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(email.getText().toString().trim(), password.getText().toString().trim());
            }

            private void registerUser(final String uemail, final String upassword) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_REGISTERPRODUCER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")) {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                jsonObject.getString("message"),
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        Intent i = new Intent(getApplicationContext(), ProducerLogin.class);
                                        startActivity(i);
                                        finish();
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
                                if (error.getMessage() != null) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
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
                        params.put("pr_name", uemail);
                        params.put("pr_pword", upassword);
                        return params;
                    }
                };
                // This request handler keeps the internet connection until logout... Instead of attempt everytime.
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
    }
}
