package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.MissingFileAssociationException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */

// TODO (BUG): Fix prompt when filename is already defined. 

class DoSaveFile extends Command<WarehouseManager> {

	/** @param receiver */
	DoSaveFile(WarehouseManager receiver) {
		super(Label.SAVE, receiver);
		if (_receiver.getFilename().isEmpty()) {
			addStringField("filename", Prompt.saveAs());
		}
	}

	@Override
	public final void execute() throws CommandException {
		try {
			if (_receiver.getFilename().isEmpty()) {
				String newFilename = stringField("filename");
				_receiver.saveAs(newFilename);
			} else {
				_receiver.save();
			}
		} catch (MissingFileAssociationException | IOException e) {
			e.printStackTrace();
		}
	}

}
