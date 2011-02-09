package com.towel.sound.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;

import com.towel.sound.Streamed;


/**
 * Allows a Streamed to loop continuously. This class subclasses only streamed
 * since there's no sense in an endless sample array of bytes.
 * 
 * Streams with no bytes or closed streams will not loop.
 * 
 * @author Vin�cius
 */
public class LoopFilter implements Streamed
{
    private Streamed stream;

    /**
     * Creates a new loop filter for the given Streamed.
     * 
     * @param stream The streamed to loop.
     * @param IllegalArgumentException If the source is null or is a loop
     *            stream.
     */
    public LoopFilter(Streamed stream)
    {
        if (stream == null)
            throw new IllegalArgumentException("Source cannot be null!");

        if (stream instanceof LoopFilter)
            throw new IllegalArgumentException("Cannot loop a loop!");

        this.stream = stream;
    }

    public InputStream newInputStream()
    {
        return new LoopInputStream(stream.newInputStream());
    }

    public AudioFormat getFormat()
    {
        return stream.getFormat();
    }

    /**
     * Loop through the source stream endlessly.
     * 
     * @author Vin�cius
     */
    private class LoopInputStream extends InputStream
    {
        private InputStream source;

        /**
         * Create a new LoopInputStream that will loop over the bytes in the
         * source stream.
         * 
         * @param in The jgf.input stream to be used as source.
         */
        protected LoopInputStream(InputStream in)
        {
            super();
            source = in;
        }

        @Override
        public int read() throws IOException
        {
            int read = source.read();

            if (read != -1)
                return read;

            source.reset();
            return source.read();
        }
    }
}
