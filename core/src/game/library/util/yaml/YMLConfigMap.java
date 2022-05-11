package game.library.util.yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.badlogic.gdx.files.FileHandle;

/**
 * Represents a util.configuration section in YML. Internally, this class is
 * simply a map of <String, Object> where the objects are values or possibly,
 * more ConfigSections.
 * 
 * This is an easy way of storing and loading data from util.configuration
 * files. It uses the SnakeYAML library, coupled with this project.
 * 
 * @author Dirk Jamieson
 */
public class YMLConfigMap implements Map<Object, Object> {

	/**
	 * The map of <String, Object> contained in this section. This is the key-values
	 * of the map. Note that the key cannot contain full stops ('.').
	 */
	protected Map<Object, Object> map;

	/**
	 * Constructs a new {@code ConfigSection} with no values within the {@code Map}.
	 */
	public YMLConfigMap() {
		this(new HashMap<Object, Object>());
	}

	/**
	 * Constructs a new {@code ConfigSection} with values based on the YAML parsed
	 * from the specified {@code string}.
	 * 
	 * @param string the YAML string to be decoded into a map
	 */
	@SuppressWarnings("unchecked")
	public YMLConfigMap(String string) {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml parser = new Yaml(options);
		map = (Map<Object, Object>) parser.load(string);
		if (map == null) map = new HashMap<Object, Object>();
	}

	/**
	 * Constructs a new {@code ConfigSection} with values based on the YAML parsed
	 * from the specified {@code InputStream}.
	 * 
	 * @param in the {@code InputStream} to parse the YAML from
	 */
	@SuppressWarnings("unchecked")
	public YMLConfigMap(InputStream in) {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml parser = new Yaml(options);
		map = (Map<Object, Object>) parser.load(in);
		if (map == null) map = new HashMap<Object, Object>();
	}

	/**
	 * Constructs a new {@code ConfigSection} based on the {@code values} map.
	 * 
	 * @param values the map of values to put inside of this {@code ConfigSection}
	 * 
	 * @throws NullPointerException if the {@code values} are null
	 */
	public YMLConfigMap(Map<Object, Object> values) {
		if (values == null) throw new NullPointerException("Configurable section may not have a null map.");
		this.map = values;
	}

