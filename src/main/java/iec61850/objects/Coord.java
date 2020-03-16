
package iec61850.objects;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "Coord", namespace = "http://www.iec.ch/61850/2006/SCLcoordinates")
public class Coord {

    @XmlAttribute(name = "x", namespace = "http://www.iec.ch/61850/2006/SCLcoordinates", required = true)
    protected int x;
    @XmlAttribute(name = "y", namespace = "http://www.iec.ch/61850/2006/SCLcoordinates", required = true)
    protected int y;
    @XmlAttribute(name = "dir", namespace = "http://www.iec.ch/61850/2006/SCLcoordinates")
    protected TConndir dir;

    public int getX() {
        return x;
    }

    public void setX(int value) {
        this.x = value;
    }

    public int getY() {
        return y;
    }

    public void setY(int value) {
        this.y = value;
    }

    public TConndir getDir() {
        return dir;
    }

    public void setDir(TConndir value) {
        this.dir = value;
    }

}
