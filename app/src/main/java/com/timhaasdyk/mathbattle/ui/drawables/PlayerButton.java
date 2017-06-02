package com.timhaasdyk.mathbattle.ui.drawables;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.timhaasdyk.mathbattle.R;
import com.timhaasdyk.mathbattle.exceptions.InvalidTagException;
import com.timhaasdyk.mathbattle.models.PlayerTag;

/**
 * @author Tim Haasdyk on 12-May-17.
 */
public class PlayerButton extends View {

    private final GradientDrawable circle;
    private Paint textPaint;
    private String text;
    private final boolean showText;

    private final int DIAMETER;
    private static final int EMPTY_BUTTON_STROKE_WIDTH = 2;
    private static final int EMPTY_BUTTON_STROKE_COLOR = Color.GREEN;
    private static final int ACTIVE_BUTTON_STROKE_WIDTH = 10;
    private static final int ACTIVE_BUTTON_STROKE_COLOR = Color.LTGRAY;
    private final int textX;
    private final int textY;

    public PlayerButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlayerButton, defStyleAttr, 0);
        showText = a.getBoolean(R.styleable.PlayerButton_showText, false);
        a.recycle();

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metric = textPaint.getFontMetrics();
        int textHeight = (int) Math.ceil(metric.descent - metric.ascent);

        DIAMETER = (int)context.getResources().getDimension(R.dimen.btnCircleDiameter);
        textX = DIAMETER/2;
        textY = (int) ((DIAMETER + textHeight)/2 - metric.descent);

        circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        resetButton();
    }

    private void resetButton() {
        setColorAndStroke(Color.TRANSPARENT, EMPTY_BUTTON_STROKE_WIDTH, EMPTY_BUTTON_STROKE_COLOR);
    }

    private void setColorAndStroke(Integer color, int strokeWidth, int strokeColor) {
        if (color != null) circle.setColor(color);
        circle.setStroke(strokeWidth, strokeColor);
        circle.setBounds(0, 0, DIAMETER - strokeWidth, DIAMETER - strokeWidth);
    }

    public GradientDrawable getCircle() {
        return circle;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setTag(Object tag) {
        super.setTag(tag);
        if (tag == null) {
            resetButton();
        } else if (!(tag instanceof PlayerTag)) {
            throw new InvalidTagException("PlayerButton only accepts a tag of type PlayerTag");
        } else {
            setColorAndStroke(null, 0, Color.TRANSPARENT);
            ((PlayerTag) tag).tagPlayerButton(this, getContext().getResources());
        }
        safeInvalidate();
    }

    public void setActivated(boolean activated) {
        if (activated) {
            setColorAndStroke(null, ACTIVE_BUTTON_STROKE_WIDTH, ACTIVE_BUTTON_STROKE_COLOR);
        } else {
            setColorAndStroke(null, 0, Color.TRANSPARENT);
        }
        safeInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        circle.draw(canvas);
        if (showText) canvas.drawText(text, textX, textY, textPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(DIAMETER, DIAMETER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    private void safeInvalidate() {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}