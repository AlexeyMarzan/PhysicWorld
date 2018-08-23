package ru.physicmodeler.world;

import ru.physicmodeler.world.prototype.IWindow;
import ru.physicmodeler.world.prototype.view.IView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ViewMenuActionListener implements ActionListener {
    private IWindow window;
    private IView view;

    ViewMenuActionListener(IWindow window, IView rend) {
        this.window = window;
        this.view = rend;
    }

    public void actionPerformed(ActionEvent e) {
        this.window.setView(this.view);
    }
}
