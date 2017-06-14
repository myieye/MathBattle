package com.timhaasdyk.mathbattle.ui.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.models.PlayerTag;
import com.timhaasdyk.mathbattle.logic.impl.PlayerAdmin;
import com.timhaasdyk.mathbattle.models.PlayerPosition;
import com.timhaasdyk.mathbattle.ui.drawables.PlayerButton;

import java.util.List;

/**
 * @author Tim Haasdyk on 18-May-17.
 */
public class PlayerButtonUtil {

    private static Activity activity;
    private static Animation shake;
    private static Animation squeeze;
    private static ViewGroup playerBtns;

    public static void init(Activity activity) {
        PlayerButtonUtil.activity = activity;
        shake = AnimationUtils.loadAnimation(activity, R.anim.shake_player_btns);
        squeeze = AnimationUtils.loadAnimation(activity, R.anim.squeeze);
        playerBtns = (ViewGroup) activity.findViewById(R.id.buttonGroup);
    }

    public static void activateButtonForPlayer(Player player) {
        PlayerButton btn = getPlayerButtonByPlayerTag(player.getTag());
        if (btn != null) btn.setActivated(true);
    }

    public static void deactivateButtonForPlayer(Player player) {
        PlayerButton btn = getPlayerButtonByPlayerTag(player.getTag());
        if (btn != null) btn.setActivated(false);
    }

    public static void squeezePlayerButton(Player player) {
        PlayerButton btn = getPlayerButtonByPlayerTag(player.getTag());
        if (btn != null) btn.startAnimation(squeeze);
    }

    public static void showAllButtons() {
        for (int i = 0; i < playerBtns.getChildCount(); i++) {
            View btn = playerBtns.getChildAt(i);
            btn.setVisibility(View.VISIBLE);
        }
    }

    public static void hideUnusedButtons() {
        for (int i = 0; i < playerBtns.getChildCount(); i++) {
            View btn = playerBtns.getChildAt(i);
            if (btn.getTag() == null || !(btn.getTag() instanceof PlayerTag))
                btn.setVisibility(View.INVISIBLE);
        }
    }

    public static long shakePlayerButtons() {
        playerBtns.startAnimation(shake);
        return shake.getDuration() * shake.getRepeatCount();
    }

    public static void setTestPlayerButtons(PlayerAdmin playerAdmin, List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            playerAdmin.setPlayer(
                    players.get(i),
                    (PlayerPosition) playerBtns.getChildAt(i).getTag(R.id.playerPosTagKey));
        }
    }

    private static PlayerButton getPlayerButtonByPlayerTag(PlayerTag tag) {
        for (int i = 0; i < playerBtns.getChildCount(); i++) {
            View btn = playerBtns.getChildAt(i);
            if (tag.equals(btn.getTag())) {
                return (PlayerButton) btn;
            }
        }
        return null;
    }
}