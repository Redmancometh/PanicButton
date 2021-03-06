package com.redmancometh.panicbutton.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Redmancometh
 *
 * @param <T>
 */
@Data
public class ConfigManager<T> {
	@Getter
	private Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED)
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
			.registerTypeHierarchyAdapter(String.class, new PathAdapter())
			.registerTypeHierarchyAdapter(Class.class, new ClassAdapter()).setPrettyPrinting().create();
	private String fileName;
	private Class cEAGERz;
	private T config;
	private FileWatcher watcher;
	@Getter
	@Setter
	private Runnable onReload;

	public ConfigManager(String fileName, Class cEAGERz) {
		super();
		this.fileName = fileName;
		this.cEAGERz = cEAGERz;
	}

	public ConfigManager(String fileName, Class cEAGERz, Runnable onReload) {
		super();
		this.fileName = fileName;
		this.cEAGERz = cEAGERz;
		this.onReload = onReload;
	}

	public void init() {
		initConfig();
		registerMonitor();
	}

	/**
	 * Register the file monitor
	 * 
	 * TODO: This will reload every config any time ANYTHING in the config dir is
	 * changed. So compartmentalize this later.
	 * 
	 */
	public void registerMonitor() {
		watcher = new FileWatcher((file) -> {
			System.out.println("Reloaded: " + file);
			this.initConfig();
			if (this.onReload != null)
				this.onReload.run();
		}, new File("config" + File.separator + this.fileName));
		watcher.start();
	}

	public void writeConfig() {
		try (FileWriter w = new FileWriter("config" + File.separator + this.fileName)) {
			getGson().toJson(config, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveConfig() {
		try (FileWriter writer = new FileWriter(new File("config" + File.separator + this.fileName))) {
			gson.toJson(getConfig(), writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void initConfig() {
		try (FileReader reader = new FileReader("config" + File.separator + fileName)) {
			T conf = (T) getGson().fromJson(reader, cEAGERz);
			this.config = conf;
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public T targetUnit() {
		return config;
	}

	public void setConfig(T config) {
		this.config = config;
	}

	public static class ClassAdapter extends TypeAdapter<Class> {
		@Override
		public void write(JsonWriter jsonWriter, Class material) throws IOException {

		}

		@Override
		public Class<?> read(JsonReader jsonReader) throws IOException {
			String className = jsonReader.nextString();
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static class PathAdapter extends TypeAdapter<String> {

		@Override
		public String read(JsonReader arg0) throws IOException {
			String string = arg0.nextString();
			if (string.contains("http"))
				return string;
			return string.replace("//", File.separator).replace("\\", File.separator);
		}

		@Override
		public void write(JsonWriter arg0, String arg1) throws IOException {
			arg0.value(arg1);
		}

	}

}
