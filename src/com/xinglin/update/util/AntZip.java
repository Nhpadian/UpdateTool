/*
 * Copyright (c) 2005-2021 Orcus team. All rights reserved.
 */
package com.xinglin.update.util;

import java.io.*;
import org.apache.tools.zip.*;

import com.lansle.lang.TimeUtil;

import java.util.Enumeration;

/**
 * 
 * 
 * @author <a href="mailto:msk@xinglin-tech.com">��˳��</a>
 * @since 2021-04-15
 */
public class AntZip
{
    private ZipFile         zipFile;
    private ZipOutputStream zipOut;     // ѹ��Zip
    
    private static int      _bufSize;   // size of bytes
    private byte[]          buf;
    private int             readedBytes;

    public AntZip()
    {
        this( 512 );
    }

    public AntZip( int bufSize )
    {
        _bufSize = bufSize;
        this.buf = new byte[_bufSize];
    }

    // ѹ���ļ����ڵ��ļ�
    // zipDirectoryPath:��Ҫѹ�����ļ�����
    public String doZip( String zipDirectory )
    {
        File zipDir = new File( zipDirectory );
        String zipFileName = zipDir.getName() + "_" + TimeUtil.format( System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss" ) + ".zip";// ѹ�������ɵ�zip�ļ���

        try
        {
            this.zipOut = new ZipOutputStream( new BufferedOutputStream( new FileOutputStream( zipDir.getParent() + "/" + zipFileName ) ) );
            handleDir( zipDir, this.zipOut, zipDirectory );
            System.out.println( TimeUtil.dateTime() );
            this.zipOut.close();
        }
        catch( IOException ioe )
        {
            ioe.printStackTrace();
        }
        return zipFileName;
    }

    // ��doZip����,�ݹ����Ŀ¼�ļ���ȡ
    private void handleDir( File dir, ZipOutputStream zipOut, String fpath ) throws IOException
    {
        FileInputStream fileIn;
        File[] files;

        files = dir.listFiles();

        if( files.length == 0 )
        {// ���Ŀ¼Ϊ��,�򵥶�����֮.
         // ZipEntry��isDirectory()������,Ŀ¼��"/"��β.
            System.out.println( dir.getAbsolutePath() );
            System.out.println( new File( fpath ).getParent() );
            this.zipOut.putNextEntry( new ZipEntry( dir.toString().replace( new File( fpath ).getParent(), "" ) ) );
            this.zipOut.closeEntry();
        }
        else
        {// ���Ŀ¼��Ϊ��,��ֱ���Ŀ¼���ļ�.
            for( File fileName : files )
            {
//                 System.out.println(fileName.getAbsolutePath());

                if( fileName.isDirectory() )
                {
                    handleDir( fileName, this.zipOut, fpath );
                }
                else
                {
                    fileIn = new FileInputStream( fileName );

                    this.zipOut.putNextEntry( new ZipEntry( fileName.toString().replace( new File( fpath ).getParent(), "" ) ) );

                    while( ( this.readedBytes = fileIn.read( this.buf ) ) > 0 )
                    {
                        this.zipOut.write( this.buf, 0, this.readedBytes );
                    }

                    this.zipOut.closeEntry();
                }
            }
        }
    }

    // ��ѹָ��zip�ļ�
    public void unZip( String unZipfileName )
    {// unZipfileName��Ҫ��ѹ��zip�ļ���
        FileOutputStream fileOut;
        File file;
        InputStream inputStream;

        try
        {
            this.zipFile = new ZipFile( unZipfileName );

            for( Enumeration<?> entries = this.zipFile.getEntries(); entries.hasMoreElements(); )
            {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                file = new File( entry.getName() );

                if( entry.isDirectory() )
                {
                    file.mkdirs();
                }
                else
                {
                    // ���ָ���ļ���Ŀ¼������,�򴴽�֮.
                    File parent = file.getParentFile();
                    if( !parent.exists() )
                    {
                        parent.mkdirs();
                    }

                    inputStream = zipFile.getInputStream( entry );

                    fileOut = new FileOutputStream( file );
                    while( ( this.readedBytes = inputStream.read( this.buf ) ) > 0 )
                    {
                        fileOut.write( this.buf, 0, this.readedBytes );
                    }
                    fileOut.close();

                    inputStream.close();
                }
            }
            this.zipFile.close();
        }
        catch( IOException ioe )
        {
            ioe.printStackTrace();
        }
    }

    // ���û�������С
    public void setBufSize( int bufSize )
    {
        _bufSize = bufSize;
    }

    // ����AntZip��
    public static void main( String[] args ) throws Exception
    {
        System.out.println( TimeUtil.dateTime() );

        System.out.println( "��ʼGC" );
        long s1 = System.currentTimeMillis();
        System.gc();
        System.out.println( "��ʱ��" + JSPUtil.spanToString( System.currentTimeMillis() - s1 ) );
        
        AntZip a = new AntZip();
        System.out.println( "��ʼѹ��" );
        long s = System.currentTimeMillis();
        String name = a.doZip( "D:\\00F\\niszl" );
        System.out.println( name + "ѹ������" );
        System.out.println( "��ʱ��" + JSPUtil.spanToString( System.currentTimeMillis() - s ) );
    }
    

}