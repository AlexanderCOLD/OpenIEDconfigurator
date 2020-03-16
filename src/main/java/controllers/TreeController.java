package controllers;

import iec61850.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description
 */
public class TreeController {

    private static TreeView tree;
    private static Image rootIcon = new Image(TreeController.class.getResourceAsStream("/view/image/proj.png"));
    private static Image iedIcon = new Image(TreeController.class.getResourceAsStream("/view/image/ied.png"));
    private static Image ldIcon = new Image(TreeController.class.getResourceAsStream("/view/image/ld.png"));
    private static Image lnIcon = new Image(TreeController.class.getResourceAsStream("/view/image/ln.png"));
    private static Image datasetIcon = new Image(TreeController.class.getResourceAsStream("/view/image/dataset.png"));
    private static Image dataIcon = new Image(TreeController.class.getResourceAsStream("/view/image/data.png"));

    public static void updateTree(ArrayList<IED> ieds){

         tree.setRoot(buildTree(ieds));
    }

    private static TreeItem buildTree(ArrayList<IED> ieds){
        TreeItem root = new TreeItem("Project", new ImageView(rootIcon));
        root.setExpanded(true);

        TreeItem iedItem = null, ldItem = null, dsItem = null, lnItem = null, doItem = null;

        for(IED ied:ieds){
            iedItem = new TreeItem(ied, new ImageView(iedIcon));
            iedItem.setExpanded(true);
            for(LD ld:ied.getLdList()){
                ldItem = new TreeItem(ld, new ImageView(ldIcon));
                ldItem.setExpanded(true);

                for(LN ln:ld.getLnList()){
                    lnItem = new TreeItem(ln, new ImageView(lnIcon));
                    ldItem.getChildren().add(lnItem);
                }

                for(DataSet ds:ld.getGooseOutputDataSets()){
                    dsItem = new TreeItem(ld, new ImageView(datasetIcon));
                    dsItem.setExpanded(true);

                    for(DO dobj:ds.getDoList()){
                        doItem = new TreeItem(ld, new ImageView(dataIcon));
                        dsItem.getChildren().add(doItem);
                    }
                    ldItem.getChildren().add(dsItem);
                }

                for(DataSet ds:ld.getGooseInputDataSets()){
                    dsItem = new TreeItem(ld, new ImageView(datasetIcon));
                    dsItem.setExpanded(true);

                    for(DO dobj:ds.getDoList()){
                        doItem = new TreeItem(ld, new ImageView(dataIcon));
                        dsItem.getChildren().add(doItem);
                    }
                    ldItem.getChildren().add(dsItem);
                }
                iedItem.getChildren().add(ldItem);
            }
            root.getChildren().add(iedItem);
        }

        return root;
    }


    public static void setTree(TreeView tree) { TreeController.tree = tree; }
}
