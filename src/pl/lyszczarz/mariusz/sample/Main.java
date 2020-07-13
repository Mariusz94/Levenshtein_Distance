package pl.lyszczarz.mariusz.sample;

import pl.lyszczarz.mariusz.levenshteinDistance.LevenshteinDistance;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

import static pl.lyszczarz.mariusz.levenshteinDistance.LevenshteinDistance.calculateLevenshteinDistance;

public class Main {
    static List<String> wordList;
    static JFrame frame;
    JPanel panel;
    Container container;
    static JLayeredPane layeredPane;
    static JTextField textField;
    static JButton button;
    static JList list;
    static Map<String, Integer> filteredWord;

    public Main() {
        wordList = new ArrayList<>();
        filteredWord = new TreeMap<>();
        wordList = getWordFromFile("src\\pl\\lyszczarz\\mariusz\\sample\\word.txt");
        //wordList = getWordFromFile("src\\pl\\lyszczarz\\mariusz\\sample\\imiona_polskie.txt");
        frame = new JFrame();
        frame.setTitle(getClass().getName());
        frame.setSize(400, 400);
        frame.setLocation(0, 0);
        initComponent();

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initComponent() {
        container = frame.getContentPane();
        layeredPane = frame.getLayeredPane();
        panel = new JPanel(null);
        container.add(panel);

        textField = new JTextField(10);
        textField.setSize(200, 20);
        textField.setLocation(frame.getSize().width / 2 - textField.getSize().width / 2, 10);
        textField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        textField.addFocusListener(new TextFieldFocusListener());
        panel.add(textField);

        button = new JButton("OK");
        button.setBounds(textField.getLocation().x + textField.getSize().width + 10,
                textField.getLocation().y, 80, 20);
        button.requestFocus();
        panel.add(button);

    }

    private static void fillListFilteredWord() {
        clearList();
        if (!filteredWord.isEmpty()) {
            list = new JList(mapToList(filteredWord).toArray());

            list.setBounds(textField.getLocation().x,
                    textField.getLocation().y + textField.getHeight(),
                    textField.getWidth(), 18 * filteredWord.size());
            list.addListSelectionListener(new SelectionListener());
            layeredPane.add(list, new Integer(1));
        }
    }

    private static void clearList(){
        if (layeredPane.getComponents().length != 1){
            layeredPane.remove(0);
            frame.repaint();
        }
    }

    private static class TextFieldFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            System.out.println("Focus detected in text field ");
            if (!filteredWord.isEmpty())
                list.setVisible(true);
        }

        @Override
        public void focusLost(FocusEvent e) {
            System.out.println("Focus lost in text field ");
            if (!filteredWord.isEmpty())
                list.setVisible(false);

        }
    }

    private static class TextFieldDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            System.out.println("insert action detected in text filed");
            filteredWord = getFilteredList();
            clearList();
            fillListFilteredWord();
            printList(mapToList(filteredWord));
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            System.out.println("remove action detected in text filed");
            filteredWord = getFilteredList();
            clearList();
            fillListFilteredWord();
            printList(mapToList(filteredWord));
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            System.out.println("change action detected in text filed");
            filteredWord = getFilteredList();
            clearList();
            fillListFilteredWord();
            printList(mapToList(filteredWord));
        }
    }

    private static Map<String, Integer> getFilteredList() {
        String text = textField.getText();
        Map<String, Integer> map = new TreeMap<>();
        for (String s : wordList) {
            if (s.length() > 2) {
                int result = LevenshteinDistance.calculateLevenshteinDistance(text.toCharArray(), s.toCharArray());
                if (result < 2) map.put(s, result);
            }
        }
        return map;
    }

    private static List<String> mapToList(Map<String, Integer> map){
        System.out.println("start");
        List<String> stringList = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0) {
                stringList.add(0, entry.getKey());
            }else{
            stringList.add(entry.getKey());
            }
        }
        return stringList;
    }

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();


    }

    private List<String> getWordFromFile(String path) {
        File file = new File(path);

        List<String> wordList = null;
        try {
            wordList = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(wordList.size());
        return wordList;
    }

    private static void printList(List<String> list) {
        for (String s : list) {
            System.out.println(s);
        }
    }

    private static class SelectionListener implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            System.out.println((String)list.getSelectedValue() + " Zostało wybrane");
            textField.setText((String)list.getSelectedValue());
            button.requestFocus();
        }
    }
}
