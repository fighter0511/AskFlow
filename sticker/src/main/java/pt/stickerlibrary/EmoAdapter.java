package pt.stickerlibrary;

/**
 * Created by Phuc on 7/18/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class EmoAdapter extends BaseAdapter {

    Context context;
    List<Integer> listEmoticonSticker;
    LayoutInflater inflater;

    public EmoAdapter(Context context, List<Integer> listEmoticonSticker) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listEmoticonSticker = listEmoticonSticker;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    R.layout.chat_screen_sticker_set_grid_item, parent, false);

            viewHolder.imgSticker = (ImageView) convertView
                    .findViewById(R.id.imgSticker);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imgSticker.setImageResource(listEmoticonSticker.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return listEmoticonSticker.size();
    }

    @Override
    public Object getItem(int i) {
        return listEmoticonSticker.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder {
        ImageView imgSticker;
    }
}
