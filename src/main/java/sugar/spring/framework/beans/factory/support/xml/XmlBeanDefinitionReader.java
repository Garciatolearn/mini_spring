package sugar.spring.framework.beans.factory.support.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.PropertyValue;
import sugar.spring.framework.beans.PropertyValues;
import sugar.spring.framework.beans.factory.config.BeanDefinition;
import sugar.spring.framework.beans.factory.config.BeanReference;
import sugar.spring.framework.beans.factory.support.AbstractBeanDefinitionReader;
import sugar.spring.framework.core.io.Resource;
import sugar.spring.framework.utils.XmlUtils;
import sugar.spring.framework.beans.factory.support.BeanDefinitionRegistry;
import sugar.spring.framework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    protected XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader loader) {
        super(registry, loader);
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeanException {
        ResourceLoader loader = getResourceLoader();
        Resource resource = loader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeanException {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinition(inputStream);
        } catch (ClassNotFoundException | SAXException |IOException e) {
            throw new RuntimeException("Exception about parse xml document from:"+e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeanException {
        for (Resource resource:
             resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeanException {
        for (String location:
             locations) {
            loadBeanDefinitions(location);
        }
    }

    protected void doLoadBeanDefinition(InputStream inputStream) throws IOException, SAXException, ClassNotFoundException {
        Document document = XmlUtils.readXml(inputStream);
        NodeList beans = document.getElementsByTagName("beans");
        if (beans.getLength() == 0) {
            throw new RuntimeException("not found beans tag from:" + inputStream.toString());
        }
        Node all = beans.item(0);
        NodeList beanList = all.getChildNodes();
        for (int i = 0; i < beanList.getLength(); i++) {
            if (beanList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (!"bean".equals(beanList.item(i).getNodeName())) {
                continue;
            }
            Element bean = (Element) beanList.item(i);
            //metaData提取
            String id = bean.getAttribute("id");
            //todo name起别名以" "和 ","和 ";" 作为分隔,将其抽出来作为方法
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            Class<?> clazz = Class.forName(className);
            String beanName = "".equals(id) ? name : id;
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            NodeList properties = bean.getChildNodes();
            PropertyValues propertyValues = new PropertyValues();
            for (int j = 0; j < properties.getLength(); j++) {
                if (properties.item(j).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                if (!"property".equals(properties.item(j).getNodeName())) {
                    continue;
                }
                Element property = (Element) properties.item(j);
                String propertyName = property.getAttribute("name");
                String propertyValue = property.getAttribute("value");
                String propertyRef = property.getAttribute("ref");
                Object value = "".equals(propertyRef) ? propertyValue : new BeanReference(propertyRef);
                PropertyValue pv = new PropertyValue(propertyName, value);
                propertyValues.addPropertyValue(pv);
            }
            beanDefinition.setPropertyValues(propertyValues);
            BeanDefinitionRegistry registry = this.getRegistry();
            registry.registerDefinition(beanName, beanDefinition);
        }

    }

}
