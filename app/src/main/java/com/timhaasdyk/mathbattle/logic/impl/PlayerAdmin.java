package com.timhaasdyk.mathbattle.logic.impl;

import android.view.ViewGroup;
import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.exceptions.PlayerNotFoundException;
import com.timhaasdyk.mathbattle.exceptions.TooManyPlayersException;
import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.models.PlayerTag;
import com.timhaasdyk.mathbattle.models.PlayerPosition;

import java.util.*;

/**
 * @author Tim Haasdyk on 11-May-17.
 */
public class PlayerAdmin {

    private Map<PlayerPosition, Player> playerSlots = new HashMap<>();
    private ViewGroup buttonGroup;

    public PlayerAdmin(ViewGroup buttonGroup) {
        for (int i = 0; i < buttonGroup.getChildCount(); i++) {
            buttonGroup.getChildAt(i).setTag(R.id.playerPosTagKey, PlayerPosition.values()[i]);
        }

        this.buttonGroup = buttonGroup;
    }

    public void setPlayer(Player player, PlayerPosition position) {
        clearPlayer(player);

        if (playerSlots.size() >= PlayerPosition.values().length) {
            throw new TooManyPlayersException(String.format(Locale.getDefault(),
                    "PlayerAdmin supports maximum %d players [%d]", PlayerPosition.values().length, playerSlots.size()));
        }

        playerSlots.put(position, player);
        buttonGroup.getChildAt(position.I).setTag(player.getTag());
    }

    public void clearPlayer(Player player) {
        PlayerPosition pos = getCurrPositionOfPlayer(player);
        if (pos != null) {
            playerSlots.remove(pos);
            buttonGroup.getChildAt(pos.I).setTag(null);
        }
    }

    private PlayerPosition getCurrPositionOfPlayer(Player player) {
        for (PlayerPosition key : playerSlots.keySet()) {
            if (playerSlots.get(key).equals(player))
                return key;
        }
        return null;
    }

    public Collection<Player> getPlayers() {
        return playerSlots.values();
    }

    public List<PlayerTag> getPlayerTags() {
        List<PlayerTag> playerTags = new ArrayList<>();
        for (Player player : playerSlots.values()) {
            playerTags.add(player.getTag());
        }
        return playerTags;
    }

    public Player getPlayerByTag(PlayerTag playerTag) {
        if (playerTag == null)
            throw new PlayerNotFoundException("PlayerTag null");
        for (Player player : playerSlots.values()) {
            if (player.getTag().equals(playerTag))
                return player;
        }
        throw new PlayerNotFoundException(String.format("Player %s not found.", playerTag));
    }

    public Player getPlayerByTag(String tag) {
        if (tag == null)
            throw new PlayerNotFoundException("PlayerTag null");
        return getPlayerByTag(new PlayerTag(tag));
    }

    public boolean playerExists(String playerTag) {
        return getPlayerTags().contains(new PlayerTag(playerTag));
    }
}