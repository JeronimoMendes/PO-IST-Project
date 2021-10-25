package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.MissingFileAssociationException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */


class DoSaveFile extends Command<WarehouseManager> {

	/** Form to ask for the filename only if it is empty */
	Form _fileForm = new Form();

	/** @param receiver */
	DoSaveFile(WarehouseManager receiver) {
		super(Label.SAVE, receiver);
	}

	@Override
	public final void execute() throws CommandException {
		try {
			if (_receiver.getFilename().isEmpty()) {
				String newFilename = _fileForm.requestString(Prompt.newSaveAs());
				_receiver.saveAs(newFilename);
			} else {
				_receiver.save();
			}
		} catch (MissingFileAssociationException | IOException e) {
			e.printStackTrace();
		}
	}

}
