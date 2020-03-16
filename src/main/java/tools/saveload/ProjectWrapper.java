package tools.saveload;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="cldproject")
@XmlAccessorType(XmlAccessType.NONE)
public class ProjectWrapper {

//	@XmlElement(name = "equipment")
//	private ArrayList<Equipment> equipments;
//	@XmlElement(name = "link")
//	private ArrayList<LinkData> links;

	public void getCurrentProject(){
//		equipments = new ArrayList<>();
//		links = new ArrayList<>();
//		for(Node node:CurrentProject.getElements()){
//			if(node.getClass()==EquipmentNode.class) equipments.add(((EquipmentNode)node).getEquipment());
//			if(node.getClass()==Link.class) { Link l = ((Link)node); links.add(new LinkData(l.getSourceID(),l.getTargetID(),l.getSourceConnectorID(),l.getTargetConnectorID())); }
//		}
	}

	public void setCurrentProject(){
//		CurrentProject.clear();
//		ArrayList<EquipmentNode> enode = new ArrayList<>();
//		for(Equipment eq:equipments){
//			double x = eq.getLayoutX(), y = eq.getLayoutY();
//			EquipmentNode node = new EquipmentNode(eq);
//			CurrentProject.getElements().add(node);
//			node.relocate(x,y);
//			enode.add(node);
//		}
//		EquipmentNode source = null, target = null;
//		AnchorPane sourceConnector = null, targetConnector = null;
//		boolean sourceFound = false, targetFound = false;
//		for(LinkData ld:links){
//			sourceFound = false; targetFound = false;
//			for(EquipmentNode node:enode){
//				if(node.getId().equals(ld.getSourceID())){
//					source = node;
//					for(AnchorPane con:node.getConnectors()){
//						if(con.getId().equals(ld.getSourceConnectorID())){ sourceConnector = con; sourceFound = true; break; }
//					}
//				}
//				if(node.getId().equals(ld.getTargetID())){
//					target = node;
//					for(AnchorPane con:node.getConnectors()){
//						if(con.getId().equals(ld.getTargetConnectorID())){ targetConnector = con; targetFound = true; break; }
//					}
//				}
//				if(targetFound && sourceFound) break;
//			}
//			if(targetFound && sourceFound){
//				Linker.addSource2Link(source,sourceConnector);
//				Linker.addTarget2Link(target, targetConnector);
//				Linker.createConnection();
//				Linker.clearLink();
//			}
//		}
	}

//	public ArrayList<Equipment> getEquipments() {
//		return equipments;
//	}
//	public void setEquipments(ArrayList<Equipment> equipments) {
//		this.equipments = equipments;
//	}
//
//	public ArrayList<LinkData> getLinks() {
//		return links;
//	}
//	public void setLinks(ArrayList<LinkData> links) {
//		this.links = links;
//	}
}
