package web2.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "", propOrder = { "code", "message" })
public class Violation {

    @XmlElement(name = "code")
    private String code;

    @XmlElement(name = "message")
    private String message;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
