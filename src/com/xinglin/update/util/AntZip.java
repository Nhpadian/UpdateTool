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
 * @author <a href="mailto:msk@xinglin-tech.com">缪顺科</a>
 * @since 2021-04-15
 */
public class AntZip
{
    private ZipFile         zipFile;
    private ZipOutputStream zipOut;     // 压缩Zip
    
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

    // 压缩文件夹内的文件
    // zipDirectoryPath:需要压缩的文件夹名
    public String doZip( String zipDirectory )
    {
        File zipDir = new File( zipDirectory );
        String zipFileName = zipDir.getName() + "_" + TimeUtil.format( System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss" ) + ".zip";// 压缩后生成的zip文件名

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

    // 由doZip调用,递归完成目录文件读取
    private void handleDir( File dir, ZipOutputStream zipOut, String fpath ) throws IOException
    {
        FileInputStream fileIn;
        File[] files;

        files = dir.listFiles();

        if( files.length == 0 )
        {// 如果目录为空,则单独创建之.
         // ZipEntry的isDirectory()方法中,目录以"/"结尾.
            System.out.println( dir.getAbsolutePath() );
            System.out.println( new File( fpath ).getParent() );
            this.zipOut.putNextEntry( new ZipEntry( dir.toString().replace( new File( fpath ).getParent(), "" ) ) );
            this.zipOut.closeEntry();
        }
        else
        {// 如果目录不为空,则分别处理目录和文件.
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

    // 解压指定zip文件
    public void unZip( String unZipfileName )
    {// unZipfileName需要解压的zip文件名
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
                    // 如果指定文件的目录不存在,则创建之.
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

    // 设置缓冲区大小
    public void setBufSize( int bufSize )
    {
        _bufSize = bufSize;
    }

    // 测试AntZip类
    public static void main( String[] args ) throws Exception
    {
        System.out.println( TimeUtil.dateTime() );

        System.out.println( "开始GC" );
        long s1 = System.currentTimeMillis();
        System.gc();
        System.out.println( "耗时：" + JSPUtil.spanToString( System.currentTimeMillis() - s1 ) );
        
        AntZip a = new AntZip();
        System.out.println( "开始压缩" );
        long s = System.currentTimeMillis();
        String name = a.doZip( "D:\\00F\\niszl" );
        System.out.println( name + "压缩结束" );
        System.out.println( "耗时：" + JSPUtil.spanToString( System.currentTimeMillis() - s ) );
    }
    

}