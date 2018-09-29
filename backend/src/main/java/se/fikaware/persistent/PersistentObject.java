package se.fikaware.persistent;

import java.io.IOException;

public abstract class PersistentObject {
    private final DataStorage owner;

    protected PersistentObject(DataStorage owner) {
        this.owner = owner;
    }

    protected abstract void write(DataWriter writer) throws IOException;

    public void delete() throws IOException {
        owner.remove(this.getClass(), this);
        owner.update(this.getClass());
    }

    public final DataStorage getDataStorage() {
        return owner;
    }

    public final void save() throws IOException {
        if (!owner.insertObject(this)) {
            owner.update(this.getClass());
        }
    }
}
