package com.xinglin.update.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.lansle.io.FileResource;

/**
 * 文件读取的工具类
 * 
 * @author <a href="mailto:huoray@xinglin-tech.com">霍瑞</a>
 * @since 2015-1-12
 */
public class FileUtil
{
    private static final String SEP = System.getProperty( "line.separator" );

    /**
     * 在指定路径下根据当前时间创造一个新路径
     * 
     * @param path
     */
    public static String currentDir( String path )
    {
        File oldDir = new File( path );
        if( oldDir.exists() == false || oldDir.isDirectory() == false )
        {
            oldDir.mkdir();
        }
        SimpleDateFormat formater = null;
        formater = new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss" );
        Date date = new Date();
        String newPath = path + "/" + formater.format( date ) + "/";
        File newDir = new File( newPath );
        if( newDir.exists() == false || newDir.isDirectory() == false )
        {
            newDir.mkdir();
        }

        return newPath;
    }

    /**
     * 获取路径下所有带前缀的文件
     * 
     * @param path
     */
    public static String[] getFilesFromDirbyPrefix( String path )
    {
        File dir = new File( path );
        if( dir.exists() == false || dir.isDirectory() == false )
        {
            return null;
        }

        return dir.list( new MyFilter() );
    }

    static class MyFilter implements FilenameFilter
    {

        public MyFilter()
        {

        }

        public boolean accept( File dir, String name )
        {
            File file = new File( dir.getAbsoluteFile() + "\\" + name );
            if( file.isDirectory() )
            {
                return false;
            }
            return true;
        }
    }

    /**
     * 遍历指定路径下的所有文件夹
     * 
     * @param path
     * @return
     */
    public static ArrayList<String> getSubDir( String path )
    {
        File dir = new File( path );
        if( dir.exists() == false || dir.isDirectory() == false )
        {
            return null;
        }

        String[] subDirs = dir.list();
        if( subDirs == null || subDirs.length == 0 )
        {
            return null;
        }

        ArrayList<String> list = new ArrayList<String>();
        for( String sub : subDirs )
        {
            String fileName = path + "\\" + sub;
            File file = new File( fileName );
            if( file.isDirectory() )
            {
                list.add( file.getAbsolutePath() );
                ArrayList<String> subs = getSubDir( file.getAbsolutePath() );
                if( subs != null )
                {
                    list.addAll( subs );
                }
            }
        }

        return list;
    }

    /**
     * 获取路径下及子目录下所有带前缀的文件，文件均为绝对路径
     * 
     * @param path
     */
    public static String[] getFilesFromSubDirbyPrefix( String path )
    {
        return getFilesFromSubDirbyPrefix( path, true );

    }

    /**
     * 获取路径下及子目录下所有带前缀的文件，文件均为绝对路径
     * 
     * @param path
     */
    public static String[] getFilesFromSubDirbyPrefix( String path, boolean havePath )
    {
        ArrayList<String> subDirs = getSubDir( path );

        ArrayList<String> list = new ArrayList<String>();
        String[] files2 = getFilesFromDirbyPrefix( path );
        for( String name : files2 )
        {
            if( havePath )
            {
                list.add( path + "\\" + name );
            }
            else
            {
                list.add( name );
            }
        }

        if( subDirs == null || subDirs.size() == 0 )
        {
            return list.toArray( new String[0] );
        }
        else
        {
            for( String sub : subDirs )
            {
                String[] files = getFilesFromDirbyPrefix( sub );
                if( files != null )
                {
                    for( String name : files )
                    {
                        if( havePath )
                        {
                            list.add( sub + "\\" + name );
                        }
                        else
                        {
                            list.add( name );
                        }
                    }
                }
            }
            return list.toArray( new String[0] );
        }

    }

