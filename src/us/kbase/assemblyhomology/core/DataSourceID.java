package us.kbase.assemblyhomology.core;

import us.kbase.assemblyhomology.core.exceptions.IllegalParameterException;
import us.kbase.assemblyhomology.core.exceptions.MissingParameterException;

public class DataSourceID extends Name {

	//TODO JAVADOC
	//TODO TEST
	
	public DataSourceID(final String id)
			throws MissingParameterException, IllegalParameterException {
		super(id, "loadID", 256);
	}
}