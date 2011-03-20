package org.freeplane.view.swing.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.freeplane.core.controller.Controller;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.ui.IEditHandler;
import org.freeplane.core.ui.IEditHandler.KeyTypeAction;
import org.freeplane.view.swing.map.MapView;

/**
 * The KeyListener which belongs to the node and cares for Events like C-D
 * (Delete Node). It forwards the requests to NodeController.
 */
public class DefaultNodeKeyListener implements KeyListener {
// // 	final private Controller controller;
	final private IEditHandler editHandler;

	public DefaultNodeKeyListener(final IEditHandler editHandler) {
//		this.controller = controller;
		this.editHandler = editHandler;
	}

	public void keyPressed(final KeyEvent e) {
		if (e.isAltDown() || e.isControlDown() || e.isMetaDown()) {
			return;
		}
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_SHIFT:
			case KeyEvent.VK_DELETE:
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_INSERT:
			case KeyEvent.VK_TAB:
				return;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_PAGE_UP:
			case KeyEvent.VK_PAGE_DOWN:
				((MapView) Controller.getCurrentController().getViewController().getMapView()).move(e);
				return;
			case KeyEvent.VK_HOME:
			case KeyEvent.VK_END:
			case KeyEvent.VK_BACK_SPACE:
				if (editHandler != null) {
					editHandler.edit(e, KeyTypeAction.EDIT_CURRENT, false);
				}
				return;
		}
		final String keyTypeActionString = ResourceController.getResourceController().getProperty("key_type_action", KeyTypeAction.EDIT_CURRENT.toString());
		KeyTypeAction keyTypeAction = KeyTypeAction.valueOf(keyTypeActionString);
		if (! KeyTypeAction.IGNORE.equals(keyTypeAction)) {
			if (!e.isActionKey() && e.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
				if (editHandler != null) {
					editHandler.edit(e, keyTypeAction, false);
				}
				return;
			}
		}
	}

	public void keyReleased(final KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			((MapView) Controller.getCurrentController().getViewController().getMapView()).resetShiftSelectionOrigin();
		}
	}

	public void keyTyped(final KeyEvent e) {
	}
}
