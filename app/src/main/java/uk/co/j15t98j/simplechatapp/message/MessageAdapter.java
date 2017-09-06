package uk.co.j15t98j.simplechatapp.message;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.j15t98j.simplechatapp.MainActivity;
import uk.co.j15t98j.simplechatapp.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> data;
    public Map<String, Bitmap> ppics;
    public Map<String, Long> ppic_changeNumbers;

    public MessageAdapter(List<Message> data) {
        this.data = data;
        ppics = new HashMap<>();
        ppic_changeNumbers = new HashMap<>();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView contentView;
        CircleImageView imageView;

        ViewHolder(LinearLayout v) {
            super(v);
            item = v;

            contentView = (TextView)item.findViewById(R.id.message_content);
            imageView = (CircleImageView)item.findViewById(R.id.message_picture);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate((viewType == 1? R.layout.message_right : R.layout.message_left), parent, false);
        return new ViewHolder((LinearLayout)v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Message message = data.get(position);
        holder.contentView.setText(message.getContent());

        loadPic(holder, message.getAuthor());
    }

    private void loadPic(final ViewHolder holder, final String author) {
        String path = "profile_" + author;
        if(!ppics.containsKey(path)) {
            File localCacheFile = new File(MainActivity.cacheDir, path);
            if(localCacheFile.exists()) {
                ppics.put(path, BitmapFactory.decodeFile(localCacheFile.getAbsolutePath()));
            } else {
                StorageReference cloudRef = MainActivity.pictures.child(author);
                List<FileDownloadTask> tasks = cloudRef.getActiveDownloadTasks();
                (tasks.size() > 0? tasks.get(0) : cloudRef.getFile(localCacheFile)) // Use existing download task if it exists, else create new one
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                notifyDataSetChanged(); // Call update again; this time the file will exist in local cache
                            }
                        });
                return;
            }
        }
        holder.imageView.setImageBitmap(ppics.get(path));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType() == MessageType.SENT? 1 : 0;
    }
}
