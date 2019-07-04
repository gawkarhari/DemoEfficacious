package com.mobi.efficacious.demoefficacious.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.LoginDetail;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class Gmail_login extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,  View.OnClickListener {
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    String roleid,User_id,Academic_id,Schooli_id;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressBar;
    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmail_login);
        cd = new ConnectionDetector(getApplicationContext());
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        roleid = settings.getString("TAG_USERTYPEID", "");
        User_id = settings.getString("TAG_USERID", "");
        Academic_id= settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id= settings.getString("TAG_SCHOOL_ID", "");


        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Gmail_login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String email= user.getEmail();

            String command="";
            String Firebasetoken = FirebaseInstanceId.getInstance().getToken();
            settings.edit().putString("TAG_USERFIREBASETOKEN",Firebasetoken).commit();
            settings.edit().putString("TAG_USEREMAILID",email).commit();
            switch (Integer.parseInt(roleid))
            {
                case 1:command="FcmTokenStudent";
                    break;
                case  2:command="FcmTokenStudent";
                    break;
                case 3:command="FcmTokenTeacher";
                    break;
                case 4:command="FcmTokenStaff";
                    break;
                case 5:command="FcmTokenAdmin";
                    break;
                case 6:command="FcmTokenPrincipal";
                    break;
                case 7:command="FcmTokenManager";
                    break;
            }
            if (!cd.isConnectingToInternet())
            {

                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK",null);
                alert.show();

            }
            else {

               FCMTOKENASYNC(command, Firebasetoken, email);

            }

        }

    }

        public void FCMTOKENASYNC(String command,String Firebasetoken,String EMail) {
            try {
                progressBar = new ProgressDialog(Gmail_login.this);
                progressBar.setCancelable(false);
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.setMessage("loading...");
                DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
                LoginDetail loginDetail=new LoginDetail(Firebasetoken,EMail, Integer.parseInt(User_id), Integer.parseInt(Schooli_id), Integer.parseInt(Academic_id));
                Observable<ResponseBody> call = service.FCMTokenUpdate(command,loginDetail);
                call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        progressBar.show();
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        try {

                        } catch (Exception ex) {
                            progressBar.dismiss();
                            Toast.makeText(Gmail_login.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        progressBar.dismiss();
                        Toast.makeText(Gmail_login.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                        progressBar.dismiss();
                        Toast.makeText(Gmail_login.this, "Gmail Verified Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Gmail_login.this, MainActivity.class));
                        finish();
                    }
                });
            } catch (Exception ex) {

            }
        }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }
    }


}