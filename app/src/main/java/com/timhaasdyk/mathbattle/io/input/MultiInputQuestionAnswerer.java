package com.timhaasdyk.mathbattle.io.input;

import android.util.Log;
import com.timhaasdyk.mathbattle.io.*;
import com.timhaasdyk.mathbattle.io.sounds.SoundPlayer;
import com.timhaasdyk.mathbattle.io.tts.HelpType;
import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.models.Question;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public class MultiInputQuestionAnswerer implements QuestionAnswerer, OnPlayerAnswerInputListener {

    private List<PlayerAnswerRecognizer> recognizers;
    private QuestionAnsweredListener questionAnsweredListener;
    private final ActivePlayerListener activePlayerListener;
    private final SoundPlayer soundPlayer;

    private Queue<AnsweringPlayer> answeringPlayers = new ConcurrentLinkedQueue<>();
    private Lock lock = new ReentrantLock();

    private ScheduledExecutorService timeoutExecutor;
    private ScheduledFuture offerHelpFuture;

    private Question currQ;
    private int attemptCount = 0;

    private static final int ACTIVE_PLAYER_TIMEOUT = 1500;
    private static final int HELP_TIMEOUT = 5000;
    private static final int HELP_WAIT_TIMEOUT = 1000;
    private static final int ATTEMPTS_BEFORE_HELP = 5;

    public MultiInputQuestionAnswerer(List<PlayerAnswerRecognizer> recognizers,
                                      ActivePlayerListener activePlayerListener,
                                      SoundPlayer soundPlayer) {
        this.recognizers = recognizers;
        this.activePlayerListener = activePlayerListener;
        this.soundPlayer = soundPlayer;
        for (PlayerAnswerRecognizer recognizer : recognizers) recognizer.setOnPlayerAnswerRecognizedListener(this);
        timeoutExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void setQuestionAnsweredListener(QuestionAnsweredListener questionAnsweredListener) {
        this.questionAnsweredListener = questionAnsweredListener;
    }

    @Override
    public void answerQuestion(Question question) {
        Log.d("RECOG", String.format("Answering question: %s", question));

        clearHelp();
        currQ = question;
        resume();
    }

    @Override
    public void pause() {
        for (PlayerAnswerRecognizer recognizer : recognizers) recognizer.stopRecognizing();
    }

    @Override
    public void resume() {
        for (PlayerAnswerRecognizer recognizer : recognizers) recognizer.startRecognizing();
        rescheduleOfferHelp();
    }

    @Override
    public void cancel() {
        reset();
    }

    @Override
    public void destroy() {
        timeoutExecutor.shutdown();
        for (PlayerAnswerRecognizer recognizer : recognizers) recognizer.destroy();
    }

    @Override
    public void onPlayer(Player player, final long time) {
        AnsweringPlayer answeringPlayer = new AnsweringPlayer(player, time);
        lock.lock();
        answeringPlayers.add(answeringPlayer);
        scheduleActivateDeactivatePlayer(answeringPlayer);
        lock.unlock();
    }

    @Override
    public boolean onAnswer(String answer, boolean finalAnswer) {
        AnsweringPlayer answeringPlayer = answeringPlayers.peek();
        return answeringPlayer != null && attemptAnswer(answeringPlayer.getPlayer(), answer, finalAnswer);
    }

    @Override
    public boolean onPlayerAnswer(Player player, String answer, long time, boolean finalAnswere) {
        scheduleActivateDeactivatePlayer(new AnsweringPlayer(player, time));
        return attemptAnswer(player, answer, finalAnswere);
    }

    private void reset() {
        currQ = null;
        resetQueue();
        clearHelp();
        pause();
    }

    private boolean attemptAnswer(Player player, String answer, boolean finalAnswer) {
        rescheduleOfferHelp();
        if (finalAnswer) attemptCount++;

        if (currQ.attemptAnswer(player, answer)) {
            Log.d("RECOGT", String.format("[%d] Question answered by: %s", System.currentTimeMillis(), player));
            questionAnsweredListener.onFinishedAnsweringQuestion(currQ);
            reset();
            return true;
        } else {
            if (finalAnswer) soundPlayer.playWrongAnswer();
            if (attemptCount >= ATTEMPTS_BEFORE_HELP) {
                requestHelp(HelpType.WRONG);
            }
            return false;
        }
    }

    private void requestHelp(HelpType helpType) {
        clearHelp();
        questionAnsweredListener.onNeedsHelp(helpType);
    }

    private void clearHelp() {
        attemptCount = 0;
        if (offerHelpFuture != null) offerHelpFuture.cancel(true);
    }

    private boolean playerInAnswerQueue(Player player) {
        for (AnsweringPlayer answeringPlayer : answeringPlayers) {
            if (answeringPlayer.getPlayer().equals(player))
                return true;
        }
        return false;
    }

    private void scheduleActivateDeactivatePlayer(final AnsweringPlayer answeringPlayer) {
        activePlayerListener.onActivatePlayer(answeringPlayer.getPlayer());

        timeoutExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                Log.d("LOCK", "LOCKED ASYNC");
                Player player = answeringPlayer.getPlayer();
                if (answeringPlayer.equals(answeringPlayers.peek())) {
                    answeringPlayers.remove().getPlayer();
                }
                if (!playerInAnswerQueue(player))
                    activePlayerListener.onDeactivatePlayer(player);
                lock.unlock();
                Log.d("LOCK", "UNLOCKED ASYNC");
            }
        }, ACTIVE_PLAYER_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private void rescheduleOfferHelp(int timeout) {
        if (offerHelpFuture != null) offerHelpFuture.cancel(true);
        offerHelpFuture = timeoutExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                if (!recognizersAreReceivung())
                    requestHelp(HelpType.TIME);
                else
                    rescheduleOfferHelp(HELP_WAIT_TIMEOUT);
            }
        }, timeout, TimeUnit.MILLISECONDS);
    }

    private boolean recognizersAreReceivung() {
        for (PlayerAnswerRecognizer recognizer : recognizers) {
            if (recognizer.isReceiving()) return true;
        }
        return false;
    }

    private void rescheduleOfferHelp() {
        rescheduleOfferHelp(HELP_TIMEOUT);
    }

    private void resetQueue() {
        lock.lock();
        answeringPlayers.clear();
        lock.unlock();
    }
}