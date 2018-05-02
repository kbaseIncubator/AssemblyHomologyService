package us.kbase.assemblyhomology.load;

import static us.kbase.assemblyhomology.load.ParseHelpers.getString;
import static us.kbase.assemblyhomology.load.ParseHelpers.fromYAML;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import us.kbase.assemblyhomology.core.DataSourceID;
import us.kbase.assemblyhomology.core.NamespaceID;
import us.kbase.assemblyhomology.core.exceptions.IllegalParameterException;
import us.kbase.assemblyhomology.core.exceptions.MissingParameterException;
import us.kbase.assemblyhomology.load.exceptions.LoadInputParseException;

public class NamespaceLoadInfo {
	
	//TODO TEST
	//TODO JAVADOC
	
	private final NamespaceID id;
	private final DataSourceID dataSourceID;
	private final Optional<String> sourceDatabaseID;
	private final Optional<String> description;

	public NamespaceLoadInfo(final InputStream input, final String sourceInfo)
			throws LoadInputParseException {
		final Map<String, Object> data = fromYAML(input, sourceInfo);
		id = getID(data, "id", sourceInfo);
		dataSourceID = getDataSourceID(data, "datasource", sourceInfo);
		sourceDatabaseID = Optional.fromNullable(
				getString(data, "sourcedatabase", sourceInfo, true));
		description = Optional.fromNullable(getString(data, "description", sourceInfo, true));
	}

	public NamespaceID getId() {
		return id;
	}

	public DataSourceID getDataSourceID() {
		return dataSourceID;
	}

	public Optional<String> getSourceDatabaseID() {
		return sourceDatabaseID;
	}

	public Optional<String> getDescription() {
		return description;
	}

	private NamespaceID getID(
			final Map<String, Object> data,
			final String key,
			final String sourceInfo) throws LoadInputParseException {
		final String nsid = getString(data, key, sourceInfo, false);
		try {
			return new NamespaceID(nsid);
		} catch (IllegalParameterException e) {
			throw new LoadInputParseException("Illegal namespace ID: " + nsid, e);
		} catch (MissingParameterException e) {
			throw new RuntimeException("this should be impossible", e);
		}
	}
	
	private DataSourceID getDataSourceID(
			final Map<String, Object> data,
			final String key,
			final String sourceInfo) throws LoadInputParseException {
		final String nsid = getString(data, key, sourceInfo, false);
		try {
			return new DataSourceID(nsid);
		} catch (IllegalParameterException e) {
			throw new LoadInputParseException("Illegal data source ID: " + nsid, e);
		} catch (MissingParameterException e) {
			throw new RuntimeException("this should be impossible", e);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NamespaceLoadInfo [id=");
		builder.append(id);
		builder.append(", dataSourceID=");
		builder.append(dataSourceID);
		builder.append(", sourceDatabaseID=");
		builder.append(sourceDatabaseID);
		builder.append(", description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}

	public static void main(final String[] args) throws Exception {
		final Map<String, String> data = ImmutableMap.of(
				"id", "foo",
				"datasource", "KBase",
				"sourcedatabase", "Ci Refdata",
				"description", "some ref data");
		
		final DumperOptions dos = new DumperOptions();
		dos.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		
		final String yaml = new Yaml(dos).dump(data);
		System.out.println(yaml);
		
		final NamespaceLoadInfo nsload = new NamespaceLoadInfo(
				new ByteArrayInputStream(yaml.getBytes()), "whoo");
		
		System.out.println(nsload);
	}
	
}