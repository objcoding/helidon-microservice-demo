package com.objcoding.helidon;

import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 2018/9/18
 */
public class GreetService implements Service {

    /**
     * 读取配置文件中的 “app”字段 部分配置
     */
    private static final Config CONFIG = Config.create().get("app");

    /**
     * 读取配置文件中 “app”下层属性“greeting”字段属性值
     */
    private static String greeting = CONFIG.get("greeting").asString("Hello xs");

    /**
     * 定义当前Service 路由规则
     */
    @Override
    public final void update(final Routing.Rules rules) {
        rules
                .get("/", this::getDefaultMessage)
                .get("/{name}", this::getMessage)
                .put("/greeting/{greeting}", this::updateGreeting);
    }

    /**
     * 测试: http://localhost:8080/greet/  (get 请求)
     */
    private void getDefaultMessage(final ServerRequest request, final ServerResponse response) {
        String msg = String.format("%s %s!", greeting, "World");

        JsonObject returnObject = Json.createObjectBuilder()
                .add("message", msg)
                .build();
        response.send(returnObject);
    }

    /**
     * 测试: http://localhost:8080/greet/xs          (get 请求)
     * 测试: http://localhost:8080/greet/xs?age=18   (get 请求)
     */
    private void getMessage(final ServerRequest request, final ServerResponse response) {
        //获取问号后面的查询参数
        String age = request.queryParams().first("age").orElse("age 未提供！！！");
        //获取路径参数
        String name = request.path().param("name");
        String msg =  name+": "+age;
        JsonObject returnObject = Json.createObjectBuilder()
                .add("message", msg)
                .build();
        response.send(returnObject);
    }

    /**
     * 测试: http://localhost:8080/greet/xs?age=18  （put 请求）
     */
    private void updateGreeting(final ServerRequest request, final ServerResponse response) {
        greeting = request.path().param("greeting");
        JsonObject returnObject = Json.createObjectBuilder()
                .add("greeting", greeting)
                .build();
        response.send(returnObject);
    }

}
