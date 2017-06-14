package com.timhaasdyk.mathbattle.constants;

import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.models.ColorTag;
import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.models.PlayerTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.timhaasdyk.mathbattle.util.ResourceUtil.col;
import static com.timhaasdyk.mathbattle.util.ResourceUtil.str;

/**
 * @author Tim Haasdyk on 11-May-17.
 */
public class PlayerStore {

    private static PlayerStore instance;

    private List<ColorTag> colorTags;

    private PlayerStore() {
        this.colorTags = Arrays.asList(
                new ColorTag(str(R.string.green), col(R.color.green)),
                new ColorTag(str(R.string.blue), col(R.color.blue)),
                new ColorTag(str(R.string.pink), col(R.color.pink)),
                new ColorTag(str(R.string.gold), col(R.color.gold)),
                new ColorTag(str(R.string.orange), col(R.color.orange)),
                new ColorTag(str(R.string.sky), col(R.color.sky)),
                new ColorTag(str(R.string.purple), col(R.color.purple)),
                new ColorTag(str(R.string.red), col(R.color.red)));
    }

    public static PlayerStore getInstance() {
        if (instance == null)
            instance = new PlayerStore();

        return instance;
    }

    public List<ColorTag> getColorTags() {
        return new ArrayList<>(colorTags);
    }

    public List<Player> getDefaultPlayers() {
        List<Player> players = new ArrayList<>();
        for (PlayerTag id : colorTags) {
            players.add(new Player(id, id.toString()));
        }
        return players;
    }
}