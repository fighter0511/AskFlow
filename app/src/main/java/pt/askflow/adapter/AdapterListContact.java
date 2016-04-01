package pt.askflow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import pt.askflow.R;
import pt.askflow.util.Contact;


/**
 * Created by PhucThanh on 1/20/2016.
 */
public class AdapterListContact extends BaseAdapter {

    private Context context;
    private List<Contact> contacts;

    public AdapterListContact(Context context, List<Contact> contacts){
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgAvatar;
        TextView tvUsername;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_list_contact, parent, false);
            holder = new ViewHolder();
            holder.tvUsername = (TextView) convertView.findViewById(R.id.tv_username);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        Contact item = contacts.get(position);
        holder.tvUsername.setText(item.getName());

        String firstLetter = String.valueOf(item.getName().charAt(0));
        ColorGenerator generator = ColorGenerator.DEFAULT;
        int color = generator.getColor(item);
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);
        holder.imgAvatar.setImageDrawable(drawable);
        return convertView;
    }
}
