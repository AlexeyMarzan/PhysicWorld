package ru.physicmodeler.world.prototype;

import ru.physicmodeler.world.prototype.view.IView;
import ru.physicmodeler.world.util.ProjectProperties;

public interface IWindow {

    void paint();

    void setView(IView view);

    ProjectProperties getProperties();

}
