package se.fikaware.tarta.models;

import se.fikaware.database.*;
import se.fikaware.web.Sendable;
import se.fikaware.web.Server;

import java.io.IOException;

public class School extends PersistentObject implements Sendable {
    public final String slugName;
    public final String schoolName;
    public final DataStorage schoolStorage;
    public final Group group;
    public int freePostID;

    public School(String schoolName) throws IOException {
        super(Server.getInstance().miscStorage); // TODO: Hmmmm.....
        this.slugName = createSlug(schoolName);
        this.schoolName = schoolName;
        this.freePostID = 0;
        this.save();
        schoolStorage = getDataStorage().getRootStorage().getStorage(slugName);
        group = new Group(this, schoolName);
    }

    public School(DataStorage storage, DataReader r) throws IOException {
        super(storage);
        if (storage != Server.getInstance().miscStorage) {
            throw new RuntimeException("Schools should only exist in the _misc storage!");
        }
        slugName = r.readString();
        schoolName = r.readString();
        freePostID = r.readInt();
        schoolStorage = storage.getRootStorage().getStorage(slugName);
        group = schoolStorage.getObject(Group.class, slugName);
    }

    private static String createSlug(String schoolName) {
        return schoolName.replace(' ', '_').toLowerCase();
    }

    @Override
    protected void write(DataWriter writer) throws IOException {
        writer.writeString(slugName);
        writer.writeString(schoolName);
        writer.writeInt(freePostID);
    }

    @Override
    public void delete() throws IOException {
        getDataStorage().getRootStorage().deleteStorage(getDataStorage());
        super.delete();
    }

    @Override
    public void send(ExtendedDataWriter writer) throws IOException {
        writer.writeMapBegin();
        writer.writeMapKey("slugName");
        writer.writeString(slugName);
        writer.writeMapNext();
        writer.writeMapKey("name");
        writer.writeString(schoolName);
        writer.writeMapEnd();
    }
}
