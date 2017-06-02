package com.timhaasdyk.mathbattle.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.ui.drawables.PlayerButton;

import java.util.List;

/**
 * @author Tim Haasdyk on 26-May-17.
 */
public class PlayerSelectionAdapter extends ArrayAdapter<Player> {

    public PlayerSelectionAdapter(Context context, @LayoutRes int resource, @NonNull List<Player> objects) {
        super(context, resource, objects);
    }

    public PlayerSelectionAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.player_selection_grid_item, null);
        }

        Player player = getItem(position);
        PlayerButton btn = (PlayerButton) v.findViewById(R.id.btnPlayer);
        btn.setTag(player.getTag());

        return v;
    }
}