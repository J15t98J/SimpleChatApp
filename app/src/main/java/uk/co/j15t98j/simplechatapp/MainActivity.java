package uk.co.j15t98j.simplechatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.j15t98j.simplechatapp.dialog.PictureOptions;
import uk.co.j15t98j.simplechatapp.message.Message;
import uk.co.j15t98j.simplechatapp.message.MessageAdapter;
import uk.co.j15t98j.simplechatapp.message.MessageComparator;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;

    private RecyclerView list;
    private MessageAdapter adapter;
    private List<Message> data;

    public static File cacheDir;
    public static StorageReference pictures;
    public static DatabaseReference pictures_notify;

    public static final String author = "Me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutInflater().inflate(R.layout.activity_main, null));

        list = (RecyclerView) findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter((adapter = new MessageAdapter((data = new ArrayList<>()))));

        list.setItemViewCacheSize(30);
        list.setDrawingCacheEnabled(true);

        //region Messages
        if(database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
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
                list.scrollToPosition(data.size()-1);
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
        //endregion

        //region Profile pictures
        FirebaseStorage storage = FirebaseStorage.getInstance();
        cacheDir = this.getCacheDir();
        pictures = storage.getReferenceFromUrl("gs://simplechatapp-fd321.appspot.com");

        pictures_notify = database.getReference("picture");
        pictures_notify.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                onChildChanged(dataSnapshot, s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //endregion
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_change_picture:
                new PictureOptions().show(getFragmentManager(), "");
                return true;

            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        switch(requestCode) {
            case 1:
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                File file = new File(this.getCacheDir(), "profile_" + author);
                try {
                    FileOutputStream stream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                } catch (FileNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }
                uri = Uri.fromFile(file);

            case 2:
                uri = (uri == null)? data.getData() : uri;
                CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).setFixAspectRatio(true).setCropShape(CropImageView.CropShape.OVAL).start(this);
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    pictures.child(author + ".png").putFile(resultUri);
                    pictures_notify.child(author).setValue(new Date().getTime());
                } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                }

                break;
        }
    }
}
