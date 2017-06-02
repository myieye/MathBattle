package com.timhaasdyk.mathbattle.models;

import android.content.res.Resources;
import com.timhaasdyk.mathbattle.ui.drawables.PlayerButton;

/**
 * @author Tim Haasdyk on 11-May-17.
 */
public class ColorTag extends PlayerTag {

    private int color;

    public ColorTag(String colorName, int color) {
        super(colorName);
        this.color = color;
    }

    @Override
    public void tagPlayerButton(PlayerButton playerButton, Resources resources) {
        playerButton.getCircle().setColor(color);
        playerButton.setText(getId());
    }

    public int getColor() {
        return this.color;
    }
}