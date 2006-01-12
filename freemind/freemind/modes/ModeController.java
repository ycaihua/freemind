/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*$Id: ModeController.java,v 1.14.14.7 2006-01-12 23:10:12 christianfoltin Exp $*/

package freemind.modes;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import freemind.controller.Controller;
import freemind.controller.MapModuleManager;
import freemind.controller.StructuredMenuHolder;
import freemind.extensions.HookFactory;
import freemind.main.FreeMindMain;
import freemind.main.XMLParseException;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

public interface ModeController  {

    public static final String NODESEPARATOR = "<nodeseparator>";
	/**
     * @param file Nowadays this is an URL to unify the behaviour of the browser and the other modes.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws XMLParseException
     * @return returns the new mode controller created for this url.
     */
    ModeController load(URL file) throws FileNotFoundException, IOException, XMLParseException;
    boolean save(File file);
    MindMap newMap();
    boolean save();
    boolean saveAs();
    void open();
    boolean close(boolean force, MapModuleManager mapModuleManager);
    
    // activation methods:
    void startupController();
    void shutdownController();
    // end activation methods.
    
    // listener -> controller handling
    void doubleClick(MouseEvent e);
    void plainClick(MouseEvent e);
    /** This method is used to hide the map "under" another opened map. 
     * In fact, should remove the focus, stop plugins, if necessary, etc. */
    void setVisible(boolean visible);

    boolean isBlocked();
    // node identifier (fc, 2.5.2004):
    /** Given a node identifier, this method returns the corresponding node. */
    NodeAdapter getNodeFromID(String nodeID);
    /** Calling this method the map-unique identifier of the node is returned 
     * (and created before, if not present)*/
    String getNodeID(MindMapNode selected);

    /** Single selection: the node is the only one selected after calling this method.
     * @param node
     */
    public void select( NodeView node) ;

    /**Single selection: the node is the only one selected after calling this method.
     * @param selected
     */
    public void select( MindMapNode selected);


    MindMapNode getSelected();
	/**
	 * @return a List of MindMapNode s.
	 */
	List getSelecteds();
	/** @return a LinkedList of MindMapNodes ordered by depth. nodes with greater depth occur first. */
    List getSelectedsByDepth();
    /** nodes with greater depth occur first.
	 * @param inPlaceList the given list is sorted by reference.
	 */
    public void sortNodesByDepth(List inPlaceList) ;
	    /** This extends the currently selected nodes. 
        @return true, if the method changed the selection.*/
    boolean extendSelection(MouseEvent e);
    /**
     * Invoke this method after you've changed how a node is to be
     * represented in the tree. 
     */
    void nodeChanged(MindMapNode n);
    void anotherNodeSelected(MindMapNode n);
     /** The position of this method is an exception. Normally, every method that changes
     *  nodes must be contained in the specific mode controllers but as this method
     *  is also used by the MapView to switch to neighbours (private NodeView getNeighbour(int directionCode)),
     *  we make this exception here (fc, 6.11.2005).
     * @param node
     * @param folded
     */
    void setFolded(MindMapNode node, boolean folded);
	/** Unfolds a node if necessary.
	 * @param node
	 */
	void displayNode(MindMapNode node);
	/** Node is displayed and selected as the only one selected. It is moved to the center of the
	 *  screen.
	 * @param node
	 */
	void centerNode(MindMapNode node);
	String getLinkShortText(MindMapNode node);

    public JToolBar getModeToolBar();
    /** For the toolbar on the left hand side of the window.*/
    public JToolBar getLeftToolBar();


	/** Use this method to get menus to the screen. */
	public void updateMenus(StructuredMenuHolder holder);
	public void updatePopupMenu(StructuredMenuHolder holder);

    JPopupMenu getPopupMenu();
    void showPopupMenu(MouseEvent e);
    /** This returns a context menu for an object placed in the background pane.*/
    JPopupMenu getPopupForModel(java.lang.Object obj);

	FreeMindMain getFrame();
	MapView getView(); 
	MapAdapter getMap();
	/** This method must only be used by the model itself at creation time. 
	 * Don't use this method.
	 * @param model
	 */
	void setModel(MapAdapter model);
	Mode getMode();
	Controller getController();
	HookFactory getHookFactory();
	Color getSelectionColor();
    /**
     * Get text from resource file
     */
    String getText(String textId);
    URL getResource(String path);

}
