package web2.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "", propOrder = { "parameters" })
@XmlRootElement(name = "Violations")
public class Violations {

    @XmlElement(name = "parameters")
    private Collection<Violation> violationsList = new ArrayList<>();

    public Collection<Violation> getViolationsList() { return violationsList; }
    public void setViolationsList(Collection<Violation> violationsList) { this.violationsList = violationsList; }
}
