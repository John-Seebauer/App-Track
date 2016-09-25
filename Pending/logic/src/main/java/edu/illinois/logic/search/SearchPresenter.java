package edu.illinois.logic.search;

import edu.illinois.logic.CommonPresenter;

import java.io.Serializable;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class SearchPresenter extends CommonPresenter implements Serializable {
	
	public SearchPresenter(SearchView view, SearchModel model) {
		super(view, model);
	}
}
