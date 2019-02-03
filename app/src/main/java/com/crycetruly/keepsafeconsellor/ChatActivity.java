package com.crycetruly.keepsafeconsellor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crycetruly.keepsafeconsellor.utils.GetCurTime;
import com.crycetruly.keepsafeconsellor.utils.GetShortTimeAgo;
import com.crycetruly.keepsafeconsellor.utils.Handy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.media.MediaRecorder.VideoSource.CAMERA;


public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private final List<Message> messagesList = new ArrayList<>();
    ImageView user_image;
    TextView title;
    //Add Emojicon
    EditText emojiconEditText;
    RelativeLayout chating;
    private String mChatUser;
    private Toolbar mChatToolbar;
    private DatabaseReference mRootRef;
    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private DatabaseReference mUserRf;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;
    private RecyclerView mMessagesList;
    private LinearLayoutManager mLinearLayout;
    private MessagesTestAdapter mAdapter;
    private ImageButton add;
    private Context mContext;
    private String currentUserDb;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private FirebaseFirestore firebaseFirestore;

    //----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = ChatActivity.this;
        linearLayout = findViewById(R.id.linearLayout);
        relativeLayout = findViewById(R.id.noaccess);
        mChatToolbar = findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chating = findViewById(R.id.chating);
        mChatMessageView = findViewById(R.id.emojicon_edit_text);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mChatSendBtn = findViewById(R.id.chat_send_btn);
        mLastSeenView = findViewById(R.id.custom_bar_seen);
        mProfileImage = findViewById(R.id.custom_bar_image);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mUserRf = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUserId);
        mUserRf.child("online").setValue("true");
        final String user_id = getIntent().getStringExtra("user_id");
        mChatUser = user_id;
//--------------CHANGE THE READ STATUS------------------
        currentUserDb = "Users";


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_barr, null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(action_bar_view);
        actionBar.setDisplayHomeAsUpEnabled(true);


        // ---- Custom Action bar Items ----
        final TextView mTitleView = findViewById(R.id.bbbb);
        final TextView mLastSeenView = findViewById(R.id.custom_bar_seen);


        mAdapter = new MessagesTestAdapter(mContext, messagesList);

        mMessagesList = findViewById(R.id.messages_list);
        // swipeRefreshLayout = findViewById(R.id.swipe_layout);
        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);

        loadMessages();
        final DatabaseReference firebaseFirestore = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore.child("Users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String name = dataSnapshot.child("phone").getValue().toString();
                    mTitleView.setText(Handy.getTrimmedName(name));
                    String online = dataSnapshot.child("online").getValue().toString();

                    if (online.equals("online")) {

                        mLastSeenView.setText("Active now");

                    } else {

                        //                  TODO FIX AFTER CREATING  CLOUD UNCTION TO BASICALLY UPDATE THE DOCUMENYT DATA WITH CORRECT VALUE
                        mLastSeenView.setText("");
try {
    long lastTime = Long.parseLong(online);


    String lastSeenTime = GetShortTimeAgo.getTimeAgo(lastTime, getApplicationContext());
    if (!lastSeenTime.equals("")) {
        mLastSeenView.setText("Active " + lastSeenTime + " ago");
    }

}catch (Exception e){

}
                    }
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mChatMessageView.getText().toString();
                if (!message.equals("")) {
                    sendMessage(message, "text");
                }


            }
        });

    }


    private void loadMessages() {

        firebaseFirestore.collection("messages").document(mCurrentUserId).collection(mChatUser)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            Message message = doc.getDocument().toObject(Message.class);

                            messagesList.add(message);

                            mAdapter.notifyDataSetChanged();
                            mMessagesList.scrollToPosition(messagesList.size() - 1);
                        }
                    }
                });
    }

    private void sendMessage(String message, String type) {
        Log.d(TAG, "sendMessage: sending " + message + " of type " + type);
        String mes = message.trim();
        if (!TextUtils.isEmpty(mes)) {
            String currentDate = DateFormat.getDateTimeInstance().format(new Date());
            String current_user_ref = mCurrentUserId + "/" + mChatUser;
            String chat_user_ref = mChatUser + "/" + mCurrentUserId;
            DatabaseReference user_message_push = mRootRef.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();
            DatabaseReference newNotificationref = mRootRef.child("messageNotifications")
                    .child(mCurrentUserId).child(mChatUser).push();
            String newNotificationId = newNotificationref.getKey();
            HashMap<String, String> notificationData = new HashMap<>();
            notificationData.put("from", mCurrentUserId);
            notificationData.put("message", mes);
            String push_id = user_message_push.getKey();
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("message", mes);
            messageMap.put("seen", false);
            messageMap.put("type", type);
            messageMap.put("userName", FirebaseAuth.getInstance().getCurrentUser().getEmail());
            messageMap.put("sendDate", currentDate);
            messageMap.put("time", System.currentTimeMillis());
            messageMap.put("from", mCurrentUserId);
            messageMap.put("owner", "counsellor");
            messageMap.put("reversedTime", GetCurTime.getReversedNow());
            firebaseFirestore.collection("messages").document(mCurrentUserId).collection(mChatUser).document(push_id).set(messageMap);
            firebaseFirestore.collection("messages").document(mChatUser).collection(mCurrentUserId).document(push_id).set(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "onComplete: success sending message");
                }
            });


            Map noti = new HashMap();
            noti.put(newNotificationId, notificationData);
            firebaseFirestore.collection("messageNotifications").document(mChatUser).set(noti);
            mChatMessageView.setText("");
            //-------------------------------CREATION OF THE CHAT NODE TO QUERY FOR THE CHATS ACTIVITY--------------------------//
            //-------------------------------CREATION OF THE CHAT NODE TO QUERY FOR THE CHATS ACTIVITY--------------------------//
            Map<String, Object> chatAddMap = new HashMap<>();
            chatAddMap.put("fitnessNum", Handy.fitnessNumber());
            chatAddMap.put("message", mes);
            chatAddMap.put("seen", false);
            chatAddMap.put("type", type);
            chatAddMap.put("sendDate", currentDate);
            chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
            chatAddMap.put("from", mCurrentUserId);
            chatAddMap.put("phone", mAuth.getCurrentUser().getEmail());
            //todo find aaway to enhance loading,possibly update the chat nodde with last info
            Map<String, Object> chatUserMap = new HashMap<String, Object>();
            chatUserMap.put("chat/" + mCurrentUserId + "/" + mChatUser, chatAddMap);
            chatUserMap.put("chat/" + mChatUser + "/" + mCurrentUserId, chatAddMap);
        //todo
        //fix the carsing of usiners
           FirebaseDatabase.getInstance().getReference().child("chat").child(mCurrentUserId)
           .child(mChatUser).updateChildren(chatAddMap);
           FirebaseDatabase.getInstance().getReference().child("chat").child(mChatUser)
           .child(mCurrentUserId).updateChildren(chatAddMap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
        } else {
            reference.child(currentUser.getUid()).child("online").setValue("true");
            //--------------------------------------------
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
        } else {
            reference.child(currentUser.getUid()).child("online").setValue(System.currentTimeMillis());
            //--------------------------------------------
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
