package org.example.L30;

import org.example.TELEGRAM.jokes.apiRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.example.TELEGRAM.translator.Translator.translate;

public class UI extends JFrame implements ActionListener {
    JTextArea joke = new JTextArea("Дісклеймер -" + '\n' +"Деякі жарти мают матеріали 18+, я їх туди не вставляв та видалити не можу");


    public UI() {
        super("Shutochka");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLayout(new GridLayout(2,1));
        JPanel jokes = new JPanel(new GridLayout(2,1));
        jokes.setBackground(Color.black);
        joke.setBackground(Color.black);
        joke.setForeground(Color.cyan);
        joke.setLineWrap(true);
        jokes.add(joke);
        add(jokes);
        JPanel buttons = new JPanel();
        buttons.setBackground(Color.white);
        JButton button1 = new JButton("Натисни мене");
        buttons.add(button1);
        add(buttons);
        button1.addActionListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            joke.setText(translate(apiRequest.getOriginakJoke()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
