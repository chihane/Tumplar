package mlxy.tumplar.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ToolbarDropdownAdapter extends BaseAdapter implements ThemedSpinnerAdapter {
    private final Helper helper;
    private final String[] entries;

    public ToolbarDropdownAdapter(Context context, @ArrayRes int entries) {
        helper = new Helper(context);
        this.entries = context.getResources().getStringArray(entries);
    }

    @Override
    public int getCount() {
        return entries.length;
    }

    @Override
    public String getItem(int i) {
        return entries[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_spinner_item, viewGroup, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getItem(i));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !convertView.getTag().toString().equals("DROPDOWN")) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            convertView.setTag("DROPDOWN");
        }

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position));

        return convertView;
    }

    @Override
    public void setDropDownViewTheme(@Nullable Resources.Theme theme) {
        helper.setDropDownViewTheme(theme);
    }

    @Nullable
    @Override
    public Resources.Theme getDropDownViewTheme() {
        return helper.getDropDownViewTheme();
    }
}
