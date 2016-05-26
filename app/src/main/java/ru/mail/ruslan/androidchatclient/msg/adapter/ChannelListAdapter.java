package ru.mail.ruslan.androidchatclient.msg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.mail.ruslan.androidchatclient.R;
import ru.mail.ruslan.androidchatclient.msg.response.Channel;

public class ChannelListAdapter extends ArrayAdapter<Channel> {
    private LayoutInflater mInflater;

    public ChannelListAdapter(Context context, List<Channel> channels) {
        super(context, R.layout.item_channel, channels);
        mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        ImageView image;
        TextView title;
        TextView onlineUsers;
        TextView description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_channel, null);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.onlineUsers = (TextView) convertView.findViewById(R.id.online_users);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Channel channel = getItem(position);
        holder.title.setText(channel.name);
        holder.onlineUsers.setText(Integer.toString(channel.online));
        holder.description.setText(channel.descr);

        if (channel.isEntered) {
            convertView.setBackgroundResource(R.color.colorEnteredChannel);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }
}