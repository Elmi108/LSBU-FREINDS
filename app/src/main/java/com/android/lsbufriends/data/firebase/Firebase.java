package com.android.lsbufriends.data.firebase;

import static com.android.lsbufriends.utils.constants.NameConstants.POSTS_REF;
import static com.android.lsbufriends.utils.constants.NameConstants.USER_REF;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.lsbufriends.R;
import com.android.lsbufriends.data.model.login.LoginRequest;
import com.android.lsbufriends.data.model.post.PostRequest;
import com.android.lsbufriends.data.model.register.RegisterRequest;
import com.android.lsbufriends.data.model.user.User;
import com.android.lsbufriends.ui.login.LoginActivity;
import com.android.lsbufriends.ui.main.MainActivity;
import com.android.lsbufriends.ui.register.RegisterActivity;
import com.android.lsbufriends.ui.resetpassword.ResetPasswordActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Firebase {

    static FirebaseAuth mAuth;
    static FirebaseAuth.AuthStateListener mAuthListener;
    static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    static DatabaseReference mReference = mDatabase.getReference();

    public static void UserLoggedIn(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user= mAuth.getCurrentUser();
        if (user != null) {
            MainActivity.start(activity);
        } else {
            LoginActivity.start(activity);
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    MainActivity.start(activity);
                } else {
                    LoginActivity.start(activity);
                }
            }
        };
    }


    public static String getUID() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth.getUid();
    }

    public static void RegisterUser(RegisterActivity activity, @NonNull RegisterRequest registerRequest) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(registerRequest.getMail(), registerRequest.getPassword())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userIdd = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            String fullName = registerRequest.getFirstName() + " " + registerRequest.getLastName();
                            // Update Display Name of User
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build();
                            assert firebaseUser != null;
                            firebaseUser.updateProfile(profileChangeRequest);
                            mReference
                                    .child(USER_REF)
                                    .child(userIdd)
                                    .setValue(registerRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                firebaseUser.sendEmailVerification();
                                                activity.showSuccessSnackBar("User Registered successfully. Please Verify your gmail");
                                                LoginActivity.start(activity);
                                            } else {
                                                activity.showErrorSnackBar("User Registered failed. Please try again.");
                                            }
                                        }
                                    });

                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthUserCollisionException e) {
                                activity.showErrorSnackBar("User is already registered with this mail. Go back and Use another mail");
                            } catch (Exception e) {
                                activity.showErrorSnackBar("Error!\n" + e.getMessage());
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        activity.showErrorSnackBar("Error!\n" + e.getMessage());
                    }
                });
    }

    private static void showAlertDialog(Activity activity) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification.");

        // open email apps if user clicks/taps continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //To email app in new window and not within our app
                activity.startActivity(intent);
            }
        });

        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        //Show the AlertDialog
        alertDialog.show();
    }

    public static void LoginUser(@NonNull LoginActivity activity, @NonNull LoginRequest loginRequest) {
        mAuth = FirebaseAuth.getInstance();
        activity.showProgressDialog();
        mAuth.signInWithEmailAndPassword(loginRequest.getMail(), loginRequest.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get Instance of the current user
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            // check if email is verified before user can access their profile
                            assert firebaseUser != null;
                            if (firebaseUser.isEmailVerified()) {
                                activity.showSuccessSnackBar("You are logged in now");
                                //open dashboard
                                MainActivity.start(activity);
                                activity.overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
                                activity.finish();
                            } else {
                                firebaseUser.sendEmailVerification();
                                mAuth.signOut(); // sign out user
                                showAlertDialog(activity);
                            }
                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException e) {
                                activity.showErrorSnackBar("User does not exists or is no longer valid. Please register again");
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                activity.showErrorSnackBar("Invalid credentials. Kindly, check and re-enter.");
                            } catch (Exception e) {
                                activity.showErrorSnackBar(e.getMessage());
                            }
                        }
                        activity.dismissProgressDialog();
                    }
                });
    }

    public static void ResetUserPassword(@NonNull ResetPasswordActivity activity, String gmailStr) {
        mAuth = FirebaseAuth.getInstance();
        activity.showProgressDialog();
        mAuth.sendPasswordResetEmail(gmailStr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    activity.showSuccessSnackBar("Please check your inbox for password reset link.");
                    LoginActivity.start(activity);
                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthInvalidUserException e) {
                        activity.showErrorSnackBar("User Does not Exist or is no longer valid. Please register again.");
                    } catch (Exception e) {
                        activity.showErrorSnackBar(e.getMessage());
                    }
                }
                activity.dismissProgressDialog();
            }
        });
    }

    public static void LogoutUser(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        LoginActivity.start(activity);
    }

    private static User userModel;

    public static User getUserInfo(Activity activity) {

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        assert uid != null;
        mReference
                .child(USER_REF)
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userModel = snapshot.getValue(User.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {
                        Toast.makeText(activity, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        userModel = null;
                    }
                });


        return userModel;
    }

    public static void Post(String message, String faculty, Activity activity) {

        String postID = mReference.push().getKey();
        mAuth = FirebaseAuth.getInstance();
        String publisher = mAuth.getCurrentUser().getUid();
        String timestamp = "" + System.currentTimeMillis();
        PostRequest postRequest = new PostRequest(publisher, postID, message, timestamp,faculty);

        if (postID != null) {
            mReference.child(POSTS_REF)
                    .child(postID)
                    .setValue(postRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(activity, "Post Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, "Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
