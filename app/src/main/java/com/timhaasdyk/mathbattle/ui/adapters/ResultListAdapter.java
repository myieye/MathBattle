package com.timhaasdyk.mathbattle.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.models.PlayerResult;
import com.timhaasdyk.mathbattle.ui.drawables.PlayerButton;

import java.util.List;

/**
 * @author Tim Haasdyk on 26-May-17.
 */
public class ResultListAdapter extends ArrayAdapter<PlayerResult> {

    public ResultListAdapter(Context context, @LayoutRes int resource, @NonNull List<PlayerResult> objects) {
        super(context, resource, objects);
    }

    public ResultListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.player_result_list_item, null);
        }

        PlayerResult pr = getItem(position);

        if (pr != null) {
            TextView txtRank = (TextView) v.findViewById(R.id.playerResultRank);
            TextView txtPoints = (TextView) v.findViewById(R.id.playerResultPoints);
            PlayerButton btnPlayer = (PlayerButton) v.findViewById(R.id.btnPlayer);

            if (txtRank != null) {
                txtRank.setText(String.valueOf(pr.getPosition().N));
            }

            if (txtPoints != null) {
                txtPoints.setText(String.valueOf(pr.getPoints()));
            }

            if (btnPlayer != null) {
                btnPlayer.setTag(pr.getPlayer().getTag());
            }
        }

        return v;
    }
}