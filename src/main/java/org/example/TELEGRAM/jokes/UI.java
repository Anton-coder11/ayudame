package org.example.TELEGRAM.jokes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UI extends JFrame {
    UI(){
        super("Простий UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new GridLayout(2,1));

        // Поле для тексту
        JTextField textField = new JTextField(15);

        add(textField);

        // Кнопка
        JButton button = new JButton("Натисни мене");
        add(button);

        // Обробник подій для кнопки
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    textField.setText(apiRequest.getJoke());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Відображення вікна
        setVisible(true);
    }
}
