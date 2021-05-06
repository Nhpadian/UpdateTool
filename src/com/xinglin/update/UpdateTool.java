package com.xinglin.update;

import org.apache.commons.logging.Log;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.lansle.lang.LogFactory;
import com.lansle.lang.TimeUtil;
import com.xinglin.update.util.JSPUtil;
import com.xinglin.update.util.ZipUtil;

public class UpdateTool
{
    protected static Log LOG = LogFactory.getLog( UpdateTool.class.getName() );

    @Option(name = "-old", usage = "待更新nis包")
    private String       oldNis;

    @Option(name = "-new", usage = "新的nis包")
    private String       newNis;

    private boolean validPara()
    {
        if( oldNis == null )
        {
            LOG.info( "待更新nis包参数为null，使用-old进行配置" );
            return false;
        }

        if( newNis == null )
        {
            LOG.info( "新的nis包参数为null，使用-new进行配置" );
            return false;
        }
        return true;
    }

    public static void main( String[] args )
    {
        UpdateTool tool = new UpdateTool();
        CmdLineParser parser = new CmdLineParser( tool );
        try
        {
            parser.parseArgument( args );

            if( tool.validPara() == true )
            {

            }
        }
        catch( CmdLineException e )
        {
            LOG.info( e.getMessage() );
        }
        LOG.info( "开始更新" );

        LOG.info( "步骤一 ：原nis包备份..." );
        tool.backup();

    }

    private void backup()
    {
        try
        {
            LOG.info( "开始压缩" );
            long s = System.currentTimeMillis();
            ZipUtil.compressFileList( oldNis + "_" + TimeUtil.format( System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss" ) + ".zip", oldNis );
            LOG.info( "压缩结束" );
            LOG.info( "耗时：" + JSPUtil.spanToString( System.currentTimeMillis() - s ) );

        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

    }
}
