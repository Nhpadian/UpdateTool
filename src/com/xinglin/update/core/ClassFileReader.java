package com.xinglin.update.core;

import java.io.IOException;
import java.io.RandomAccessFile;


public class ClassFileReader
{

    private RandomAccessFile classRaf = null;

    /**
     * Creates a new HeaderReader object with the given input stream
     * 
     * @param classRaf
     */
    public ClassFileReader( RandomAccessFile classRaf )
    {
        this.classRaf = classRaf; 
    }

    /**
     * Reads buf length of bytes from the input stream to buf
     * 
     * @param classRaf
     * @param buf
     * @return byte array
     * @throws Exception 
     * @throws ZipException
     */
    public byte[] readIntoBuff(  RandomAccessFile classRaf, byte[] buf ) throws Exception
    {
        try
        {
            if( classRaf.read( buf, 0, buf.length ) != -1 )
            {
                return buf;
            }
            else
            {
                throw new Exception( "unexpected end of file when reading short buff" );
            }
        }
        catch( IOException e )
        {
            throw new Exception( "IOException when reading short buff", e );
        }
    }
}
