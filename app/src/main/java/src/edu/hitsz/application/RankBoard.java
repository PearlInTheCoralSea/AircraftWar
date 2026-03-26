package src.edu.hitsz.application;

import edu.hitsz.dao.GameRecord;
import edu.hitsz.dao.GameRecordDaoImpl;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RankBoard {
    private final AbstractGame game;
    private final GameRecordDaoImpl gameRecordDao;
    private JPanel rankPanel;
    private JLabel difficultyLabel;
    private JTable scoreTable;
    private JButton deleteButton;
    private JLabel difficultyLabel2;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JScrollPane tableScrollPane;
    private JLabel rankLabel;
    private String userName = "User";
    private List<GameRecord> gameRecords;
    private DefaultTableModel model;

    public RankBoard(AbstractGame game) throws IOException {
        this.game = game;
        this.gameRecordDao = game.gameRecordDao;

        // 初始化UI组件
        initializeComponents();
        setupUI();
        setupEventListeners();

        // 显示排行榜
        showRank();
    }

    private void initializeComponents() {
        rankPanel = new JPanel(new BorderLayout(10, 10));
        difficultyLabel = new JLabel("难度：");
        difficultyLabel2 = new JLabel();
        scoreTable = new JTable();
        deleteButton = new JButton("删除记录");
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        tableScrollPane = new JScrollPane(scoreTable);
        rankLabel = new JLabel("排行榜", JLabel.CENTER);

        // 设置字体大小
        rankLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        difficultyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        difficultyLabel2.setFont(new Font("微软雅黑", Font.BOLD, 16));
    }

    private void setupUI() {
        // 设置难度标签
        difficultyLabel2.setText(game.difficulty);

        // 顶部面板 - 显示标题和难度信息
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        difficultyPanel.add(difficultyLabel);
        difficultyPanel.add(difficultyLabel2);

        topPanel.add(rankLabel);
        topPanel.add(difficultyPanel);

        // 底部面板 - 删除按钮
        bottomPanel.add(deleteButton);

        // 主面板布局
        rankPanel.add(topPanel, BorderLayout.NORTH);
        rankPanel.add(tableScrollPane, BorderLayout.CENTER);
        rankPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 设置表格属性
        scoreTable.setFillsViewportHeight(true);
        scoreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupEventListeners() {
        // 保存用户记录
        saveUserRecord();

        // 删除按钮事件监听
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRecord();
            }
        });
    }

    private void saveUserRecord() {
        String input = JOptionPane.showInputDialog(
                rankPanel,
                "游戏结束！你的得分为" + game.score + "分，请输入用户名记录得分\n（若输入空则默认为User，若点击取消则不保存游戏记录）",
                "User"
        );

        if (input != null) {
            if (!input.trim().isEmpty()) {
                userName = input.trim();
            }
            try {
                saveRecord();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(rankPanel, "保存记录失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedRecord() {
        int selectedRow = scoreTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(rankPanel, "请先选择要删除的记录！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userOption = JOptionPane.showConfirmDialog(
                rankPanel,
                "是否删除选中的记录？",
                "提示",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (userOption == JOptionPane.YES_OPTION) {
            String timeOfDeletedRecord = (String) scoreTable.getValueAt(selectedRow, 3);
            for (GameRecord record : gameRecords) {
                if (Objects.equals(record.getTime(), timeOfDeletedRecord)) {
                    gameRecordDao.deleteRecord(record);
                    break;
                }
            }
            model.removeRow(selectedRow);

            try {
                gameRecordDao.writeToFile();
                JOptionPane.showMessageDialog(rankPanel, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(rankPanel, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public JPanel getRankPanel() {
        return rankPanel;
    }

    /**
     * 保存游戏记录
     */
    private void saveRecord() throws IOException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = dateFormat.format(date);
        gameRecordDao.addRecord(new GameRecord(this.userName, time, game.score, game.difficulty));
        gameRecordDao.writeToFile();

        // 重新加载记录
        gameRecords = gameRecordDao.getAllRecords();
    }

    /**
     * 显示游戏排名
     */
    public void showRank() {
        // 过滤当前难度的记录
        gameRecords = gameRecordDao.getAllRecords().stream()
                .filter(record -> Objects.equals(record.getDifficulty(), game.difficulty))
                .collect(Collectors.toList());

        // 按分数降序排序
        gameRecords.sort((o1, o2) -> o2.getScore() - o1.getScore());

        String[] columnName = {"名次", "玩家名", "得分", "记录时间"};
        String[][] tableData = new String[gameRecords.size()][4];

        for (int i = 0; i < gameRecords.size(); i++) {
            tableData[i][0] = String.valueOf(i + 1);
            tableData[i][1] = gameRecords.get(i).getUserName();
            tableData[i][2] = String.valueOf(gameRecords.get(i).getScore());
            tableData[i][3] = gameRecords.get(i).getTime();
        }

        // 创建表格模型
        model = new DefaultTableModel(tableData, columnName) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        scoreTable.setModel(model);

        // 设置表格列宽
        scoreTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // 名次
        scoreTable.getColumnModel().getColumn(1).setPreferredWidth(100);  // 玩家名
        scoreTable.getColumnModel().getColumn(2).setPreferredWidth(80);   // 得分
        scoreTable.getColumnModel().getColumn(3).setPreferredWidth(200);  // 记录时间
    }
}