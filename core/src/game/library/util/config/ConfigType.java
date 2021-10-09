package game.library.util.config;

/**
 * Represents a util.configuration type that is written or read to or from a
 * file
 * 
 * @author Albert Beaupre
 */
public enum ConfigType {

	/**
	 * The YAML Configuration type which is written in YAML format.
	 */
	YAML,

	/**
	 * The JSON Configuration type which is written in JSON format.
	 */
	JSON,

	/**
	 * The Binary Configuration type which is written in binary format.
	 */
	BINARY,

	/**
	 * The Configuration which is written in plain text format, usually readable to
	 * the eye.
	 */
	TXT

}
