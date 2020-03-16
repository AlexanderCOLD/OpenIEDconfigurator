package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Контейнер для перемещений
 */
public class DragContainer implements Serializable {
	private static final long serialVersionUID = -1890998765646621338L;
	private final List <Pair<String, Object> > mDataPairs = new ArrayList<>();

	public void addData (String key, Object value) {
		mDataPairs.add(new Pair<String, Object>(key, value));
		}
}
