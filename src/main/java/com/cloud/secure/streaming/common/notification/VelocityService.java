package com.cloud.secure.streaming.common.notification;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * @author 689Cloud
 */
@Component
@Slf4j
public class VelocityService {

    public String createTemplate(ModelMap model, String template){
        try {
            Properties properties = new Properties();
            properties.setProperty("input.encoding", "UTF-8");
            properties.setProperty("output.encoding", "UTF-8");
            properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            properties.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");

            VelocityEngine velocityEngine = new VelocityEngine(properties);
            VelocityContext context = new VelocityContext();

            for (Map.Entry<String, Object> entry : model.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }

            StringWriter stringWriter = new StringWriter();

            velocityEngine.mergeTemplate("templates/" + template, "UTF-8", context, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Email body fail", e);
            return null;
        }
    }
}
