package com.towel.sound;

import javax.sound.sampled.AudioFormat;

/**
 * Represents an audio with a format.
 * 
 * @author Vin�cius
 */
public interface Formatted
{
    /**
     * Returns the noise format.
     */
    AudioFormat getFormat();
}
