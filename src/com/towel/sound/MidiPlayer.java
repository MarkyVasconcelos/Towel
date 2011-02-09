package com.towel.sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * Plays MIDI music. The objective of this class is to easy the manipulation of
 * the Sequencer object. Each MidiPlayer will control only one Sequencer. If the
 * sequencer is not available, the MidiPlayer will simply not play any
 * jgf.sound, not resulting in errors.
 * 
 * @author Vinicius
 */
public class MidiPlayer
{
    private Sequencer sequencer;
    private boolean paused;

    public MidiPlayer()
    {
        try
        {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            paused = true;
        }
        catch (MidiUnavailableException e)
        {
            sequencer = null;
        }
    }

    /**
     * Start playing the given music in loop. This method returns immediatelly.
     * 
     * @param midi The MIDI to be played.
     */
    public void play(Sequence midi)
    {
        play(midi, true);
    }

    /**
     * Start playing the given music. This method returns immediatelly.
     * 
     * @param midi The MIDI to be played.
     * @param loop If true, loop the game sound endlessly. Otherwise plays only
     *            once.
     */
    public void play(Sequence midi, boolean loop)
    {
        if (sequencer == null || midi == null)
            return;

        try
        {
            sequencer.setSequence(midi);
            sequencer.setMicrosecondPosition(0);
            if (loop)
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            setPaused(false);
        }
        catch (InvalidMidiDataException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Stops the music being played, if any.
     */
    public void stop()
    {
        if (sequencer == null || !sequencer.isOpen())
            return;

        sequencer.stop();
        sequencer.setMicrosecondPosition(0);
    }

    /**
     * Closes the device, indicating that the device should now release any
     * system resources it is using. Once closed, no jgf.sound will be played
     * again.
     */
    public void close()
    {
        if (sequencer == null || !sequencer.isOpen())
            return;

        sequencer.close();
    }

    /**
     * Pauses the jgf.sound being played.
     * 
     * @param paused True to pause, false to continue.
     * @see MidiPlayer#isPaused()
     */
    public void setPaused(boolean paused)
    {
        if (this.paused == paused || sequencer == null)
            return;

        this.paused = paused;

        if (paused)
            sequencer.stop();
        else
            sequencer.start();
    }

    /**
     * Indicate if the MIDI is paused or not.
     * 
     * @return True if the MIDI is paused, false otherwise.
     */
    public boolean isPaused()
    {
        return paused;
    }

    /**
     * Returns the current tempo factor. The default is 1.0.
     * 
     * @return The tempo factor.
     * @see MidiPlayer#setTempoFactor(float)
     */
    public double getTempoFactor()
    {
        if (sequencer == null)
            return 0;

        return sequencer.getTempoFactor();
    }

    /**
     * Obtains the current mute state for a track. The default mute state for
     * all tracks which have not been muted is false. In any case where the
     * specified track has not been muted, this method should return false. This
     * applies if the sequencer does not support muting of tracks, and if the
     * specified track index is not valid.
     * 
     * @param track the track number. Tracks in the current sequence are
     *            numbered from 0 to the number of tracks in the sequence minus
     *            1
     * @return <code>true</code> if muted, <code>false</code> if not.
     */
    public boolean getTrackMute(int track)
    {
        if (sequencer == null)
            return true;

        return sequencer.getTrackMute(track);
    }

    /**
     * Obtains the current solo state for a track. The default mute state for
     * all tracks which have not been solo'd is false. In any case where the
     * specified track has not been solo'd, this method should return false.
     * This applies if the sequencer does not support soloing of tracks, and if
     * the specified track index is not valid.
     * 
     * @param track the track number. Tracks in the current sequence are
     *            numbered from 0 to the number of tracks in the sequence minus
     *            1.
     * @return true if solo'd, false if not.
     */
    public boolean getTrackSolo(int track)
    {
        if (sequencer == null)
            return false;

        return sequencer.getTrackSolo(track);
    }

    /**
     * Scales the sequencer's actual playback tempo by the factor provided. The
     * default is 1.0. A value of 1.0 represents the natural rate (the tempo
     * specified in the sequence), 2.0 means twice as fast, etc. The tempo
     * factor does not affect the values returned by getTempoInMPQ and
     * getTempoInBPM. Those values indicate the tempo prior to scaling. Note
     * that the tempo factor cannot be adjusted when external synchronization is
     * used. In that situation, setTempoFactor always sets the tempo factor to
     * 1.0.
     * 
     * @param factor the requested tempo scalar
     */
    public void setTempoFactor(double factor)
    {
        if (sequencer == null)
            return;

        sequencer.setTempoFactor((float) factor);
    }

    /**
     * Sets the mute state for a track. This method may fail for a number of
     * reasons. For example, the track number specified may not be valid for the
     * current sequence, or the sequencer may not support this functionality. An
     * application which needs to verify whether this operation succeeded should
     * follow this call with a call to getTrackMute.
     * 
     * @param track the track number. Tracks in the current sequence are
     *            numbered from 0 to the number of tracks in the sequence minus
     *            1.
     * @param mute the new mute state for the track. true implies the track
     *            should be muted, false implies the track should be unmuted.
     */
    public void setTrackMute(int track, boolean mute)
    {
        if (sequencer == null)
            return;

        sequencer.setTrackMute(track, mute);
    }

    /**
     * Sets the solo state for a track. If solo is true only this track and
     * other solo'd tracks will jgf.sound. If solo is false then only other
     * solo'd tracks will jgf.sound, unless no tracks are solo'd in which case
     * all un-muted tracks will jgf.sound. This method may fail for a number of
     * reasons. For example, the track number specified may not be valid for the
     * current sequence, or the sequencer may not support this functionality. An
     * application which needs to verify whether this operation succeeded should
     * follow this call with a call to getTrackSolo.
     * 
     * @param track the track number. Tracks in the current sequence are
     *            numbered from 0 to the number of tracks in the sequence minus
     *            1.
     * @param solo the new solo state for the track. true implies the track
     *            should be solo'd, false implies the track should not be
     *            solo'd.
     */
    public void setTrackSolo(int track, boolean solo)
    {
        if (sequencer == null)
            return;

        sequencer.setTrackSolo(track, solo);
    }

    /**
     * Indicate if the sequencer was obtained and, therefore, if it's possible
     * to play musics with this object.
     * 
     * @return
     */
    public boolean isSequencerAvailable()
    {
        return sequencer != null;
    }

}
