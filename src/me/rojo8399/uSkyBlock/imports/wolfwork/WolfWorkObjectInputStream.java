package me.rojo8399.uSkyBlock.imports.wolfwork;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Created by RasmusLock on 25/12/2014.
 */
public class WolfWorkObjectInputStream extends ObjectInputStream {
    public WolfWorkObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        if (desc.getName().equals("me.rojo8399.uSkyBlock.PlayerInfo")) {
            return PlayerInfo.class;
        } else if (desc.getName().equals("me.rojo8399.uSkyBlock.SerializableLocation")) {
            return SerializableLocation.class;
        }
        return super.resolveClass(desc);
    }
}
