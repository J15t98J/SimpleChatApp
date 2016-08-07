package uk.co.j15t98j.simplechatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.j15t98j.simplechatapp.util.Message;
import uk.co.j15t98j.simplechatapp.util.MessageAdapter;
import uk.co.j15t98j.simplechatapp.util.MessageType;

public class MainActivity extends AppCompatActivity {

    private RecyclerView list;
    private MessageAdapter adapter;

    private List<Message> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutInflater().inflate(R.layout.activity_main, null));

        list = (RecyclerView)findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter((adapter = new MessageAdapter((data = new ArrayList<>()))));

        data.add(new Message("Hello world!", null, MessageType.SENT));
        data.add(new Message("I hear you!", null, MessageType.RECEIVED));
        adapter.notifyDataSetChanged();
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
