package com.timhaasdyk.mathbattle.io;

import com.timhaasdyk.mathbattle.models.Player;

/**
 * @author Tim Haasdyk on 17-May-17.
 */
public interface ActivePlayerListener {
    void onActivatePlayer(Player player);
    void onDeactivatePlayer(Player player);
}
