package library.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used within an {@code EventListener} class and any method
 * with this annotation is a method used to listen for the given event within
 * the parameter of the method.
 * 
 * @author Albert Beaupre
 * 
 * @see library.event.GameEventListener
 * @see library.event.GameEvent
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameEventMethod {

}
