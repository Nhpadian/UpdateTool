package com.xinglin.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.apache.commons.logging.Log;

import com.lansle.lang.LogFactory;
import com.lansle.lang.TimeUtil;
import com.xinglin.update.core.ClassFileReader;
import com.xinglin.update.util.Const;

public class Test
{
    public static void main( String[] args )
    {

        RandomAccessFile raf = null;
        try
        {
            Log                     LOG          = LogFactory.getLog( Test.class.getName() );
            raf = new RandomAccessFile( new File( "D:\\2F\\jdk8\\UserGroup.class" ), Const.READ_MODE );
            ClassFileReader creader = new ClassFileReader( raf );
            byte[] shortBuff = new byte[2];
            byte[] intBuff = new byte[4];
            byte[] longBuff = new byte[16];
            raf.seek(4);

            while( raf.read(longBuff) !=  -1)
            {
                for( byte b : longBuff )
                {
                    LOG.info( b  );

                }
            }
          //  creader.readIntoBuff( raf, longBuff);
            for( byte b : longBuff )
            {
                System.out.println( b  );

            }
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();;
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            if( raf != null )
            {
                try
                {
                    raf.close();
                }
                catch( IOException e )
                {
                    // ignore
                }
            }
        }

    }

}
