package com.acmr.model.templatecenter;

import java.util.ArrayList;
import java.util.List;
public class Tnode {

	private String strCode;
	private String strName;
	private String ifdata;
	private Tnode objParent;
	private List<Tnode> objTnodes;
	private String  uniqueID;
    private String unit;
    private String unitName;
    private Boolean ifhidden;
    public Boolean getIfhidden() {
		return ifhidden;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public void setIfhidden(Boolean ifhidden) {
		this.ifhidden = ifhidden;
	}

	public Boolean getIfcorner() {
		return ifcorner;
	}

	public void setIfcorner(Boolean ifcorner) {
		this.ifcorner = ifcorner;
	}


	private Tnode hideTnode;
    private Boolean ifcorner;

	public Tnode getHideTnode() {
		return hideTnode;
	}

	public void setHideTnode(Tnode hideTnode) {
		this.hideTnode = hideTnode;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public Tnode(){
    	super();
    	this.objTnodes=new ArrayList<Tnode>();
    }
    
	public Tnode(String strCode, String strName, String ifdata
		) {
		super();
		this.strCode = strCode;
		this.strName = strName;
		this.ifdata = ifdata;
		this.objTnodes=new ArrayList<Tnode>();
		this.objParent = null;
		this.ifhidden=false;
		this.ifcorner=false;
	}
	public void addChild(Tnode node1) {
		node1.objParent=this;
		objTnodes.add(node1);
		Modify();
	}
	
	public void addChild(Tnode node1,int index) {
		node1.objParent=this;
		objTnodes.add(index,node1);	
		Modify();
	}
	
	public void addChildWithNoCorner(Tnode node1,int index) {
		node1.objParent=this;
		objTnodes.add(index,node1);	
	}
	
	public void addChildWithNoCorner(Tnode node1) {
		node1.objParent=this;
		objTnodes.add(node1);
	}
	
	public void Modify(){
		if(this.objTnodes.size()<=1||this.getIfcorner()){
			return ;
		}
		for(int i=0;i<this.objTnodes.size();i++){
			Tnode node1=this.objTnodes.get(i);
			if(this.strCode.equals(node1.getStrCode())){
				this.setHideTnode(node1);
				node1.setIfhidden(true);
				this.setIfcorner(true);
				this.objTnodes.remove(i);
				this.objTnodes.add(0, node1);
			}
		}
	}
	
	
	public void Modify(Tnode node){
		if(node.getObjTnodes().size()<=1){
			return ;
		}
		for(int i=0;i<node.getObjTnodes().size();i++){
			Tnode node1=node.getObjTnodes().get(i);
			if(node.getStrCode().equals(node1.getStrCode())){
				node.setHideTnode(node1);
				node1.setIfhidden(true);
				node.setIfcorner(true);
				node.getObjTnodes().remove(i);
				node.getObjTnodes().add(0, node1);
			}
		}
	}
	
	public void addParent(Tnode node1) {
		Tnode parentNode=this.getObjParent();
		deleteNode();
        node1.addChild(this);
        parentNode.addChild(node1);	
	}
	public int level(){
		int l=0;
		Tnode node1=this.objParent;
		while (node1!=null){
			l+=1;
			node1=node1.objParent;
		}
		return l;	
	}	
		
	public String getStrCode() {
		return strCode;
	}

	public void setStrCode(String strCode) {
		this.strCode = strCode;
	}
	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getIfdata() {
		return ifdata;
	}

	public void setIfdata(String ifdata) {
		this.ifdata = ifdata;
	}

	public Tnode getObjParent() {
		return objParent;
	}

	public void setObjParent(Tnode objParent) {
		this.objParent = objParent;
	}

	public List<Tnode> getObjTnodes() {
		return objTnodes;
	}

	public void setObjTnodes(List<Tnode> objTnodes) {
		this.objTnodes = objTnodes;
	}

	public Tnode NextNode(){
		Tnode node1=this.objParent;
		if(node1==null){
			return null;
		}
		int i=node1.objTnodes.indexOf(this);
		return node1.objTnodes.get(i+1);
	}
	
    public boolean isLeaf() {
        if (objTnodes.size()==0) {
            return true;
        }
        else{
        	return false;
        }
    }

    /* 删除节点和它下面的节点 */
    public void deleteNode() {
        Tnode parentNode = this.getObjParent();
        String id = this.getStrCode();
        if (parentNode != null) {
            parentNode.deleteChildNode(id);
        }
    }
 
    /* 删除当前节点的某个子节点 */
    public void deleteChildNode(String childId) {
        List<Tnode> childList = this.getObjTnodes();
        int childNumber = childList.size();
        for (int i = 0; i < childNumber; i++) {
        	Tnode child = childList.get(i);
            if (child.getStrCode() == childId) {
                childList.remove(i);
                return;
            }
        }
    }
   
   
	public static void main(String[] args) {
		
	Tnode node1=new Tnode("root","root","0");		
	Tnode node2 =new Tnode("001","aaa","1");
    Tnode node3 =new Tnode("002","bbb","1");
    Tnode node4 =new Tnode("003","ccc","1");
    node3.addChild(node4);
	node2.addChild(node3);
	node1.addChild(node2);
	
    System.out.println(node4.level());
	
	}

}
