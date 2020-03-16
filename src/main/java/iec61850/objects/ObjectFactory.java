
package iec61850.objects;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


@XmlRegistry
public class ObjectFactory {

    private final static QName _Substation_QNAME = new QName("http://www.iec.ch/61850/2006/SCL", "Substation");

    public ObjectFactory() {
    }

    public THeader createTHeader() {
        return new THeader();
    }

    public TSubstation createTSubstation() {
        return new TSubstation();
    }

    public TBitRateInMbPerSec createTBitRateInMbPerSec() {
        return new TBitRateInMbPerSec();
    }

    public TConductingEquipment createTConductingEquipment() {
        return new TConductingEquipment();
    }

    public TBay createTBay() {
        return new TBay();
    }

    public TSubFunction createTSubFunction() {
        return new TSubFunction();
    }

    public TVoltage createTVoltage() {
        return new TVoltage();
    }

    public TPowerTransformer createTPowerTransformer() {
        return new TPowerTransformer();
    }

    public TVoltageLevel createTVoltageLevel() {
        return new TVoltageLevel();
    }

    public TVal createTVal() {
        return new TVal();
    }

    public TLNode createTLNode() {
        return new TLNode();
    }

    public TTransformerWinding createTTransformerWinding() {
        return new TTransformerWinding();
    }

    public TValueWithUnit createTValueWithUnit() {
        return new TValueWithUnit();
    }

    public TFunction createTFunction() {
        return new TFunction();
    }

    public TText createTText() {
        return new TText();
    }

    public TGeneralEquipment createTGeneralEquipment() {
        return new TGeneralEquipment();
    }

    public TConnectivityNode createTConnectivityNode() {
        return new TConnectivityNode();
    }

    public TTapChanger createTTapChanger() {
        return new TTapChanger();
    }

    public TDurationInSec createTDurationInSec() {
        return new TDurationInSec();
    }

    public TTerminal createTTerminal() {
        return new TTerminal();
    }

    public TPrivate createTPrivate() {
        return new TPrivate();
    }

    public TDurationInMilliSec createTDurationInMilliSec() {
        return new TDurationInMilliSec();
    }

    public TSubEquipment createTSubEquipment() {
        return new TSubEquipment();
    }

    public THitem createTHitem() {
        return new THitem();
    }

    public THeader.History createTHeaderHistory() {
        return new THeader.History();
    }

    @XmlElementDecl(namespace = "http://www.iec.ch/61850/2006/SCL", name = "Substation")
    public JAXBElement<TSubstation> createSubstation(TSubstation value) {
        return new JAXBElement<TSubstation>(_Substation_QNAME, TSubstation.class, null, value);
    }

}
