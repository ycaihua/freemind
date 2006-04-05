/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *
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
 *
 * Created on 29.09.2004
 */
/* $Id: IconAction.java,v 1.1.2.2.2.1 2006-04-05 21:26:28 dpolivaev Exp $ */

package freemind.modes.mindmapmode.actions;

import java.awt.event.ActionEvent;
import java.util.ListIterator;

import javax.swing.Action;

import freemind.controller.actions.generated.instance.AddIconAction;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.modes.MindIcon;
import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.ActorXml;


public class IconAction extends FreemindAction  implements ActorXml{
    public MindIcon icon;
    private final MindMapController modeController;
    private final RemoveLastIconAction removeLastIconAction;
    public IconAction(MindMapController controller, MindIcon _icon, RemoveLastIconAction removeLastIconAction) {
        super(_icon.getDescription(controller.getFrame()), _icon.getIcon(), controller);
        this.modeController = controller;
        this.removeLastIconAction = removeLastIconAction;
        putValue(Action.SHORT_DESCRIPTION, _icon.getDescription(controller.getFrame()));
        this.icon = _icon;
        controller.getActionFactory().registerActor(this, getDoActionClass());
    }

    public void actionPerformed(ActionEvent e) {
       for (ListIterator it = modeController.getSelecteds().listIterator();it.hasNext();) {
          MindMapNodeModel selected = (MindMapNodeModel)it.next();
          addIcon(selected, icon);
        }
    }

    public void addIcon(MindMapNode node, MindIcon icon) {
        modeController.getActionFactory().startTransaction(
                (String) getValue(NAME));
        modeController.getActionFactory().executeAction(
                getActionPair(node, icon));
        modeController.getActionFactory().endTransaction(
                (String) getValue(NAME));
    }

    /**
     * @param node
     * @param icon
     * @return
     */
    private ActionPair getActionPair(MindMapNode node, MindIcon icon)  {
        AddIconAction doAction = createAddIconAction(node, icon);
        XmlAction undoAction = removeLastIconAction.createRemoveLastIconXmlAction(node);
        return new ActionPair(doAction, undoAction);
    }

    public void act(XmlAction action) {
        if (action instanceof AddIconAction) {
            AddIconAction iconAction = (AddIconAction) action;
            MindMapNode node = modeController.getNodeFromID(iconAction.getNode());
            String iconName = iconAction.getIconName();
            MindIcon icon = MindIcon.factory(iconName);
            node.addIcon(icon);
            modeController.nodeChanged(node);
        }
    }

    public Class getDoActionClass() {
        return AddIconAction.class;
    }
    public AddIconAction createAddIconAction(MindMapNode node, MindIcon icon) {
        AddIconAction action = new AddIconAction();
        action.setNode(node.getObjectId(modeController));
        action.setIconName(icon.getName());
        return action;
    }

}