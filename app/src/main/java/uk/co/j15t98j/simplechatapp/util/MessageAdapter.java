package uk.co.j15t98j.simplechatapp.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import uk.co.j15t98j.simplechatapp.R;

public class MessageAdapter extends RecyclerView.Adapter {

    private List<Message> data;

    public MessageAdapter(List<Message> data) {
        this.data = data;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView contentView;

        ViewHolder(LinearLayout v) {
            super(v);
            item = v;

            contentView = (TextView)item.findViewById(R.id.message_content);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left, parent, false);
        return new ViewHolder((LinearLayout)v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = data.get(position);
        ((ViewHolder)holder).contentView.setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
