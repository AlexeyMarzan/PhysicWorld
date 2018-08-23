package ru.physicmodeler.world.prototype;

import ru.physicmodeler.world.Elemental;
import ru.physicmodeler.world.util.ProjectProperties;

public interface IWorld {

    int getWidth();

    int getHeight();

    void setSize(int width, int height);

    void setBot(Elemental elemental);

    void paint();

    ProjectProperties getProperties();

    Elemental[][] getWorldArray();

}
