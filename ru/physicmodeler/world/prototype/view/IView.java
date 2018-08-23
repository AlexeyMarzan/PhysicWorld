package ru.physicmodeler.world.prototype.view;

import ru.physicmodeler.world.World;

import javax.swing.*;
import java.awt.*;

public interface IView {
    Image paint(World world, JPanel canvas);

    String getName();
}
