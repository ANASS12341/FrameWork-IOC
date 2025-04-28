package xml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Property {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String ref;

    // getters et setters
}
