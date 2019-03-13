import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main {

    static JTextArea textAreaSiebel = new JTextArea();
    static  JTextArea textAreaSiebelResult = new JTextArea();
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setSize(900, 950);
        frame.setTitle("Замена биндов");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());

        JButton myButton = new JButton();
        myButton.setText("Забацать");


        textAreaSiebel.setColumns(30);
        textAreaSiebel.setRows(30);


        textAreaSiebelResult.setColumns(30);
        textAreaSiebelResult.setRows(30);
        textAreaSiebelResult.setEditable(false);
        textAreaSiebelResult.setMaximumSize(new Dimension(10, 10));



        frame.add(textAreaSiebel);
        frame.add(myButton);

        frame.add(textAreaSiebelResult);

        myButton.addActionListener(new MyButtonActionListener());
        textAreaSiebel.setSize(new Dimension(100,100));

        JScrollPane scrollPane  = new JScrollPane(textAreaSiebel);
        scrollPane.setBounds(10,60,780,500);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane);

        JScrollPane scrollPane2  = new JScrollPane(textAreaSiebelResult);
        scrollPane.setBounds(10,60,780,500);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane2);

        frame.setVisible(true);

    }
}
   class MyButtonActionListener implements ActionListener{

       @Override
       public void actionPerformed(ActionEvent e) {
          SQLStatement sqlStatement = new SelectStatement(Main.textAreaSiebel.getText());
          Main.textAreaSiebelResult.setText(replace(sqlStatement));
       }

       private String replace(SQLStatement sqlStatement){
           sqlStatement.setKeyList();
           sqlStatement.setKeyValueTable();
           sqlStatement.removeFirstBind();
           sqlStatement.replaceLog();
           sqlStatement.cutObjBinds();
           return sqlStatement.getSiebelReplaceLog();
       }
   }

