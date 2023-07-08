package sugar_spring_framework.beans.factory.support;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sugar_spring_framework.beans.BeanException;
import sugar_spring_framework.beans.PropertyValue;
import sugar_spring_framework.beans.factory.config.BeanDefinition;
import sugar_spring_framework.beans.factory.config.BeanReference;
import sugar_spring_framework.core.io.Resource;
import sugar_spring_framework.core.io.ResourceLoader;
import sugar_spring_framework.utils.XmlUtils;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    protected XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    protected XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader loader) {
        super(registry, loader);
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeanException {

    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeanException {
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeanException {

    }
    protected void doLoadBeanDefinition(InputStream inputStream) throws IOException, SAXException, ClassNotFoundException {
        Document document = XmlUtils.readXml(inputStream);
        NodeList beans = document.getElementsByTagName("beans");
        if(beans.getLength() == 0){
            throw new RuntimeException("not found beans tag from:"+inputStream.toString());
        }
        Node all = beans.item(0);
        NodeList beanList = all.getChildNodes();
        for (int i = 0; i < beanList.getLength(); i++) {
            if(beanList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if(!"bean".equals(beanList.item(i).getNodeName())){
                continue;
            }
            Element bean = (Element) beanList.item(i);
            //metaData提取
            String id = bean.getAttribute("id");
            //todo name起别名以" "和 ","和 ";" 作为分隔,将其抽出来作为方法
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            Class<?> clazz = Class.forName(className);
            String beanName = "".equals(id) ? id : name;
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            NodeList properties = bean.getChildNodes();

            for (int j = 0; j < properties.getLength(); j++) {
                if(beanList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                if(!"property".equals(beanList.item(i).getNodeName())){
                    continue;
                }
                Element property = (Element) properties.item(j);
                String propertyName = property.getAttribute("name");
                String propertyValue = property.getAttribute("value");
                String propertyRef = property.getAttribute("ref");
                Object value = "".equals(propertyRef) ? new BeanReference(propertyRef) : propertyValue;
                PropertyValue pv = new PropertyValue(name,value);

            }

        }

    }
}
