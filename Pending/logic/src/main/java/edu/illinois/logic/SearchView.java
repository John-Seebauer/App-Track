package edu.illinois.logic;

import edu.illinois.util.DatabaseTable;

/**
 * Created by john on 9/20/16.
 */
public interface SearchView extends CommonView {
	interface ActionListener {
		DatabaseTable getDatabaseTable(String name);
	}
}
