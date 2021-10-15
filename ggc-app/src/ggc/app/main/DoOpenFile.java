package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.FileOpenFailedException;
import ggc.exceptions.UnavailableFileException;

/**
 * Open existing saved state.
 */
class DoOpenFile extends Command<WarehouseManager> {

	/** File's name to load */
	private String _filename;

	/** @param receiver */
	DoOpenFile(WarehouseManager receiver) {
		super(Label.OPEN, receiver);
		addStringField("filename", Prompt.openFile());
	}

	@Override
	public final void execute() throws CommandException {
		
		try {
			_filename = stringField("filename");
			_receiver.load(_filename);
		} catch (UnavailableFileException ufe) {
			throw new FileOpenFailedException(ufe.getFilename());
		}
		
	}

}
