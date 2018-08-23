package ru.physicmodeler.world;

import ru.physicmodeler.world.prototype.IWindow;
import ru.physicmodeler.world.prototype.IWorld;
import ru.physicmodeler.world.util.ProjectProperties;

public class World implements IWorld {
    public static final int BOTW = 4;
    public static final int BOTH = 4;
    private IWindow window;
    public int width;
    public int height;
    public Elemental[][] matrix; // Матрица мира
    int time;
    public int weight;
    public int organic;
    private boolean started;
    private Worker thread;

    private World(IWindow win) {
        window = win;
        weight = 0;
        time = 0;
        organic = 0;
    }

    World(IWindow win, int width, int height) {
        this(win);
        this.setSize(width, height);
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.matrix = new Elemental[width][height];
    }

    @Override
    public void setBot(Elemental elemental) {
        this.matrix[elemental.x][elemental.y] = elemental;
    }

    public void paint() {
        window.paint();
    }

    @Override
    public ProjectProperties getProperties() {
        return window.getProperties();
    }

    void generateAdam() {
        Elemental elemental = new Elemental(this);

        elemental.x = width / 2;
        elemental.y = height / 2;
        elemental.weight = 1;
        elemental.temperature = 24;
        elemental.vx = 0;
        elemental.vy = 0;
        elemental.ax = 0;
        elemental.ay = -10; // земное притяжение

        elemental.c_red = 170; // задаем цвет элементаля
        elemental.c_blue = 170;
        elemental.c_green = 170;

        matrix[elemental.x][elemental.y] = elemental; // даём ссылку на элементаля в массиве world[]

    }

    boolean started() {
        return this.thread != null;
    }

    void start() {
        if (!this.started()) {
            this.thread = new Worker();
            this.thread.start();
        }
    }

    void stop() {
        started = false;
        this.thread = null;
    }

    Elemental getBot(int botX, int botY) {
        return this.matrix[botX][botY];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Elemental[][] getWorldArray() {
        return this.matrix;
    }

    class Worker extends Thread {
        @Override
        public void run() {
            started = true;// Флаг работы потока, если установить в false поток
            // заканчивает работу
            while (started) {

                // обновляем матрицу
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        if (matrix[x][y] != null) {
                            {
                                matrix[x][y].step(); // выполняем шаг элементаля
                            }
                        }
                    }
                }
                time = time + 1;
                if (time % 10 == 0) { // отрисовка на экран через каждые ... шагов
                    paint(); // отображаем текущее состояние симуляции на экран
                }
            }
            paint();// если запаузили рисуем актуальную картинку
            started = false;// Закончили работу
        }
    }
}
