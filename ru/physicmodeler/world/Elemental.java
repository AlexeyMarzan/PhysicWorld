package ru.physicmodeler.world;


import ru.physicmodeler.world.prototype.IWorld;

public class Elemental {

    public int c_red;
    public int c_green;
    public int c_blue;

    private World world;

    int x, y, vx, vy, ax, ay, temperature;
    public int weight;

    Elemental(World world) {
        this.world = world;
    }


    void step() {
        // x1 = x0 + dt*vx + dt*dt*ax/2
        // y1 = y0 + dt*yx + dt*dt*ay/2
        // считаем dt = 1
        double x1 = x;
        double dx = vx + ax / 2.0;
        double y1 = y;
        double dy = vy + ay / 2.0;

        double step = Math.sqrt(dx * dx + dy * dy) / Math.max(Math.abs(dx), Math.abs(dy));
        Elemental[][] elementals = getWorld().getWorldArray();
        // проверяем, ЧТО там на пути следования, двигаясь шагом step
        for (double xx = x1 + dx / step, yy = y1 + dy / step;
             Math.abs(xx - (x1 + dx)) < step && Math.abs(yy - (y1 + dy)) < step;
             xx += dx / step, yy += dy / step) {
            int i = ((int) Math.round(xx) + world.width) % world.width; // с учётом цилиндрической системы мира
            int j = (int) Math.round(yy);
            if (j < 0) {// ниже некуда
                // удар о твердоё дно мира, вся кинетическая энергия переходит в тепло
                temperature += weight * (vx * vx + vy * vy) / 2.0;
                vx = 0;
                vy = 0;
                move(xx, yy);
                return;
            }

            if (j >= world.height) { // улетели в космос
                // данный элемент потерян навсегда
                return;
            }


            if (elementals[i][j] == null) continue;

            // произошло столкновение с соседом
            return;
        }

        // здесь оказались, если элементаль может свободно передвинутся на новое место
        vx += ax;
        vy += ay;
        move(x1 + dx, y1 + dy);
    }

    private IWorld getWorld() {
        return this.world;
    }

    private void move(double xt, double yt) {
        int i = ((int) Math.round(xt) + world.width) % world.width; // с учётом цилиндрической системы мира
        int j = (int) Math.round(yt);
        world.matrix[i][j] = this;
        world.matrix[x][y] = null;
        x = i;
        y = j;
    }

    //жжжжжжжжжжжжжжжжжжжхжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжж
    //=== делаем бота более зеленым на экране         ======
    //=== in - номер бота, на сколько озеленить       ======

    /**
     * делаем бота более зеленым на экране
     *
     * @param elemental
     * @param num       номер бота, на сколько озеленить
     */
    private void goGreen(Elemental elemental, int num) {  // добавляем зелени
        elemental.c_green = elemental.c_green + num;
        if (elemental.c_green + num > 255) {
            elemental.c_green = 255;
        }
        int nm = num / 2;
        // убавляем красноту
        elemental.c_red = elemental.c_red - nm;
        if (elemental.c_red < 0) {
            elemental.c_blue = elemental.c_blue + elemental.c_red;
        }
        // убавляем синеву
        elemental.c_blue = elemental.c_blue - nm;
        if (elemental.c_blue < 0) {
            elemental.c_red = elemental.c_red + elemental.c_blue;
        }
        if (elemental.c_red < 0) {
            elemental.c_red = 0;
        }
        if (elemental.c_blue < 0) {
            elemental.c_blue = 0;
        }
    }

    //жжжжжжжжжжжжжжжжжжжхжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжжж
    //=== делаем бота более синим на экране         ======
    //=== in - номер бота, на сколько осинить       ======

    /**
     * делаем бота более синим на экране
     *
     * @param elemental
     * @param num       номер бота, на сколько осинить
     */
    private void goBlue(Elemental elemental, int num) {  // добавляем синевы
        elemental.c_blue = elemental.c_blue + num;
        if (elemental.c_blue > 255) {
            elemental.c_blue = 255;
        }
        int nm = num / 2;
        // убавляем зелень
        elemental.c_green = elemental.c_green - nm;
        if (elemental.c_green < 0) {
            elemental.c_red = elemental.c_red + elemental.c_green;
        }
        // убавляем красноту
        elemental.c_red = elemental.c_red - nm;
        if (elemental.c_red < 0) {
            elemental.c_green = elemental.c_green + elemental.c_red;
        }
        if (elemental.c_red < 0) {
            elemental.c_red = 0;
        }
        if (elemental.c_green < 0) {
            elemental.c_green = 0;
        }
    }

    /**
     * делаем бота более красным на экране
     *
     * @param elemental
     * @param num       номер бота, на сколько окраснить
     */
    private void goRed(Elemental elemental, int num) {  // добавляем красноты
        elemental.c_red = elemental.c_red + num;
        if (elemental.c_red > 255) {
            elemental.c_red = 255;
        }
        int nm = num / 2;
        // убавляем зелень
        elemental.c_green = elemental.c_green - nm;
        if (elemental.c_green < 0) {
            elemental.c_blue = elemental.c_blue + elemental.c_green;
        }
        // убавляем синеву
        elemental.c_blue = elemental.c_blue - nm;
        if (elemental.c_blue < 0) {
            elemental.c_green = elemental.c_green + elemental.c_blue;
        }
        if (elemental.c_blue < 0) {
            elemental.c_blue = 0;
        }
        if (elemental.c_green < 0) {
            elemental.c_green = 0;
        }
    }
}