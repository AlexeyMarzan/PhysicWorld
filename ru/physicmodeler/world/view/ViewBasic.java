package ru.physicmodeler.world.view;

import ru.physicmodeler.world.Elemental;
import ru.physicmodeler.world.World;
import ru.physicmodeler.world.prototype.view.IView;

import javax.swing.*;
import java.awt.*;

public class ViewBasic implements IView {

    public ViewBasic() {
    }

    @Override
    public String getName() {
        // Отображение ...
        return "Базовое";
    }

    public Image paint(World world, JPanel canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        //Создаем временный буфер для рисования
        Image buf = canvas.createImage(w, h);
        //подеменяем графику на временный буфер
        Graphics g = buf.getGraphics();

        g.drawRect(0, 0, world.width * World.BOTW + 1, world.height * World.BOTH + 1);

        world.weight = 0;
        world.organic = 0;
        for (int y = 0; y < world.height; y++) {
            for (int x = 0; x < world.width; x++) {
                Elemental elemental = world.matrix[x][y];
                if (elemental == null) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x * World.BOTW, y * World.BOTH, World.BOTW, World.BOTH);
                } else {
                    g.setColor(Color.BLACK);
                    g.drawRect(x * World.BOTW, y * World.BOTH, World.BOTW, World.BOTH);

                    g.setColor(new Color(elemental.c_red, elemental.c_green, elemental.c_blue));
                    g.fillRect(x * World.BOTW + 1, y * World.BOTH + 1, World.BOTW - 1, World.BOTH - 1);
                    world.weight = world.weight + elemental.weight;
                }
            }
        }
        return buf;
    }
}