    public static String read( String fileName, String charset )
    {

        StringBuffer sb = new StringBuffer();

        try
        {
            FileInputStream input = new FileInputStream( fileName );
            InputStreamReader ir = new InputStreamReader( input, charset );
            BufferedReader reader = new BufferedReader( ir );
            String line = null;
            while( ( line = reader.readLine() ) != null )
            {
                sb.append( line );
                sb.append( SEP );
            }

            input.close();
            ir.close();
            reader.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String[] getText( String filename )
    {
        return getText( filename, "UTF8" );
    }

    public static String[] getText( String filename, String charset )
    {
        File file = getFile( filename );

        return getText( file, charset );
    }

    public static String[] getText( File file )
    {
        return getText( file, "UTF8" );
    }

    public static String[] getText( File file, String charset )
    {
        ArrayList<String> list = new ArrayList<String>();
        FileInputStream input;
        InputStreamReader ir = null;
        try
        {
            input = new FileInputStream( file );
            ir = new InputStreamReader( input, charset );
        }
        catch( UnsupportedEncodingException e1 )
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader( ir );
        try
        {
            String line = null;
            while( ( line = reader.readLine() ) != null )
            {
                list.add( line );
            }
            reader.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return list.toArray( new String[0] );
    }

    /**
     * 写文本到指定文件
     * 
     * @param filename
     * @param content
     */
    public static void save( String filename, String content, boolean append )
    {
        File file = getFile( filename );

        OutputStreamWriter writer = null;
        try
        {
            writer = new OutputStreamWriter( new FileOutputStream( file, append ),
                    "UTF8" );
            writer.write( content );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                if( writer != null )
                {
                    writer.close();
                }
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    public static void save( String filename, String[] content, boolean append )
    {
        if( content == null || content.length == 0 )
        {
            save( filename, "", append );
            return;
        }
        StringBuffer buf = new StringBuffer();

        for( String s : content )
        {
            buf.append( s );
            buf.append( "\r\n" );
        }

        save( filename, buf.toString(), append );
    }

    public static OutputStream getOutputStream( String fileName )
            throws Throwable
    {
        File file1 = getFile( fileName );

        if( file1.exists() == false )
        {
            file1.createNewFile();
        }

        OutputStream outputStream = ( new FileResource( file1 ) )
                .createOutputStream();

        return outputStream;
    }

    /**
     * 返回指定的文件，若路径或文件不存在自动创建
     * 
     * @param fileName
     * @return
     */
    public static File getFile( String fileName )
    {
        return getFile( fileName, true );
    }

    /**
     * 获取指定文件，并根据要求创建或返回null
     * 
     * @param fileName
     * @param create
     * @return
     */
    public static File getFile( String fileName, boolean create )
    {
        if( fileName == null || fileName.length() == 0 )
        {
            return null;
        }

        fileName = fileName.replace( "\\", "/" );

        String[] dirs = fileName.split( "/" );
        if( dirs.length < 2 )
        {
            System.out.println( "没有路径名:" + fileName );
            return null;
        }

        try
        {
            String path = null;
            for( int i = 0; i < dirs.length - 1; i++ )
            {
                if( path == null )
                {
                    path = dirs[i];
                }
                else
                {
                    path += "\\" + dirs[i];
                }

                File fpath = new File( path );
                if( fpath.exists() == false )
                {
                    fpath.mkdir();
                }
            }

            File file = new File( path + "\\" + dirs[dirs.length - 1] );

            if( file.exists() == false )
            {
                if( create == true )
                {
                    file.createNewFile();
                }
                else
                {
                    return null;
                }
            }
            return file;
        }
        catch( Throwable t )
        {
            System.out.println( "创建路径失败，无法写入数据" + fileName );
            t.printStackTrace();
        }

        return null;
    }

    public static void readSrcCode( String srcDir, String outFileName )
    {
        File file = getFile( srcDir );
        StringBuilder bu = new StringBuilder();
        tree( file, bu );

        FileUtil.save( outFileName, bu.toString(), false );
    }

    /**
     * 将路径下文件写入StringBuilder中
     * 
     * @param f
     * @param bu
     */
    public static void tree( File f, StringBuilder bu )
    {
        // 判断传入对象是否为一个文件夹对象
        if( !f.isDirectory() )
        {
            System.out.println( "你输入的不是一个文件夹，请检查路径是否有误！！" );
        }
        else
        {
            File[] t = f.listFiles();
            for( int i = 0; i < t.length; i++ )
            {
                // 判断文件列表中的对象是否为文件夹对象，如果是则执行tree递归，直到把此文件夹中所有文件输出为止
                if( t[i].isDirectory() )
                {
                    tree( t[i], bu );
                }
                else
                {
                    if( t[i].getName().indexOf( "java" ) != -1
                            || t[i].getName().indexOf( "jsp" ) != -1 )
                    {
                        String[] texts = FileUtil.getText( t[i] );
                        for( String line : texts )
                        {
                            if( line.indexOf( "*" ) == -1
                                    && line.indexOf( "package" ) == -1 )
                            {
                                bu.append( line );
                                bu.append( "\r\n" );
                            }
                        }

                        System.out.println( "read from " + t[i].getName() );
                    }
                }
            }
        }

    }

    public static void writeObject2File( String fileName, Object[] obj )
    {
        File f = getFile( fileName );
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream( f ) );
            oos.writeObject( obj );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    public static void writeObject2File( String fileName, Object obj )
    {
        File f = getFile( fileName );
        try
        {
            BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( f ) );
            ObjectOutputStream oos = new ObjectOutputStream( bos );
            oos.writeObject( obj );
            oos.close();
            bos.close();
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Object[] readObjectFromFile( String fileName ) throws Exception
    {
        File f = getFile( fileName, false );

        if( f == null )
        {
            throw new Exception( "文件不存在" );
        }
        ObjectInputStream ois = new ObjectInputStream( new FileInputStream( f ) );
        try
        {
            Object ob = ois.readObject();
            ois.close();
            return (Object[]) ob;

        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }

        return null;
    }

    public static Object readObjectFromFile2( String fileName ) throws Exception
    {
        try
        {
            File f = new File( fileName );

            if( f.exists() == false )
            {
                return null;
            }
            BufferedInputStream bis = new BufferedInputStream( new FileInputStream( f ) );
            ObjectInputStream ois = new ObjectInputStream( bis );
            Object obj = ois.readObject();
            ois.close();
            bis.close();
            return obj;
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * 生成指定caseid的指定类型,指定存储路径的文件路径名
     * 
     * @param filePath
     * @param caseid
     * @param type
     * @return
     */
    public static String getFileName( String filePath, String caseid, String type )
    {
        StringBuffer path = new StringBuffer( filePath );
        String pid = caseid.substring( 0, caseid.indexOf( "(" ) );

        int index = 0;
        for( int i = 1; index <= 4; i++ )
        {
            if( i > pid.length() && index <= 4 )
            {
                if( i % 2 == 0 )
                {
                    path.append( "0" );
                }
                else
                {
                    if( index == 4 )
                    {
                        path.append( "\\" + caseid );
                        index++;
                    }
                    else
                    {
                        path.append( "\\" + 0 );
                        index++;
                    }
                }
                continue;
            }

            if( i % 2 == 0 )
            {
                path.append( pid.charAt( pid.length() - i ) );
            }
            else if( index < 4 )
            {
                path.append( "\\" + pid.charAt( pid.length() - i ) );
                index++;
            }
            if( i == 8 && index <= 4 )
            {
                path.append( pid.substring( 0, pid.length() - i ) + "\\" + caseid );
                index++;
            }
        }
        return path.toString();
    }

    public static void main( String[] args )
    {
        // Print.out( FileUtil.getFilesFromDirbyPrefix(
        // "D:/2F/huge/output/exam_master(out)0406/", "da" ) );

        // try
        // {
        // Print.out( readXLS( "d:/2f/xls/oper.xlsx", "手术" ) );
        // }
        // catch( Throwable e )
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // String[] dirs = FileUtil.getFilesFromSubDirbyPrefix( "d:\\", "aa" );

    }
}
