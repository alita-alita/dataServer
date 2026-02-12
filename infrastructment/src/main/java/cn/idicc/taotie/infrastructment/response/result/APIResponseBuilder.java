package cn.idicc.taotie.infrastructment.response.result;

public class APIResponseBuilder {

    public static APIResponse success() {
        return new APIResponse(200, "success");
    }

    public static APIResponse success(Object obj) {
        return new APIResponse(200, "success",obj);
    }

    public static APIResponse fail(int code, String msg) {
        return new APIResponse(code, msg);
    }

}
