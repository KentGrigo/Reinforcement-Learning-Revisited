package connectfour;

import static connectfour.Settings.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;

class SettingsPanel extends JPanel {

    public SettingsPanel() {
        add(setupLabels());
        add(setupFields());
//        setLayout(new GridLayout(0, 1));

        JButton button = new JButton("Run configuration");
        add(button);
    }

    private JPanel setupLabels() {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(0, 1));

//        addLabel(labelPanel, "NETWORK_FOLDER");
//        addLabel(labelPanel, "LEARNER_TYPE");
//        addLabel(labelPanel, "LEARNER_NETWORK_PATH");
//        addLabel(labelPanel, "LEARNER_MOVE_SELECTOR_TYPE");
//        addLabel(labelPanel, "LEARNER_SEARCH_DEPTH");
//        addLabel(labelPanel, "OPPONENT_TYPE");
//        addLabel(labelPanel, "OPPONENT_NETWORK_PATH");
//        addLabel(labelPanel, "OPPONENT_MOVE_SELECTOR_TYPE");
//        addLabel(labelPanel, "OPPONENT_SEARCH_DEPTH");
//        addLabel(labelPanel, "BENCHMARK_TYPE");
//        addLabel(labelPanel, "BENCHMARK_NETWORK_PATH");
//        addLabel(labelPanel, "BENCHMARK_SEARCH_DEPTH");
//        addLabel(labelPanel, "OVERWRITE");
//        addLabel(labelPanel, "SAVE_NETWORKS");
//        addLabel(labelPanel, "USE_SAME_NETWORK");
//        addLabel(labelPanel, "DEBUG");
//        addLabel(labelPanel, "MOVE_SELECTOR_RATE_DECAY_FACTOR");
//        addLabel(labelPanel, "MIN_MOVE_SELECTOR_RATE");
//        addLabel(labelPanel, "LEARNING_RATE_DECAY_FACTOR");
//        addLabel(labelPanel, "ETA_DECAY_FACTOR");
//        addLabel(labelPanel, "GRID_SEARCH_RERUNS");
        addLabel(labelPanel, "EPOCHS");
        addLabel(labelPanel, "GAMES_PER_EPOCH");
        addLabel(labelPanel, "BENCHMARK_GAMES");
        addLabel(labelPanel, "INITIAL_PIECES");
        addLabel(labelPanel, "BATCH_SIZE");

        return labelPanel;
    }

    private JPanel setupFields() {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(0, 1));

//        addTextField(fieldPanel, "" + NETWORK_FOLDER);
//        addTextField(fieldPanel, "" + LEARNER_TYPE);
//        addTextField(fieldPanel, "" + LEARNER_NETWORK_PATH);
//        addTextField(fieldPanel, "" + LEARNER_MOVE_SELECTOR_TYPE);
//        addTextField(fieldPanel, "" + LEARNER_SEARCH_DEPTH);
//        addTextField(fieldPanel, "" + OPPONENT_TYPE);
//        addTextField(fieldPanel, "" + OPPONENT_NETWORK_PATH);
//        addTextField(fieldPanel, "" + OPPONENT_MOVE_SELECTOR_TYPE);
//        addTextField(fieldPanel, "" + OPPONENT_SEARCH_DEPTH);
//        addTextField(fieldPanel, "" + BENCHMARK_TYPE);
//        addTextField(fieldPanel, "" + BENCHMARK_NETWORK_PATH);
//        addTextField(fieldPanel, "" + BENCHMARK_SEARCH_DEPTH);
//        addTextField(fieldPanel, "" + OVERWRITE);
//        addTextField(fieldPanel, "" + SAVE_NETWORKS);
//        addTextField(fieldPanel, "" + USE_SAME_NETWORK);
//        addTextField(fieldPanel, "" + DEBUG);
//        addTextField(fieldPanel, "" + MOVE_SELECTOR_RATE_DECAY_FACTOR);
//        addTextField(fieldPanel, "" + MIN_MOVE_SELECTOR_RATE);
//        addTextField(fieldPanel, "" + LEARNING_RATE_DECAY_FACTOR);
//        addTextField(fieldPanel, "" + ETA_DECAY_FACTOR);
//        addTextField(fieldPanel, "" + GRID_SEARCH_RERUNS);
        addTextField(fieldPanel, "" + EPOCHS);
        addTextField(fieldPanel, "" + GAMES_PER_EPOCH);
        addTextField(fieldPanel, "" + BENCHMARK_GAMES);
        addTextField(fieldPanel, "" + INITIAL_PIECES);
        addTextField(fieldPanel, "" + BATCH_SIZE);

        return fieldPanel;
    }

    public void addLabel(JPanel panel, String value) {
        JLabel label = new JLabel(value);
        label.setMinimumSize(new Dimension(50, 20));
        label.setPreferredSize(new Dimension(50, 20));
        label.setMaximumSize(new Dimension(50, 20));
        panel.add(label);
    }

    public void addTextField(JPanel panel, String value) {
        int columnSize = 10;
        JTextField field = new JTextField(value);
        field.setMinimumSize(new Dimension(50, 20));
        field.setPreferredSize(new Dimension(50, 20));
        field.setMaximumSize(new Dimension(50, 20));
//        field.setColumns(columnSize);
        panel.add(field);
    }
}
