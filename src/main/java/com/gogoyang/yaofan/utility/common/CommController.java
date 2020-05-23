package com.gogoyang.yaofan.utility.common;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.gogoyang.yaofan.controller.vo.Response;
import com.gogoyang.yaofan.utility.GogoTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/yaofanapi/common")
public class CommController {
    private final ICommonBusinessService iCommonBusinessService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 文件服务器域名
     */
    @Value("${domain.name}")
    private String domainName;

    @Value("${cbs.imagesPath}")
    private String theSetDir; //全局配置文件中设置的图片的路径

    public CommController(ICommonBusinessService iCommonBusinessService) {
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("/uploadFile")
    public Response uploadFile(@RequestParam(value = "file") MultipartFile file,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String fPath = "gogopics/yaofan";
            String fileName = file.getOriginalFilename();
            fileName = fPath + "/" + fileName;

            String endpoint = "https://oss-cn-shenzhen.aliyuncs.com";

            /**
             * todo
             * 在时空笔记上开发
             * 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，
             * 请登录 https://ram.console.aliyun.com 创建。
             *
             */
            String accessKeyId = "0O9R0XlXcfYcPUwB";
            String accessKeySecret = "Vhx4wv8LUrpkQkeLQ0BYKMPg9KV950";

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 上传文件流。
            //创建缓存文件
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            InputStream inputStream = new FileInputStream(f);
            ossClient.putObject("gogoyangbucket", fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();
            Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
            String url = ossClient.generatePresignedUrl("gogoyangbucket", fileName, expiration).toString();
            f.deleteOnExit();

            Map in =new HashMap();
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("url", url);
            in.put("fileName", fileName);

            Map out=iCommonBusinessService.createFileLog(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setCode(10001);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }
}
