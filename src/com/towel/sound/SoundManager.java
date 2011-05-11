package com.towel.sound;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import com.towel.sound.filter.LoopFilter;


/**
 * Manages the game sounds. All noise in game must be in the same format. <br>
 * TODO Adicionar um mecanismo de listeners para os sons.<br>
 * TODO Adicionar um tempo m�ximo para que o som seja tocado.
 * 
 * @author Vin�cius
 */
public class SoundManager
{
    private AudioFormat format;

    private ExecutorService soundPool;

    /**
     * Create a new SoundManager for the specified audio format. All future
     * sounds in this manager should match this format or will not be played.
     * 
     * @param format The format that will be managed.
     */
    public SoundManager(AudioFormat format)
    {
        if (format == null)
            throw new IllegalArgumentException("Invalid audio format!");

        this.format = format;
        this.soundPool = createThreadPool(format,
                new SoundManagerThreadFactory());
    }

    /**
     * Returns the format supported by this SoundManager.
     * 
     * @return the format supported by this SoundManager.
     */
    public AudioFormat getFormat()
    {
        return format;
    }

    /**
     * Retrieve the ExecutorService more adequate for this system using the
     * given AudioFormat.
     * 
     * @param format The format to test
     * @return The more adequate ExecutorService.
     */
    private ExecutorService createThreadPool(AudioFormat format,
            ThreadFactory threadFactory)
    {
        int max = getMaxSimultaneousSounds(format);

        if (max == AudioSystem.NOT_SPECIFIED || max == 0)
            return Executors.newCachedThreadPool(threadFactory);

        if (max == 1)
            return Executors.newSingleThreadExecutor(threadFactory);

        return Executors.newFixedThreadPool(max, threadFactory);
    }

    /**
     * Return the maximum number of simultaneous sounds in this format supported
     * by this audio format in the default mixer.
     * 
     * @param format The format to be tested.
     * @return The maximum number of simultaneous sounds supported, or
     *         AudioSystem.NOT_SPECIFIED if there's no known upper limit.
     */
    private int getMaxSimultaneousSounds(AudioFormat format)
    {
        return AudioSystem.getMixer(null).getMaxLines(
                new DataLine.Info(SourceDataLine.class, format));
    }

    /**
     * Play this Streamed. Return a PlayingStreamed object that represent the
     * noise being played. The noise starts almost immediatelly, as long as
     * there are available threads in the pool.
     * 
     * @param streamed The noise to play.
     * @return A PlayingStreamed that represents the noise being played.
     * @throws IllegalArgumentException If the format of the given noise does
     *             not match the noise manager supported format.
     */
    public PlayingStreamed play(Streamed streamed)
    {
        return play(streamed, false);
    }

    /**
     * Play this Streamed. Return a PlayingStreamed object that represent the
     * noise being played. The noise starts almost immediatelly, as long as
     * there are available threads in the pool.
     * 
     * @param streamed The noise to play.
     * @param loop If true, automatically adds a loop filter to the stream and
     *            so, play the sound endlessly.
     * @return A PlayingStreamed that represents the noise being played.
     * @throws IllegalArgumentException If the format of the given noise does
     *             not match the noise manager supported format.
     * @throws IllegalStateException If the manager was already closed.
     */
    public PlayingStreamed play(Streamed streamed, boolean loop)
    {
        if (soundPool.isShutdown())
            throw new IllegalStateException("Manager already closed!");

        if (!format.matches(streamed.getFormat()))
            throw new IllegalArgumentException("The streamed noise is a \n"
                    + streamed.getFormat() + " but it should be a \n" + format);

        PlayingStreamed playing;

        playing = new PlayingStreamed(loop ? new LoopFilter(streamed)
                : streamed);

        soundPool.execute(playing);
        return playing;
    }

    /**
     * Closes this manager. The manager will interrupt all sound threads and
     * will no longer accept any more sounds.
     */
    public void close()
    {
        soundPool.shutdownNow();
    }

    /**
     * Creates sound threads for the manager.
     * 
     * @author Vin�cius
     */
    private static class SoundManagerThreadFactory implements ThreadFactory
    {
        public Thread newThread(Runnable r)
        {
            Thread t = new Thread(r);
            t.setName("Sound Thread - " + Long.toString(t.getId()));
            t.setDaemon(true);
            return t;
        }
    }
}
