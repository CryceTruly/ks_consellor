package com.crycetruly.keepsafeconsellor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crycetruly.keepsafeconsellor.utils.GetShortTimeAgo;
import com.crycetruly.keepsafeconsellor.utils.Handy;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter adapter;
    TextView nomsgs;
    private Context mContext;
    private RecyclerView mUserFriendsList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mUserDatabase, ChatsDb;
    private String currentUser;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            gotoLogin();
        } else {
            setuptoolbar();
            dostuff();
        }
    }

    private void setuptoolbar() {
        Log.d(TAG, "setuptoolbar: ");
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keep Safe Conselor");
        getSupportActionBar().setSubtitle("My Inbox");
    }

    private void dostuff() {
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setEnabled(false);
        mContext = getBaseContext();

        progressBar = findViewById(R.id.progress);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chat")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDatabase.keepSynced(true);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabase.keepSynced(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Handy.isNetworkAvailable(getBaseContext())) {
                    onStart();
                } else {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }


                    Snackbar.make(progressBar, "Not connected", Snackbar.LENGTH_LONG).setAction("Connect", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                        }
                    }).show();

                }

            }
        });
        //------------------------------RECYCLERVIEW    ------------------------//
        mUserFriendsList = findViewById(R.id.friendList);
        mUserFriendsList.setHasFixedSize(true);
        nomsgs = findViewById(R.id.themessos);
        mUserFriendsList.setLayoutManager(new LinearLayoutManager(mContext));


    }

    @Override
    public void onStart() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            gotoLogin();
         else


        try {
             FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("online").setValue("true");

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d(TAG, "chats" + dataSnapshot);
                    if (!dataSnapshot.exists()) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            nomsgs.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setEnabled(true);

                        } catch (NullPointerException e) {
                            swipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            nomsgs.setVisibility(View.INVISIBLE);

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (NullPointerException e) {

        }
        FirebaseRecyclerOptions<Chat> options =
                new FirebaseRecyclerOptions.Builder<Chat>()
                        .setQuery(mDatabase.orderByChild("fitnessNum"), Chat.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Chat, TrulysChatVH>(options) {
            @Override
            public TrulysChatVH onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_chat, parent, false);

                return new TrulysChatVH(view);
            }

            @Override
            protected void onBindViewHolder(final TrulysChatVH viewHolder, int position, final Chat model) {
                final String userId = getRef(position).getKey();


                viewHolder.setMessage(model.getMessage());

                long time = model.getTimestamp();
                String times = GetShortTimeAgo.getTimeAgo(time, mContext);
                viewHolder.setSentDate(times);
                Log.d(TAG, "onDataChange: This new try  one ran");

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, ChatActivity.class);
                        i.putExtra("user_id", model.getFrom());
                        startActivity(i);

                    }
                });
                mUserDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {
                            final String userName = dataSnapshot.child("phone").getValue().toString();
                            viewHolder.setName(Handy.getTrimmedName(userName));
                            //----------------------NAVIGATING TO USER PROFILE ACTIVITY------------------------//


                            swipeRefreshLayout.setEnabled(true);

                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Log.d(TAG, "onDataChange: error" + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: Cancelled");

                    }
                });

            }
        };


        mUserFriendsList.setAdapter(adapter);
        adapter.startListening();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        adapter.startListening();
        super.onStart();
    }


    @Override
    public void onStop() {
        adapter.stopListening();
        try{
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().
                    getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP);
        }catch (NullPointerException e){

        }

        super.onStop();
    }

    public static class TrulysChatVH extends RecyclerView.ViewHolder {
        View mView;

        public TrulysChatVH(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setImage(String name, Context context) {
            ImageView imageView = mView.findViewById(R.id.userpic);
            RequestOptions requestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test);
            Glide.with(context).load(name).apply(requestOptions).into(imageView);
        }

        public void setMessage(String message) {
            TextView textView = mView.findViewById(R.id.message);
            textView.setText(message);
        }

        public void setSentDate(String date) {
            TextView dateTextView = mView.findViewById(R.id.lasttime);
            dateTextView.setText(date);


        }

        public void setName(String name) {
            TextView namet = mView.findViewById(R.id.name);
            namet.setText(String.valueOf(name));


        }
    }


    private void gotoLogin() {
        Intent i = new Intent(getBaseContext(), AuthActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.logout){
            try{
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP);
            }catch (NullPointerException e){

            }
            logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent=new Intent(mContext,AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });
    }
}
