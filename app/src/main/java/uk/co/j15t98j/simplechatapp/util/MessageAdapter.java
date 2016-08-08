package uk.co.j15t98j.simplechatapp.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import uk.co.j15t98j.simplechatapp.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> data;

    public MessageAdapter(List<Message> data) {
        this.data = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView contentView;

        ViewHolder(LinearLayout v) {
            super(v);
            item = v;

            contentView = (TextView)item.findViewById(R.id.message_content);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate((viewType == 1? R.layout.message_right : R.layout.message_left), parent, false);
        return new ViewHolder((LinearLayout)v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = data.get(position);
        holder.contentView.setText(message.getContent());
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
