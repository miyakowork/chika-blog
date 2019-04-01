package me.wuwenbin.chika.model.bean;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网络传输对象
 * created by Wuwenbin on 2017/12/20 at 下午3:39
 *
 * @author wuwenbin
 * @since 1.10.5.RELEASE
 */
public class Result extends ConcurrentHashMap<String, Object> {

    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";

    public static final int SUCCESS = 200;
    public static final int LOGIN_INVALID = -1;
    public static final int SERVER_ERROR = 500;

    private Result() {
        put(CODE, SUCCESS);
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 返回默认的成功响应的实体
     *
     * @return
     */
    public static Result ok() {
        return new Result();
    }

    /**
     * 返回默认的成功响应的实体，只带文本信息
     *
     * @param msg 文本信息
     * @return
     */
    public static Result ok(String msg) {
        Result nbr = new Result();
        nbr.put("message", msg == null || "".equals(msg) ? "success!" : msg);
        return nbr;
    }

    /**
     * 返回默认的成功响应的实体，只带文本信息
     *
     * @param msg   文本信息
     * @param param 替换为本占位符参数
     * @return
     */
    public static Result formatOk(String msg, Object... param) {
        Result nbr = new Result();
        nbr.put("message", msg == null || "".equals(msg) ? "success!" : StrUtil.format(msg, param));
        return nbr;
    }


    /**
     * 自定义成功响应数据，包含额外的返回数据
     *
     * @param data 额外的数据
     * @return 返回响应正确的实体
     */
    public static Result ok(Object data) {
        return ok(null, data);
    }

    /**
     * 自定义成功响应数据，包含响应信息以及额外的返回数据
     *
     * @param msg  响应信息
     * @param data 额外的西湖局
     * @return 返回响应正确的实体
     */
    public static Result ok(String msg, Object data) {
        return ok(msg).put(DATA, data);
    }

    /**
     * 自定义返回成功信息，带额外的map信息
     *
     * @param jsonMap map信息
     * @return
     */
    public static Result ok(Map<String, Object> jsonMap) {
        Result nbr = new Result();
        nbr.putAll(jsonMap);
        return nbr;
    }

    /**
     * 自定义错误响应数据，带额外的数据（指定类型，默认状态码为500
     *
     * @param message 错误信息
     * @param <T>     额外数据指定的类型
     * @return 返回响应错误的实体
     */
    public static <T> Result error(String message, T data) {
        return error(message).put(DATA, data);
    }

    /**
     * 自定义错误响应数据，默认状态码为500
     *
     * @param message 错误信息
     * @return 返回响应错误的实体
     */
    public static Result error(String message) {
        return Objects.requireNonNull(ok().put(CODE, SERVER_ERROR)).put(MESSAGE, message);
    }

    /**
     * 自定义响应数据，不带额外的参数
     *
     * @param code 状态码
     * @param data 额外数据
     * @return 静态方法，返回响应实体JSON数据
     */
    public static <T> Result custom(int code, T data) {
        return Objects.requireNonNull(ok().put(CODE, code)).put(DATA, data);
    }

    /**
     * 自定义响应数据，不带额外的参数
     *
     * @param code    状态码
     * @param message 响应信息
     * @return 静态方法，返回响应实体JSON数据
     */
    public static Result custom(int code, String message) {
        return Objects.requireNonNull(ok().put(CODE, code)).put(MESSAGE, message);
    }

    /**
     * 自定义响应数据，不带额外的参数
     *
     * @param code 状态码
     * @return 静态方法，返回响应实体JSON数据
     */
    public static Result custom(int code) {
        return custom(code, "");
    }

    /**
     * 自定义响应数据，带额外的参数（指定类型）
     *
     * @param code    状态码
     * @param message 响应信息
     * @param data    额外的数据
     * @param <T>     额外数据的指定类型
     * @return 静态方法，返回响应实体JSON数据
     */
    public static <T> Result custom(int code, String message, T data) {
        return custom(code, message).put(DATA, data);
    }

    /**
     * 判断是否成功
     *
     * @param result
     * @return
     */
    public static boolean isOk(Result result) {
        return Integer.valueOf(result.get(CODE).toString()) == SUCCESS;
    }

}
