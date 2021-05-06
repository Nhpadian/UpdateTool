package com.xinglin.update.util;

import org.apache.commons.logging.Log;

import com.lansle.lang.LogFactory;
import com.lansle.mast.servlet.StringFilter;

public class JSPUtil
{
    public static StringFilter FILTER                  = new StringFilter();

    public static Log          LOG                     = LogFactory.getLog( JSPUtil.class.getName() );

    public static String       HEAD_ERRORMESSAGE       = "error-message";

    public final static long   GB                      = 1024 * 1024 * 1024;
    public final static long   MB                      = 1024 * 1024;

    public final static long   MillisecondsOfOneDay    = 24 * 60 * 60 * 1000;
    public final static long   MillisecondsOfOneHour   = 60 * 60 * 1000;
    public final static long   MillisecondsOfOneMinute = 60 * 1000;

    public static String spanToString( long span )
    {
        if( span < 1000L )
            return span + " 毫秒";
        else if( span < MillisecondsOfOneMinute )
            return ( (int) ( span / 1000 ) ) + " 秒, " + spanToString( span % 1000 );
        else if( span < MillisecondsOfOneHour )
            return ( (int) ( span / MillisecondsOfOneMinute ) ) + " 分钟, " + spanToString( span % MillisecondsOfOneMinute );
        else if( span < MillisecondsOfOneDay )
            return ( (int) ( span / MillisecondsOfOneHour ) ) + " 小时, " + spanToString( span % MillisecondsOfOneHour );
        else
            return ( (int) ( span / MillisecondsOfOneDay ) ) + " 天, " + spanToString( span % MillisecondsOfOneDay );
    }
}
