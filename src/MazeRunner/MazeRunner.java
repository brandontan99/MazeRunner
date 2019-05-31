/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MazeRunner;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class MazeRunner extends JFrame {

    public MazeRunner() {

        initUI();
    }

    private void initUI() {

        add(new Board());

        setTitle("MazeRunner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 1050);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            MazeRunner ex = new MazeRunner();
            ex.setVisible(true);
        });
    }
}
