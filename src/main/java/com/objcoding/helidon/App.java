package com.objcoding.helidon;

import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.json.JsonSupport;

import java.io.IOException;
import java.util.logging.LogManager;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 2018/9/18
 */
public class App {

    /**
     * 创建路由
     *
     * @return the new instance
     */
    private static Routing createRouting() {
        return Routing.builder()
                //添加json返回支持
                .register(JsonSupport.get())
                .register("/greet", new GreetService())
                .build();
    }

    public static void main(final String[] args) throws IOException {
        // 加载日志配置
        LogManager.getLogManager().readConfiguration(
                App.class.getResourceAsStream("/logging.properties"));

        //默认会从类路径加载 application.yaml 配置文件
        Config config = Config.create();

        //获取application.yaml中 “server”部分webserver相关配置
        ServerConfiguration serverConfig =
                ServerConfiguration.fromConfig(config.get("server"));

        //根据配置创建WebServer
        WebServer server = WebServer.create(serverConfig, createRouting());

        // Start the server and print some info.
        // 开启服务时打印一些内容
        server.start().thenAccept(ws -> System.out.println(
                "WEB server is up! http://localhost:" + ws.port()));

        // 服务停止时打印一些内容 （ 可以通过执行server.shutdown()关闭服务触发）
        server.whenShutdown().thenRun(()
                -> System.out.println("WEB server is DOWN. Good bye!"));

    }

}
