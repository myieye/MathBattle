package com.timhaasdyk.mathbattle.models;

import android.content.res.Resources;
import com.timhaasdyk.mathbattle.ui.drawables.PlayerButton;

/**
 * @author Tim Haasdyk on 11-May-17.
 */
public class PlayerTag {

    private String id;

    public PlayerTag(String id) {
        this.id = id;
    }

    protected String getId() {
        return this.id;
    }

    public void tagPlayerButton(PlayerButton playerButton, Resources resources) { }

    @Override
    public String toString() {
        return getId();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj instanceof PlayerTag)
                && this.getId().equalsIgnoreCase(((PlayerTag)obj).getId());
    }
}