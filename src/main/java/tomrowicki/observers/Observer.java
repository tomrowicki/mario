package tomrowicki.observers;

import tomrowicki.engine.GameObject;
import tomrowicki.observers.events.Event;

public interface Observer {

    void onNotify(GameObject object, Event event);
}
