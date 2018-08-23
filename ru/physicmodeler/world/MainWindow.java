package ru.physicmodeler.world;


import ru.physicmodeler.world.prototype.IWindow;
import ru.physicmodeler.world.prototype.view.IView;
import ru.physicmodeler.world.util.ProjectProperties;
import ru.physicmodeler.world.view.ViewBasic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Тест
// Основной класс программы.

public class MainWindow extends JFrame implements IWindow {
    private static final int BOTW = 4;
    private static final int BOTH = 4;
    private static World world;
    private static MainWindow window;
    private JMenuItem runItem;
    // JPanel paintPanel = new JPanel(new FlowLayout());
    private JLabel timeLabel = new JLabel(" Generation: 0 ");
    private JLabel weightLabel = new JLabel(" Масса: 0 ");
    private JLabel organicLabel = new JLabel(" Organic: 0 ");

    private JLabel recorderBufferLabel = new JLabel("");
    private JLabel memoryLabel = new JLabel("");

    private JLabel frameSavedCounterLabel = new JLabel("");
    private JLabel frameSkipSizeLabel = new JLabel("");
    /**
     * буфер для отрисовки ботов
     */
    private Image buffer = null;
    /**
     * актуальный отрисовщик
     */
    private IView view;
    private JPanel paintPanel = new JPanel() {
        public void paint(Graphics g) {
            g.drawImage(buffer, 0, 0, null);
        }

    };
    private ProjectProperties properties;

    private MainWindow() {
        window = this;
        properties = new ProjectProperties("properties.xml");


        setTitle("PhysicModeler 1.0.0");
        setSize(new Dimension(1800, 900));
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize(), fSize = getSize();
        if (fSize.height > sSize.height) {
            fSize.height = sSize.height;
        }
        if (fSize.width > sSize.width) {
            fSize.width = sSize.width;
        }
        //setLocation((sSize.width - fSize.width)/2, (sSize.height - fSize.height)/2);
        setSize(new Dimension(sSize.width, sSize.height));


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container container = getContentPane();

        container.setLayout(new BorderLayout());// у этого лейаута приятная особенность - центральная часть растягивается автоматически
        container.add(paintPanel, BorderLayout.CENTER);// добавляем нашу карту в центр
        //container.add(paintPanel);


        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        container.add(statusPanel, BorderLayout.SOUTH);

        timeLabel.setPreferredSize(new Dimension(140, 18));
        timeLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(timeLabel);

        weightLabel.setPreferredSize(new Dimension(140, 18));
        weightLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(weightLabel);

        organicLabel.setPreferredSize(new Dimension(140, 18));
        organicLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(organicLabel);

        memoryLabel.setPreferredSize(new Dimension(140, 18));
        memoryLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(memoryLabel);

        recorderBufferLabel.setPreferredSize(new Dimension(140, 18));
        recorderBufferLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(recorderBufferLabel);

        frameSavedCounterLabel.setPreferredSize(new Dimension(140, 18));
        frameSavedCounterLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(frameSavedCounterLabel);

        frameSkipSizeLabel.setPreferredSize(new Dimension(140, 18));
        frameSkipSizeLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(frameSkipSizeLabel);

        paintPanel.addMouseListener(new CustomListener());

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        runItem = new JMenuItem("Запустить");
        fileMenu.add(runItem);
        runItem.addActionListener(e -> {
            if (world == null) {
                int width = paintPanel.getWidth() / BOTW;// Ширина доступной части экрана для рисования карты
                int height = paintPanel.getHeight() / BOTH;// Боты 4 пикселя?
                world = new World(window, width, height);
                world.generateAdam();
                paint();
            }
            if (!world.started()) {
                world.start();//Запускаем его
                runItem.setText("Пауза");

            } else {
                world.stop();
                runItem.setText("Продолжить");
            }

        });

        JMenuItem exitItem = new JMenuItem("Выйти");
        fileMenu.add(exitItem);

        exitItem.addActionListener(e -> {
            System.exit(0);
        });

        menuBar.add(fileMenu);


        JMenu ViewMenu = new JMenu("Вид");
        menuBar.add(ViewMenu);

        JMenuItem item;
        /* Перечень возможных отрисовщиков*/
        IView[] views = new IView[]
                {
                        new ViewBasic(),
                };
        for (IView view1 : views) {
            item = new JMenuItem(view1.getName());
            ViewMenu.add(item);
            item.addActionListener(new ViewMenuActionListener(this, view1));
        }

        this.setJMenuBar(menuBar);

        view = new ViewBasic();
        this.pack();
        this.setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);

    }

    public static void main(String[] args) {
        MainWindow.window = new MainWindow();
    }

    @Override
    public void setView(IView view) {
        this.view = view;
    }

    public void paint() {
        buffer = this.view.paint(world, this.paintPanel);
        timeLabel.setText(" Время: " + String.valueOf(world.time));
        weightLabel.setText(" Масса: " + String.valueOf(world.weight));
        organicLabel.setText(" Organic: " + String.valueOf(world.organic));

        Runtime runtime = Runtime.getRuntime();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        memoryLabel.setText(" Memory MB: " + String.valueOf(memory / (1024L * 1024L)));

        paintPanel.repaint();
    }

    @Override
    public ProjectProperties getProperties() {
        return this.properties;
    }

    class CustomListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            if (world.started()) return;//Если идет обсчет не суетимся, выводить ничего не надо.

            Point p = e.getPoint();
            int x = (int) p.getX();
            int y = (int) p.getY();
            int botX = (x - 2) / BOTW;
            int botY = (y - 2) / BOTH;
            Elemental elemental = world.getBot(botX, botY);
            if (elemental != null) {
                {
                    Graphics g = buffer.getGraphics();
                    g.setColor(Color.MAGENTA);
                    g.fillRect(botX * BOTW, botY * BOTH, BOTW, BOTH);
                    paintPanel.repaint();
                }
                StringBuilder buf = new StringBuilder();
                buf.append("<html>")
                        .append("<p>c_blue=")
                        .append(elemental.c_blue)
                        .append("<p>c_green=")
                        .append(elemental.c_green)
                        .append("<p>c_red=")
                        .append(elemental.c_red)
                        .append("</html>");
                JComponent component = (JComponent) e.getSource();
                paintPanel.setToolTipText(buf.toString());
                MouseEvent phantom = new MouseEvent(
                        component,
                        MouseEvent.MOUSE_MOVED,
                        System.currentTimeMillis() - 2000,
                        0,
                        x,
                        y,
                        0,
                        false);

                ToolTipManager.sharedInstance().mouseMoved(phantom);
            }

        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
