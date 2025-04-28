package xml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Bean {
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String className;
    @XmlElement(name = "property")
    private List<Property> properties;

    // getters et setters
}
