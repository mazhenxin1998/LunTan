package com.example.myboot07.provide;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author lenovo
 */
@Component
public class UCloudProvice {

    @Value("${ucloud.publickey}")
    private String publicKey;

    @Value("${ucloud.privatekey}")
    private String privateKey;

    private String bucketName = "kaixin-boke";
    /**
     * 创建上传文件的方法
     * <p>
     * 返回上传文件的最终URL地址(猜想)
     * mimeType :  文件类型
     * <p>
     * Spring 上传文件的机制是通过流来实现的
     *
     *      fileInputStream流 执行要存入文件的流
     *
     *   这个是根据流来实现
     */
    public String upload(InputStream fileInputStream, String mimeType, String fileName) {

        //  对象相关API授权器   如果要调用UCloud接口API那么  那么就必须声明
        ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(publicKey, privateKey);

        //  对象操作需要objectConfig来配置您的地区和域名
        ObjectConfig config = new ObjectConfig("cn-bj", "ufileos.com");

        // 对上传到对象存储上的名字重新命名  使每一个文件名字都不相同 这里使用了UUID
        String[] filePaths = fileName.split("\\.");
        String generatedFileName = "";
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        }

        try {
            //  该对象应该是向云对象存储上存储对象时使用的 参数1： 对象相关授权API  参数2：  该次上传文件请求时的配置要求 其中包含了地区和域名后缀
            PutObjectResultBean response = UfileClient.object(OBJECT_AUTHORIZER, config)
                    // 要将那个文件和该文件类型 上传到服务器上
                    .putObject(fileInputStream, mimeType)
                    //  上传文件之后用什么名字保存
                    .nameAs(generatedFileName)
                    // 发送到那个bucket上 也就是自己注册的那个kaixin-boke
                    .toBucket("kaixin-boke")
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener((bytesWritten, contentLength) -> {
                    })
                    .execute();

                    //  如果成功上传成功   设置访问连接的 后面的参数0代表永久访问
                    if (response != null && response.getRetCode() == 0) {
                            String url = UfileClient.object(OBJECT_AUTHORIZER, config)
                                // keyName 就是要获取的文件的名字    第三个参数有效时间没起作用
                                .getDownloadUrlFromPrivateBucket(generatedFileName,"kaixin-boke", 1000*24*60*60*365*10)
                                .createUrl();

                            //  注意事项: 该URL是有 有效时间的 但是仓库里面的URL 每次都不一样
                        return url;
                    }else{
                        // 应该抛异常
                        return null;
                    }

        } catch (UfileClientException e) {
            e.printStackTrace();
            return null;
        } catch (UfileServerException e) {
            e.printStackTrace();
            return null;
        }

    }



}
