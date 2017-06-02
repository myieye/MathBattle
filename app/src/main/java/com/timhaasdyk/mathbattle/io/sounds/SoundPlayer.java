package com.timhaasdyk.mathbattle.io.sounds;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import com.timhaasdyk.mathbattle.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Tim Haasdyk on 17-May-17.
 */
public class SoundPlayer {

    private static final int DEFAULT_PRIORITY = 1;
    private static final int DEFAULT_SRC_QUALITY = 0;
    private static final int DEFAULT_MAX_STREAMS = 2;
    private static final int NO_REPEAT = 0;
    private static final float VOLUME = 1;
    private static final float DEFAULT_RATE = 1;

    private static final int CORRECT_ANSWER = R.raw.correct_answer;
    private static final int[] WRONG_ANSWERS = {R.raw.wrong_answer_1, R.raw.wrong_answer_2/*, R.raw.wrong_answer_3*/};
    private static final int QUIZ_FINISHED = R.raw.quiz_finished;

    private final SoundPool soundPool;
    private Map<Integer, Integer> soundMap;
    private Random random;

    public SoundPlayer(SoundPool soundPool, Context context) {
        this.soundPool = soundPool;
        this.random = new Random();
        soundMap = new HashMap<>();
        soundMap.put(CORRECT_ANSWER, soundPool.load(context, CORRECT_ANSWER, DEFAULT_PRIORITY));
        soundMap.put(WRONG_ANSWERS[0], soundPool.load(context, WRONG_ANSWERS[0], DEFAULT_PRIORITY));
        soundMap.put(WRONG_ANSWERS[1], soundPool.load(context, WRONG_ANSWERS[1], DEFAULT_PRIORITY));
        //soundMap.put(WRONG_ANSWERS[2], soundPool.load(context, WRONG_ANSWERS[2], DEFAULT_PRIORITY));
        soundMap.put(QUIZ_FINISHED, soundPool.load(context, QUIZ_FINISHED, DEFAULT_PRIORITY));
    }

    public SoundPool getSoundPool() {
        return this.soundPool;
    }

    public void playCorrectAnswer() {
        playSound(soundMap.get(CORRECT_ANSWER));
    }

    public void playWrongAnswer() {
        playSound(soundMap.get(WRONG_ANSWERS[random.nextInt(WRONG_ANSWERS.length)]));
    }

    public void playQuizFinished() {
        playSound(soundMap.get(QUIZ_FINISHED));
    }

    private void playSound(int soundId) {
        soundPool.play(soundId, VOLUME, VOLUME, DEFAULT_PRIORITY, NO_REPEAT, DEFAULT_RATE);
    }

    public static SoundPlayer defaultSoundPlayer(Context context) {
        SoundPool soundPool;
        if((android.os.Build.VERSION.SDK_INT) >= 21){
            soundPool = getBuildSoundPool();
        } else {
            soundPool = new SoundPool(DEFAULT_MAX_STREAMS, AudioManager.STREAM_NOTIFICATION, DEFAULT_SRC_QUALITY);
        }
        return new SoundPlayer(soundPool, context);
    }

    @TargetApi(21)
    private static SoundPool getBuildSoundPool() {
        return new SoundPool.Builder()
                .setMaxStreams(DEFAULT_MAX_STREAMS)
                .setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build())
                .build();
    }
}