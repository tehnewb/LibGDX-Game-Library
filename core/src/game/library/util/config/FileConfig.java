package game.library.util.config;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * A ConfigSection which is based on a particular file. This class contains
 * methods for reloading and saving a ConfigSection.
 * 
 * @author Dirk Jamieson
 */
public class FileConfig extends ConfigSection {

	/** The file we write to */
	private final InputStream in;

	/**
	 * Creates a new FileConfig based on the given file. This method does not load
	 * the data from the file. Instead you should call FileConfig.reload() if you
	 * wish to view previously stored values.
	 * 
	 * @param file the file to base this config on.
	 */
	public FileConfig(InputStream in) {
		this.in = in;
	}

	@SuppressWarnings("unchecked")
	public void load(ConfigType type) {
		try {
			Map<String, Object> data = null;

			switch (type) {
			case BINARY:
				break;
			case JSON:
				break;
			case TXT:
				break;
			case YAML:
				DumperOptions options = new DumperOptions();
				options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
				Yaml parser = new Yaml(options);
				data = (Map<String, Object>) parser.load(in);
				this.map.putAll(data);
				break;
			default:
				break;
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			// File does not exist, therefore it has no data to load.
		}
	}
}