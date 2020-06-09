package com.example.a1stapp.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.a1stapp.R;
import com.example.a1stapp.Models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private CircleImageView image_profile;
    private TextView username;
    public View view;
    private User user = new User();
    private DatabaseReference reference;
    private Switch sw;
    private FirebaseUser fuser;
    private StorageReference storageReference;
    public static final int REQUEST_IMAGE = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews();
        initViews();
        return view;
    }

    private void findViews() {
        image_profile = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        storageReference = FirebaseStorage.getInstance().getReference().child("uploads");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid());
        sw = view.findViewById(R.id.switch1);
    }

    private void initViews() {

        //Retrieving data from database.
        //TODO: Not single because - After uploading the image, it isn't shown in the profile.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                username.setText(user.getFirstName() + " " + user.getLastName());
                if(user.getImageURL().equals("default")) {
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    if(isAdded()) {
                        Glide.with((getActivity())).load(user.getImageURL()).into(image_profile);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Switch - Read Receipts ON/OFF.
        boolean value = true; // default value if no value was found
        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("isSeenChecked", 0);
        value = sharedPreferences.getBoolean("isSeenChecked", value); // retrieve the value of your key
        sw.setChecked(value);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPreferences.edit().putBoolean("isSeenChecked", true).apply();
                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("readReceipts", true);
                    reference.updateChildren(map);
                } else {
                    sharedPreferences.edit().putBoolean("isSeenChecked", false).apply();
                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("readReceipts", false);
                    reference.updateChildren(map);
                }
            }
        });

        //Switch - Show Connection Status ON/OFF.
        Switch showStatus = view.findViewById(R.id.switch2);
        boolean signature = true;
        final SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("isOnlineChecked", 0);
        signature = sharedPreferences1.getBoolean("isOnlineChecked", signature); // retrieve the value of your key
        showStatus.setChecked(signature);
        showStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPreferences1.edit().putBoolean("isOnlineChecked", true).apply();
                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("showOnline", true);
                    reference.updateChildren(map);
                } else {
                    sharedPreferences1.edit().putBoolean("isOnlineChecked", false).apply();
                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("showOnline", false);
                    reference.updateChildren(map);
                }
            }
        });

        //Listener - Open Image.
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        reference.updateChildren(map);
                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload Is In Progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}