	@SuppressWarnings("unchecked")
	public static YMLConfigMap load(FileHandle handle) {
		YMLConfigMap section = new YMLConfigMap();
		try {
			DumperOptions options = new DumperOptions();
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			Yaml parser = new Yaml(options);
			section.putAll((Map<? extends Object, ? extends Object>) parser.load(handle.read()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return section;
	}

	/**
	 * Returns true if this {@code ConfigSection} is empty of any values.
	 * 
	 * @return true if this {@code ConfigSection} is empty of any values; return
	 *         false otherwise
	 */
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	/**
	 * Sets the object at the given location to the given object. If you attempt to
	 * do this with an object which is not serializable, then chances are you'll end
	 * up with garbage data. Consider writing a method to serialize the required
	 * object into a ConfigSection if this is the case. Numbers, Strings and
	 * ConfigSections are stored properly and may be fetched later.
	 * 
	 * @param s   the key for the object (May include '.')
	 * @param obj the object to set.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public YMLConfigMap set(Object s, Object obj) {
		if (s == null) throw new NullPointerException("Key may not be null.");
		String[] parts = new String("" + s).split("\\.");
		Map<Object, Object> node = map;
		for (int i = 0; i < parts.length - 1; i++) {
			Object q = node.get(parts[i]);
			if (q == null) {
				q = new HashMap<Object, Object>();
				node.put(parts[i], q);
				node = (HashMap<Object, Object>) q;
			} else if (q instanceof Map) {
				node = (HashMap<Object, Object>) q;
			} else {
				q = new HashMap<Object, Object>();
			}
		}
		if (obj instanceof YMLConfigMap) obj = ((YMLConfigMap) obj).map;
		if (obj instanceof YMLSerializable) obj = ((YMLSerializable) obj).serialize();
		if (obj == null) {
			node.remove(parts[parts.length - 1]);
			if (node.isEmpty() && parts.length > 1) {
				StringBuilder sb = new StringBuilder(parts[0]);
				for (int i = 1; i < parts.length - 1; i++)
					sb.append("." + parts[i]);
				set(sb.toString(), null);
			}
		} else {
			node.put(parts[parts.length - 1], obj);
		}
		return this;
	}

	/**
	 * Fetches the map at the specified key, or if it is null or not a map, returns
	 * the given fallback.
	 * 
	 * @param key      the key to search for
	 * @param k        the maps key class, eg. String.class (For a Map<String, ?>)
	 * @param v        the maps value class, eg. Integer.class (For a Map<?,
	 *                 Integer>)
	 * @param fallback the return value if this method fails
	 * @return the map, or the fallback on failure
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap(String key, Class<K> k, Class<V> v, Map<K, V> fallback) {
		Object o = getObject(key);
		if (o == null) return fallback;
		if (o instanceof Map == false) return fallback;
		Map<?, ?> map = (Map<?, ?>) o;
		Map<K, V> results = new HashMap<K, V>();
		for (Entry<?, ?> e : map.entrySet())
			if (k.isInstance(e.getKey()) && v.isInstance(e.getValue())) results.put((K) e.getKey(), (V) e.getValue());
		return results;
	}

	/**
	 * Fetches the map at the specified key, or if it is null or not a map, returns
	 * null
	 * 
	 * @param key the key to search for
	 * @param k   the maps key class, eg. String.class (For a Map<String, ?>)
	 * @param v   the maps value class, eg. Integer.class (For a Map<?, Integer>)
	 * @return the map, or the null on failure.
	 */
	public <K, V> Map<K, V> getMap(String key, Class<K> k, Class<V> v) {
		return getMap(key, k, v, null);
	}

	/**
	 * Fetches the byte array at the specified key, or returns the given fallback if
	 * it was not found or invalid.
	 * 
	 * @param key      The key to retrieve
	 * @param fallback the return value if the value was not found or invalid
	 * @return the byte array, or the fallback if unsuccessful.
	 */
	public byte[] getByteArray(String key, byte[] fallback) {
		Object o = getObject(key);
		if (o == null) return fallback;
		try {
			return (byte[]) o;
		} catch (ClassCastException e) {
			return fallback;
		}
	}

	/**
	 * Fetches the byte array at the specified key, or returns null if it was not
	 * found or invalid.
	 * 
	 * @param key The key to retrieve
	 * @return the byte array, or the null if unsuccessful.
	 */
	public byte[] getByteArray(String key) {
		return getByteArray(key, null);
	}

	/**
	 * Fetches the list at the requested position in this section. This works by
	 * calling getObject(key). If the object is a collection, its elements are added
	 * to a new list. If the object is a map, its keys are added to a new list. This
	 * new list is returned. If it's neither a set nor map, then the fallback list
	 * is returned instead.
	 * 
	 * @param key      the location of the list to fetch
	 * @param clazz    the type of list you want. Eg, String.class or Integer.class
	 * @param fallback the list to fall back on if key wasn't found or wasn't a
	 *                 list.
	 * @return the list if successful, or the fallback if unsuccessful.
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String key, Class<T> clazz, List<T> fallback) {
		Object o = getObject(key);
		if (o == null) return fallback;
		if (o instanceof Collection) {
			Collection<?> set = (Collection<?>) o;
			List<T> results = new ArrayList<T>(set.size());
			for (Object p : set)
				if (clazz.isInstance(p)) results.add((T) p);
			return results;
		}
		if (o instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) o;
			List<T> results = new ArrayList<T>(map.size());
			for (Object p : map.keySet())
				if (clazz.isInstance(p)) results.add((T) p);
			return results;
		}
		return fallback;
	}

	/**
	 * Fetches the list at the requested position in this section. This works by
	 * calling getObject(key). If the object is a collection, its elements are added
	 * to a new list. If the object is a map, its keys are added to a new list. This
	 * new list is returned. If it's neither a set nor map, then null is returned
	 * instead. This method calls getList(key, clazz, null)
	 * 
	 * @param key   the location of the list to fetch
	 * @param clazz the type of list you want. Eg, String.class or Integer.class
	 * @return the list if successful, or the null if unsuccessful.
	 */
	public <T> List<T> getList(String key, Class<T> clazz) {
		return getList(key, clazz, null);
	}

	/**
	 * Fetches a set of strings which represent the keys for this config section.
	 * This set is immutable.
	 * 
	 * @return the set of strings
	 */
	public Set<Object> getKeys() {
		return Collections.unmodifiableSet(map.keySet());
	}

	/**
	 * Fetches the section under the given key, or returns fallback if there was an
	 * issue. If the section contains the key, but it is not a Configurable Section,
	 * then the fallback is returned.
	 * 
	 * @param key      The key
	 * @param fallback return value if the value was not specified or is not a
	 *                 ConfigSection
	 * @return the section
	 */
	public YMLConfigMap getUnderlyingSection(String key, YMLConfigMap fallback) {
		Object o = getObject(key);
		try {
			if (o != null && o instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<Object, Object> map = (Map<Object, Object>) o;
				return new YMLConfigMap(map);
			}
		} catch (Exception e) {}
		return fallback;
	}

	/**
	 * Fetches the section under the given key, or returns a blank config section if
	 * there was an issue. Note that this differs from other methods which return a
	 * null fallback object. If you desire null on error, use getSection(key, null).
	 * 
	 * @param key the key
	 * @return the config section, never null.
	 */
	public YMLConfigMap getUnderlyingSection(String key) {
		YMLConfigMap c = getUnderlyingSection(key, null);
		if (c == null) c = new YMLConfigMap();
		return c;
	}

	/**
	 * Fetches the raw object at the given location.
	 * 
	 * @param s the key to search for, may contain '.' for subsections.
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public Object getObject(Object s) {
		if (s == null) throw new NullPointerException("Key may not be null.");
		String[] parts = new String("" + s).split("\\.");
		Map<Object, Object> last = map;
		for (int i = 0; i < parts.length - 1; i++) {
			Object q = last.get(parts[i]);
			if (q == null || q instanceof Map == false) return null;
			last = (Map<Object, Object>) q;
		}
		if (last == null) return null;
		Object o = last.get(parts[parts.length - 1]);
		if (o instanceof Map) {
			HashMap<Object, Object> result = new HashMap<Object, Object>();
			for (Entry<?, ?> e : ((Map<?, ?>) o).entrySet()) {
				if (e.getKey() instanceof String == false) return o;
				result.put((String) e.getKey(), e.getValue());
			}
			return new YMLConfigMap(result);
		}
		return o;
	}

	/**
	 * Fetches the integer at the given key.
	 * 
	 * @param k the key
	 * @return the value or 0 if not found.
	 */
	public int getInt(String k) {
		return getInt(k, 0);
	}

	/**
	 * Fetches the integer at the given key, allowing a fallback value to be
	 * specified.
	 * 
	 * @param k        the key to search for
	 * @param fallback the value to use if the key is not found.
	 * @return the value, or fallback if not found.
	 */
	public int getInt(String k, int fallback) {
		try {
			return ((Number) getObject(k)).intValue();
		} catch (Exception e) {
			return fallback;
		}
	}

	/**
	 * Fetches the long at the given key.
	 * 
	 * @param k the key
	 * @return the value or 0 if not found.
	 */
	public long getLong(String k) {
		return getLong(k, 0);
	}

	/**
	 * Fetches the long at the given key, allowing a fallback value to be specified.
	 * 
	 * @param k        the key to search for
	 * @param fallback the value to use if the key is not found.
	 * @return the value, or fallback if not found.
	 */
	public long getLong(String k, long fallback) {
		try {
			return ((Number) getObject(k)).longValue();
		} catch (Exception e) {
			return fallback;
		}
	}

	/**
	 * Fetches the double at the given key.
	 * 
	 * @param k the key
	 * @return the value or 0 if not found.
	 */
	public double getDouble(String k) {
		return getDouble(k, 0);
	}

	/**
	 * Fetches the double at the given key, allowing a fallback value to be
	 * specified.
	 * 
	 * @param k        the key to search for
	 * @param fallback the value to use if the key is not found.
	 * @return the value, or fallback if not found.
	 */
	public double getDouble(String k, double fallback) {
		try {
			return ((Number) getObject(k)).doubleValue();
		} catch (Exception e) {
			return fallback;
		}
	}

	/**
	 * Fetches the String at the given key.
	 * 
	 * @param k the key
	 * @return the value or null if not found.
	 */
	public String getString(String k) {
		return getString(k, null);
	}

	/**
	 * Fetches the String at the given key, allowing a fallback value to be
	 * specified.
	 * 
	 * @param k        the key to search for
	 * @param fallback the value to use if the key is not found.
	 * @return the value, or fallback if not found.
	 */
	public String getString(String k, String fallback) {
		try {
			return getObject(k).toString();
		} catch (Exception e) {
			return fallback;
		}
	}

	/**
	 * Fetches the boolean at the given key.
	 * 
	 * @param k the key
	 * @return the value or false if not found.
	 */
	public boolean getBoolean(String k) {
		return getBoolean(k, false);
	}

	/**
	 * Fetches the boolean at the given key, allowing a fallback value to be
	 * specified.
	 * 
	 * @param k        the key to search for
	 * @param fallback the value to use if the key is not found.
	 * @return the value, or fallback if not found.
	 */
	public boolean getBoolean(String k, boolean fallback) {
		try {
			Object o = getObject(k);
			if (o instanceof Boolean) {
				return (Boolean) o;
			} else if (o instanceof Number) {
				return ((Number) o).doubleValue() != 0; // Nonzero values are
														// true.
			} else if (o instanceof String) {
				return Boolean.parseBoolean((String) o);
			}
			return (Boolean) o; // This will probably fail.
		} catch (Exception e) {
			return fallback;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml parser = new Yaml(options);
		return parser.dump(map);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	public int size() {
		return map.size();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object key) {
		return map.get(key);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		return map.remove(key);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends Object, ? extends Object> m) {
		map.putAll(m);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		map.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	public Set<Object> keySet() {
		return map.keySet();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	public Collection<Object> values() {
		return map.values();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	public Set<Entry<Object, Object>> entrySet() {
		return map.entrySet();
	}

	public Map<Object, Object> getMap() {
		return map;
	}
}