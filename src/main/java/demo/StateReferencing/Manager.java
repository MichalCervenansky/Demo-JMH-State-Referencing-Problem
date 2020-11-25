package demo.StateReferencing;

public class Manager {

    public void disposeEngine(Engine engine) {
        engine.close();
    }

    public void close() {
        // Closes the manager
    }

    public void setUpSomething() {
        // does something in manager
    }
}
