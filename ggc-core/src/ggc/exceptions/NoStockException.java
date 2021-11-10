package ggc.exceptions;

/**
 * 
 */
public class NoStockException extends Exception {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202009192006L;

	int _currentStock;
	int _askedStock;

	/**
	 * @param filename 
	 */
	public NoStockException(int currentStock, int askedStock) {
        _currentStock = currentStock;
        _askedStock = askedStock;
	}

	/**
	 * @return the requested stock
	 */
	public int getCurrentStock() {
		return _currentStock;
	}

    public int getAskedStock() {
        return _askedStock;
    }

}
