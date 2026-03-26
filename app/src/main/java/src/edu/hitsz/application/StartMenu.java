package src.edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class StartMenu {
    private final String musicOn = "开";
    public String difficulty;
    public boolean isMusic;
    private JButton easyButton;
    private JButton hardButton;
    private JButton mediumButton;
    private JPanel startPanel;
    private JLabel musicLabel;
    private JComboBox<String> musicBox;

    public StartMenu() {
        // 初始化 UI 组件
        startPanel = new JPanel();
        easyButton = new JButton("简单模式");
        mediumButton = new JButton("中等模式");
        hardButton = new JButton("困难模式");
        musicLabel = new JLabel("背景音乐");
        musicBox = new JComboBox<>(new String[]{"开", "关"});

        // 添加组件到面板
        startPanel.add(easyButton);
        startPanel.add(mediumButton);
        startPanel.add(hardButton);
        startPanel.add(musicLabel);
        startPanel.add(musicBox);

        // 添加监听器
        easyButton.addActionListener(e -> {
            difficulty = "EASY";
            isMusic = Objects.equals(musicBox.getSelectedItem().toString(), "开");
            startPanel.setVisible(false);
            synchronized (Main.MAIN_LOCK) {
                Main.MAIN_LOCK.notify();
            }
        });

        mediumButton.addActionListener(e -> {
            difficulty = "MEDIUM";
            isMusic = Objects.equals(musicBox.getSelectedItem().toString(), "开");
            startPanel.setVisible(false);
            synchronized (Main.MAIN_LOCK) {
                Main.MAIN_LOCK.notify();
            }
        });

        hardButton.addActionListener(e -> {
            difficulty = "HARD";
            isMusic = Objects.equals(musicBox.getSelectedItem().toString(), "开");
            startPanel.setVisible(false);
            synchronized (Main.MAIN_LOCK) {
                Main.MAIN_LOCK.notify();
            }
        });
    }

    public JPanel getStartPanel() {
        return startPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Start");
            frame.setContentPane(new StartMenu().getStartPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}