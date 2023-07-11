package sugar.spring.framework.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Garcia
 * @since 2022.7.6
 */
public class PropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue pv) {
        this.propertyValueList.add(pv);
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String valueName) {
        for (PropertyValue propertyValue :
                this.propertyValueList) {
            if (propertyValue.getName().equals(valueName)) {
                return propertyValue;
            }
        }
        return null;
    }
}
