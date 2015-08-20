package fox.spiteful.avaritia;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

public class FieldHelper {
	@SuppressWarnings("unchecked")
	public static <T> T get(Field field, Object instance) {
		try {
			return (T) field.get(instance);
		} catch(Exception e) {
			Lumberjack.log(Level.ERROR, e);
		}
		return null;
	}
	
	public static <T> void set(Field field, Object instance, T value) {
		try {
			field.set(instance, value);
		} catch(Exception e) {
			Lumberjack.log(Level.ERROR, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Method method, Object instance, Object... params) {
		try {
			return (T) method.invoke(instance, params);
		} catch(Exception e) {
			Lumberjack.log(Level.ERROR, e);
		}
		return null;
	}
}
