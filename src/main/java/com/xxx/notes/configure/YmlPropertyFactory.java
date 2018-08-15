package com.xxx.notes.configure;


import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;

/**
 * @ClassName YmlPropertyFactory
 * @Description 加载自定义的yml文件的工厂类
 * @Author l17561
 * @Date 2018/7/4 17:02
 * @Version V1.0
 */
public class YmlPropertyFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {

        if (encodedResource == null) {

            return super.createPropertySource(name, encodedResource);
        } else {
            return name != null ? new YamlPropertySourceLoader().load(name, encodedResource.getResource()).get(0) :
                    new YamlPropertySourceLoader().load(encodedResource.getResource().getFilename(), encodedResource.getResource()).get(0);
        }
    }
}