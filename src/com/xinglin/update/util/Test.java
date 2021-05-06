package com.xinglin.update.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import com.lansle.lang.TimeUtil;

public class Test
{
    public static void main( String[] args )
    {
        // String path = "D:\\0000F\\nis18";
        // String[] oldfiles = FileUtil.getFilesFromSubDirbyPrefix( "D:\\0000F\\niszl" );
        // String[] newfiles = FileUtil.getFilesFromSubDirbyPrefix( "D:\\0000F\\nis18" );
        // HashMap<String,String> oldmap = new HashMap<String,String>();
        // HashMap<String,String> newmap = new HashMap<String,String>();
        // Stream.of( oldfiles ).forEachOrdered( el -> oldmap.put( el.replace( "D:\\0000F\\niszl", "") , el ) );
        // Stream.of( newfiles ).forEachOrdered( el -> newmap.put( el.replace( "D:\\0000F\\nis18", ""), el ) );
        //
        // System.out.println( oldmap.size() );
        // System.out.println( newmap.size() );
        //
        // String[] olds = Stream.of( oldfiles ).parallel().map( el -> el.replace( "D:\\0000F\\niszl", "") ).filter( el -> newmap.get( el ) == null ).toArray( String[]::new );
        // String[] diffs = Stream.of( oldfiles ).parallel().map( el -> el.replace( "D:\\0000F\\niszl", "") ).filter( el -> newmap.get( el ) != null ).toArray( String[]::new );
        // String[] news = Stream.of( newfiles ).parallel().map( el -> el.replace( "D:\\0000F\\nis18", "") ).filter( el -> oldmap.get( el ) == null ).toArray( String[]::new );
        // System.out.println( olds.length );
        // System.out.println( diffs.length );
        // System.out.println( news.length );
        //
        // System.out.println( "news文件：" );
        // Stream.of( olds ).parallel().forEach( System.out :: println );

        //
        // try
        // {
        // System.out.println( "开始压缩" );
        // long s = System.currentTimeMillis();
        // ZipUtil.compressFileList( "D:\\0000F\\niszl_" + TimeUtil.format( System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss" ) + ".zip", path );
        // System.out.println( "压缩结束" );
        // System.out.println( "耗时：" + JSPUtil.spanToString( System.currentTimeMillis() - s ) );
        //
        // }
        // catch( Exception e )
        // {
        // e.printStackTrace();
        // }
        printlnLibs();
    }

    public static void printlnLibs()
    {
        String[] libs = FileUtil.getFilesFromSubDirbyPrefix( "D:\\eclipse-workspace3\\updatetool\\lib", false );

        Stream.of( libs ).map( el -> " lib/" + el + " " ).forEachOrdered( System.out::println );
    }

}
