package com.gogoyang.yaofan.controller.wx;

import com.gogoyang.yaofan.business.user.IUserBusinessService;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.controller.wx.util.AesCbcUtil;
import com.gogoyang.yaofan.controller.wx.util.HttpRequest;
import com.gogoyang.yaofan.meta.user.entity.UserInfo;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/yaofanapi/wx")
public class WXController {
    private final ICommonBusinessService iCommonBusinessService;
    private final IUserBusinessService iUserBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${wechat.appid}")
    private String wxspAppid;

    @Value("${wechat.secret}")
    private String wxspSecret;

    @Value("${wechat.user_url}")
    private String wxUserUrl;

    public WXController(ICommonBusinessService iCommonBusinessService,
                        IUserBusinessService iUserBusinessService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iUserBusinessService = iUserBusinessService;
    }

    /**
     * 微信登录、注册
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/wxLogin")
    public Response wxLogin(@RequestBody WxRequest request) {
        Response response = new Response();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String code = request.getCode();
            String encryptedData = request.getEncryptedData();
            String iv = request.getIv();

            logMap.put("GogoActType", GogoActType.WX_LOGIN);

            if (code == null || code.length() == 0) {
                throw new Exception("20013");
            }

            // 授权（必填）
            String grant_type = "authorization_code";

            //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
            // 请求参数
            String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type="
                    + grant_type;
            // 发送请求
            String sr = HttpRequest.sendGet(wxUserUrl, params);

            // 解析相应内容（转换成json对象）
            JSONObject json = JSONObject.fromObject(sr);
            // 获取会话密钥（session_key）
            String session_key = json.get("session_key").toString();
            // 用户的唯一标识（openid）
            String openid = (String) json.get("openid");

            //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
            String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                JSONObject userInfoJSON = JSONObject.fromObject(result);
                Map userInfo = new HashMap();
                userInfo.put("openId", openid);
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                // 解密unionId & openId;
                //userInfo.put("unionId", userInfoJSON.get("unionId"));
                //todo 注册登录,根据openId查找用户，没有就注册一个
                Map out = iUserBusinessService.wxLogin(userInfo);

                response.setData(out);

                memoMap.put("result", true);
                memoMap.put("openId", openid);
                memoMap.put("userId", ((UserInfo) out.get("userInfo")).getUserId());
            } else {
                throw new Exception("20014");
            }
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
            memoMap.put("result", GogoStatus.FAILED);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActLog(logMap);
        } catch (Exception ex3) {
            logger.error("用户行为日志写入错误：" + ex3.getMessage());
        }
        return response;
    }
}
