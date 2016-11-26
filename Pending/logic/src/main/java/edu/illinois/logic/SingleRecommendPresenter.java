package edu.illinois.logic;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class SingleRecommendPresenter<V extends SingleRecommendView, M extends SingleRecommendModel> extends CommonPresenter<V,M> {
	private final static Logger logger = Logger.getLogger(SingleRecommendPresenter.class.getName());
}