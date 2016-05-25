package ru.mail.ruslan.androidchatclient.msg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.mail.ruslan.androidchatclient.R;
import ru.mail.ruslan.androidchatclient.msg.response.ChannelMessage;

public class ChannelMessageAdapter extends ArrayAdapter<ChannelMessage> {
    private LayoutInflater mInflater;
    private String mUserId;

    public ChannelMessageAdapter(Context context, String userId, List<ChannelMessage> messages) {
        super(context, R.layout.item_message, messages);
        mInflater = LayoutInflater.from(context);
        mUserId = userId;
    }

    static class ViewHolder {
        ImageView image;
        ImageView myImage;
        TextView nickname;
        TextView message;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_message, null);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.myImage = (ImageView) convertView.findViewById(R.id.my_image);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChannelMessage channelMessage = getItem(position);
        if (mUserId == null || mUserId.equals(channelMessage.from)) {
            // Message from current user
            holder.image.setVisibility(View.GONE);
            holder.myImage.setVisibility(View.VISIBLE);
            holder.nickname.setText(null);
            holder.nickname.setVisibility(View.GONE);
        } else {
            // Message from another user
            holder.image.setVisibility(View.VISIBLE);
            holder.myImage.setVisibility(View.GONE);
            holder.nickname.setText(channelMessage.nick);
            holder.nickname.setVisibility(View.VISIBLE);
        }
        holder.message.setText(channelMessage.body);

        return convertView;
    }
}