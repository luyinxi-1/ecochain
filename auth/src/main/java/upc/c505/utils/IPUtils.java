package upc.c505.utils;


//import com.alibaba.nacos.common.utils.IPUtil;
import com.alibaba.nacos.common.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Objects;



@Slf4j
public class IPUtils {
    private static final String COMMA = ",";
    private static final String IP = "127.0.0.1";
    private static final String LOCAL_IP = "0:0:0:0:0:0:0:1";
    /**
     * x-forwarded-for是识别通过HTTP代理或负载均衡方式连接到Web服务器的客户端
     * 最原始的IP地址的HTTP请求头字段
     */
    private static final String HEADER = "x-forwarded-for";
    private static final String UNKNOWN = "unknown";
    /**
     * 经过apache http服务器的请求才会有
     * 用apache http做代理时一般会加上Proxy-Client-IP请求头
     * 而WL-Proxy-Client-IP是它的weblogic插件加上的头。
     */
    private static final String WL_IP = "WL-Proxy-Client-IP";
    private static final Integer IP_LENGTH = 15;

    /**
     * 获取IP地址
     *
     * @param request HttpServletRequest
     * @return IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader(HEADER);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals(IP) || ip.equals(LOCAL_IP)) {
                // 根据网卡获取本机配置的IP地址
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (Objects.nonNull(inetAddress)) {
                    ip = inetAddress.getHostAddress();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实的IP地址，多个IP按照','分割
        if (null != ip && ip.length() > IP_LENGTH) {
            // "***.***.***.***".length() = 15
            if (ip.indexOf(COMMA) > 0) {
                ip = ip.substring(0, ip.indexOf(COMMA));
            }
        }
        return ip;
    }


    /**
     * 根据IP地址获取城市
     * @param ip
     * @return
     */
    public static String getCityInfo(String ip) throws IOException {
        URL url = IPUtil.class.getResource("/ip2region.db");
        File file;
        file = new File("/app/project/croot_rims/package/webserver/ip2region.db");
        if (!file.exists()) {
            System.out.println("Error: Invalid ip2region.db file, filePath：" + file.getPath());
            return null;
        }
        //查询算法
        int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
        //DbSearcher.BINARY_ALGORITHM //Binary
        //DbSearcher.MEMORY_ALGORITYM //Memory
        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, file.getPath());
            Method method;
            switch ( algorithm )
            {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
                default:
                    return null;
            }
            DataBlock dataBlock;
            if (!Util.isIpAddress(ip)) {
                System.out.println("Error: Invalid ip address");
                return null;
            }
            dataBlock  = (DataBlock) method.invoke(searcher, ip);
            return dataBlock.getRegion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) throws IOException {
//        getCityInfo("139.159.154.177");
    }
}

