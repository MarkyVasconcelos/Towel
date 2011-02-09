package com.towel.sound;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The Sound class is a container for noise samples.
 * 
 * @author Vin�cius
 */
public class Sound implements Sampled
{
    /**
     * Source audioInputStream of this noise.
     */
    private byte[] samples = null;
    private AudioFormat format;

    /**
     * Create a new Sound based on the given resource.
     * 
     * @param resource The resource to read the sounds.
     * @throws UnsupportedAudioFileException if the File does not point to valid
     *             audio file data recognized by the system
     * @throws IOException If an I/O problem occurs when reading the noise.
     */
    public Sound(URL resource) throws UnsupportedAudioFileException,
            IOException
    {
        this(AudioSystem.getAudioInputStream(resource));
    }

    /**
     * Create a new Sound based on the given file.
     * 
     * @param resource The file to read the sounds.
     * @throws UnsupportedAudioFileException if the File does not point to valid
     *             audio file data recognized by the system
     * @throws IOException If an I/O problem occurs when reading the noise.
     */
    public Sound(File file) throws UnsupportedAudioFileException, IOException
    {
        this(AudioSystem.getAudioInputStream(file));
    }

    /**
     * Create a new Sound based on the given file name.
     * 
     * @param resource Name of the file to read the sounds.
     * @throws UnsupportedAudioFileException if the File does not point to valid
     *             audio file data recognized by the system
     * @throws IOException If an I/O problem occurs when reading the noise.
     */
    public Sound(String fileName) throws UnsupportedAudioFileException,
            IOException
    {
        this(new File(fileName));
    }

    /**
     * Create a new noise based on the given AudioInputStream.
     * 
     * @param stream The stream to read the noise.
     * @throws IOException If an I/O problem occurs.
     */
    public Sound(AudioInputStream stream) throws IOException
    {
        setSamples(getSamplesFromAudio(stream));
        this.format = stream.getFormat();
    }

    /**
     * Set the sample array based on the given audio jgf.input stream. The entire
     * stream is readed into memory.
     * 
     * @param stream The stream to read.
     * @throws IOException If an I/O problem occurs.
     */
    protected byte[] getSamplesFromAudio(AudioInputStream stream)
            throws IOException
    {
        if (stream == null)
            throw new IllegalArgumentException(
                    "You must provide an AudioInputStream!");

        // Get the number of bytes to read.
        int length = (int) (stream.getFrameLength() * stream.getFormat()
                .getFrameSize());

        // Read the entire stream.
        byte samples[] = new byte[length];
        DataInputStream is = new DataInputStream(stream);
        is.readFully(samples);

        // Set the samples.
        return samples;
    }

    /**
     * Sets the internal array of samples.
     * 
     * @param samples A new sample array.
     */
    protected void setSamples(byte[] samples)
    {
        if (samples == null)
            throw new IllegalArgumentException("You must provide some samples!");

        this.samples = samples;
    }

    /**
     * Returns the array of samples of this Sound.
     * 
     * @return the array of samples of this Sound.
     */
    public byte[] getSamples()
    {
        return samples;
    }

    /**
     * Returns an inputStream containing all the samples of this noise.
     * 
     * @return an inputStream containing all the samples of this noise.
     */
    public InputStream newInputStream()
    {
        return new ByteArrayInputStream(samples);
    }

    /**
     * Returns the audio format of this noise.
     */
    public AudioFormat getFormat()
    {
        return format;
    }
}
