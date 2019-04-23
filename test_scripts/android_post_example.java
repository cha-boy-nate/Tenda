        
public class android_post_example {
  public static void main (String[] args) throws IOException 
{
        
        EditText email = (EditText)findViewById(R.id.email);
        EditText password = (EditText)findViewById(R.id.password);

        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        //final String result = "{\"email\":\""+emailString+"\",\"password\":\""+passwordString+"\"}";
        final Intent intent = new Intent(this, HomeActivity.class);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://34.217.162.221/test";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                String result = response.toString();
                //textView.setText("Response is: "+ response.toString());
                intent.putExtra("Test", result);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        startActivity(intent);
  }
}
