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

    @Option(name = "-old", usage = "������nis��")
    private String       oldNis;

    @Option(name = "-new", usage = "�µ�nis��")
    private String       newNis;

    private boolean validPara()
    {
        if( oldNis == null )
        {
            LOG.info( "������nis������Ϊnull��ʹ��-old��������" );
            return false;
        }

        if( newNis == null )
        {
            LOG.info( "�µ�nis������Ϊnull��ʹ��-new��������" );
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
        LOG.info( "��ʼ����" );

        LOG.info( "����һ ��ԭnis������..." );
        tool.backup();

    }

    private void backup()
    {
        try
        {
            LOG.info( "��ʼѹ��" );
            long s = System.currentTimeMillis();
            ZipUtil.compressFileList( oldNis + "_" + TimeUtil.format( System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss" ) + ".zip", oldNis );
            LOG.info( "ѹ������" );
            LOG.info( "��ʱ��" + JSPUtil.spanToString( System.currentTimeMillis() - s ) );

        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

    }
}
