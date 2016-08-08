package uk.co.j15t98j.simplechatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.j15t98j.simplechatapp.util.Message;
import uk.co.j15t98j.simplechatapp.util.MessageAdapter;
import uk.co.j15t98j.simplechatapp.util.MessageComparator;
import uk.co.j15t98j.simplechatapp.util.MessageType;

public class MainActivity extends AppCompatActivity {

    private MessageAdapter adapter;

    private List<Message> data;

    public static final String author = "Me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutInflater().inflate(R.layout.activity_main, null));

        RecyclerView list = (RecyclerView) findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter((adapter = new MessageAdapter((data = new ArrayList<>()))));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        final DatabaseReference messages = database.getReference("message");
        messages.keepSynced(true);

        findViewById(R.id.messageSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = ((EditText)findViewById(R.id.editMessage)).getText().toString();
                ((EditText)findViewById(R.id.editMessage)).setText("");

                Map<String, Object> children = new HashMap<>();
                children.put("author", author);
                children.put("content", content);
                children.put("timestamp", new Date().getTime());

                DatabaseReference message = FirebaseDatabase.getInstance().getReference("message").push();
                message.updateChildren(children);
            }
        });

        messages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                data.add(new Message(dataSnapshot));
                Collections.sort(data, new MessageComparator());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                data.remove(new Message(dataSnapshot));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails());
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;

            default:
                return false;
        }
    }
}
