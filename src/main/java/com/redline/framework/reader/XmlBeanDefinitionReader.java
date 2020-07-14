package com.redline.framework.reader;

import com.redline.framework.DocumentUtils;
import com.redline.framework.registry.BeanDefinitionRegistry;
import org.dom4j.Document;

import java.io.InputStream;

public class XmlBeanDefinitionReader {
    private BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void registerBeanDefinitions(InputStream inputStream) {
        Document document = DocumentUtils.getDocument(inputStream);

        XmlBeanDefinitionDocumentReader documentReader = new XmlBeanDefinitionDocumentReader(beanDefinitionRegistry);
        documentReader.loadBeanDefinitions(document.getRootElement());
    }
}
