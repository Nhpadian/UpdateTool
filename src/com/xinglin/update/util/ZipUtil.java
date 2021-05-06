
package com.xinglin.update.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.input.NullInputStream;


/**
 * 
 * 压缩工具
 * 
 * @author <a href="mailto:msk@xinglin-tech.com">缪顺科</a>
 * @since 2021-04-28
 */
public class ZipUtil
{

    /**
     * 批量压缩文件 v4.0
     *
     * @param filepath 需要压缩的文件名称
     * @param zipOutName 压缩后的文件名称
     **/
    public static void compressFileList( String zipOutName, String filepath ) throws IOException, ExecutionException, InterruptedException
    {
        long s0 = System.currentTimeMillis();

        String[] filenames = FileUtil.getFilesFromSubDirbyPrefix( filepath );
        System.out.println(filenames.length);
//        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat( "compressFileList-pool-" ).build();
//        ExecutorService executor = new ThreadPoolExecutor( 5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>( 20 ), factory );
//        ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
//                60L, TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>());
        
        int cpuNums = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool( cpuNums );
        
        ParallelScatterZipCreator parallelScatterZipCreator = new ParallelScatterZipCreator( executor );
        OutputStream outputStream = new FileOutputStream( zipOutName );
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream( outputStream );
        zipArchiveOutputStream.setEncoding( "UTF-8" );
        for( String fileName : filenames )
        {
            File inFile = new File( fileName );
            if( fileName.indexOf( "busy" )> -1 )
            {
                System.out.println(inFile.getName());
                System.out.println(  fileName.toString().replace( new File( filepath ).getParent(), "" )  );
            }

            final InputStreamSupplier inputStreamSupplier = () -> {
                try
                {
                    return new FileInputStream( inFile );
                }
                catch( FileNotFoundException e )
                {
                    e.printStackTrace();
                    return new NullInputStream( 0 );
                }
            };
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry( fileName.toString().replace( new File( filepath ).getParent(), "" ) );
            zipArchiveEntry.setMethod( ZipArchiveEntry.DEFLATED );
            zipArchiveEntry.setSize( inFile.length() );
            zipArchiveEntry.setUnixMode( UnixStat.FILE_FLAG | 436 );
            parallelScatterZipCreator.addArchiveEntry( zipArchiveEntry, inputStreamSupplier );
        }
        parallelScatterZipCreator.writeTo( zipArchiveOutputStream );
        zipArchiveOutputStream.close();
        outputStream.close();
        
        executor.shutdown();

        while( true )
        {
            if( executor.isTerminated() )
            {
                long end = System.currentTimeMillis();
                System.out.println( cpuNums + "个线程去压缩文件夹用时: " + ( end - s0 ) + "ms" );
                break;
            }
        }
//        LOG.info( "ParallelCompressUtil->ParallelCompressUtil-> info:{}", JSONObject.toJSONString( parallelScatterZipCreator.getStatisticsMessage() ) );
    }

}
