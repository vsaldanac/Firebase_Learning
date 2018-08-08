package cl.vero.firebase_learning;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            startActivity(new Intent(this,ListActivity.class));

        } else {
            logUser();
        }

    }

   private void logUser(){
       startActivityForResult(
               AuthUI.getInstance()
                       .createSignInIntentBuilder()
                       .setAvailableProviders(Arrays.asList(
                               new AuthUI.IdpConfig.GoogleBuilder().build(),
                               new AuthUI.IdpConfig.FacebookBuilder().build(),
//                               new AuthUI.IdpConfig.TwitterBuilder().build(),
                               new AuthUI.IdpConfig.EmailBuilder().build()))
                       .setIsSmartLockEnabled(false)
                       .build(),
               RC_SIGN_IN);
   }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(this, ListActivity.class));
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No hay internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Error desconocido", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
