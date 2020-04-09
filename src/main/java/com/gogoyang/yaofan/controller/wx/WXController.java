package com.gogoyang.yaofan.controller.wx;

import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.utility.GogoActType;
import com.gogoyang.yaofan.utility.GogoStatus;
import com.gogoyang.yaofan.utility.common.ICommonBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/yaofanapi/wx")
public class WXController {
    private final ICommonBusinessService iCommonBusinessService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public WXController(ICommonBusinessService iCommonBusinessService) {
        this.iCommonBusinessService = iCommonBusinessService;
    }

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

            String wxspAppid=SDKconfig

            memoMap.put("result", GogoStatus.SUCCESS);
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
