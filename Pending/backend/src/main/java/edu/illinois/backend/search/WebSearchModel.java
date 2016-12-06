package edu.illinois.backend.search;

import com.vaadin.ui.Image;
import edu.illinois.backend.WebCommonModel;
import edu.illinois.backend.services.PosterFetchService;
import edu.illinois.logic.SearchModel;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.DatabaseTable;
import edu.illinois.util.JDBCResult;

import javax.imageio.ImageIO;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class WebSearchModel extends WebCommonModel implements SearchModel {
	private final static Logger logger = Logger.getLogger(WebSearchModel.class.getName());
	private ActionListener actionListener;
	
	public WebSearchModel() {
		
	}
	
	public void runSELECTquery(String query) {
		storageService.runSELECTquery(query, this::formatSearchResult, actionListener::saveRatingFailure);
	}
	
	@Override
	public void saveRating(String query, Object additionalArgs) {
		storageService.runUPDATEquery(query, false, this::saveRatingSuccess, actionListener::saveRatingFailure, additionalArgs);
	}
	
	@Override
	public void formatSearchResult(final JDBCResult result) {
		actionListener.formatMovieContainer(result);
	}
	
	@Override
	public void saveRatingSuccess(JDBCResult result) {
		actionListener.saveRatingSuccess(result);
	}
	
	@Override
	public void getGenreAndPlotForMovie(String genreQuery, String plotQuery, DatabaseEntry selected) {
		storageService.runSELECTquery(genreQuery, this::getPlotForMovie, null, Arrays.asList(plotQuery, selected));
	}
	
	private void getPlotForMovie(JDBCResult genreQuery) {
		genreQuery.getOriginalQuery().getAdditionalArgs().ifPresent(
				plotQuery -> storageService.runSELECTquery(((String) ((List<Object>) plotQuery).get(0)), this::notifyGenreAndPlot,
						null, Arrays.asList(genreQuery, ((List<Object>) plotQuery).get(1))));
	}
	
	private void notifyGenreAndPlot(JDBCResult plotResult) {
		if (plotResult.getOriginalQuery().getAdditionalArgs().isPresent()) {
			List<Object> additionalArgs = (List<Object>) plotResult.getOriginalQuery().getAdditionalArgs().get();
			JDBCResult genreResult = (JDBCResult) additionalArgs.get(0);
			DatabaseEntry selected = (DatabaseEntry) additionalArgs.get(1);
			Collection<String> genre = new ArrayList<>();
			if (genreResult.getResult().isPresent()) {
				DatabaseTable genreTable = genreResult.getResult().get();
				for (DatabaseEntry genres : genreTable.getRows()) {
					genre.add(genres.getAttribute("genre", String.class));
				}
			}
			Collection<String> plots = new ArrayList<>();
			if (plotResult.getResult().isPresent()) {
				DatabaseTable plotTable = plotResult.getResult().get();
				for (DatabaseEntry plot : plotTable.getRows()) {
					plots.add(plot.getAttribute("plot", String.class));
				}
			}
			
			actionListener.notifyGenreAndPlot(genre, plots, selected);
		}
		
	}
	
	@Override
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
	@Override
	public Image getTitleImage(String movieName, Integer year) {
		PosterFetchService posterFetchService = new PosterFetchService();
		String urlString = posterFetchService.getURLString(movieName, year);
		try {
			return posterFetchService.getImage(ImageIO.read(new URL(urlString)));
		} catch (Exception ex) {
			logger.log(Level.INFO, "Couldn't get title image", ex);
		}
		return null;
	}
}
