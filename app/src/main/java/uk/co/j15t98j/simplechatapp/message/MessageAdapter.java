package uk.co.j15t98j.simplechatapp.message;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.j15t98j.simplechatapp.MainActivity;
import uk.co.j15t98j.simplechatapp.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> data;

    public MessageAdapter(List<Message> data) {
        this.data = data;
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

        final File file = new File(MainActivity.cacheDir, "profile_" + message.getAuthor());
        MainActivity.pictures.child(message.getAuthor() + ".png").getFile(file)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        holder.imageView.setImageURI(Uri.parse(file.toURI().toString()));
                    }
                });
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
