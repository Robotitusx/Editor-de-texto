import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Utilidades {
    

    public static void append(String line, JTextPane areaTexto){

        try {
            Document doc = areaTexto.getDocument();
            doc.insertString(doc.getLength(), line, null);


        } catch (BadLocationException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }


    //Metodo para mostrar numeracion

    public static void viewNumeracionInicio(boolean numeracion,JTextPane textArea, JScrollPane scroll){

        if(numeracion){



            scroll.setRowHeaderView(new TextLineNumber(textArea));
        }
        else{

            scroll.setRowHeaderView(null);

        }

    }

    public static void viewNumeracion(int contador, boolean numeracion ,ArrayList<JTextPane> textArea,ArrayList<JScrollPane> scroll){

        if(numeracion){

            for(int i = 0;i < contador;i++){


                scroll.get(i).setRowHeaderView(new TextLineNumber(textArea.get(i)));
            }

        }
        else{

            for(int i = 0;i < contador;i++){


                scroll.get(i).setRowHeaderView(null);
            }

        }

    }

    //Apariencia

    public static void aplicarFondo(int contador, String tipo,int tam, ArrayList<JTextPane> list){

        if (tipo.equals("white")) {
            

            for (int i = 0; i < contador; i++) {
                
                
                StyleContext sc = StyleContext.getDefaultStyleContext();

                //Para el color del texto
                javax.swing.text.AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
                //Para el tipo de texto
                aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Arial");
            
                list.get(i).setCharacterAttributes(aset, true);
                list.get(i).selectAll();
                list.get(i).setCharacterAttributes(aset, false);
                list.get(i).setBackground(Color.WHITE);

                //Para el tamaño del texto

                aset = sc.addAttribute(aset, StyleConstants.FontSize, tam);
                list.get(i).setCharacterAttributes(aset, false);
              
            }
        }

        if (tipo.equals("dark")) {
            
            System.out.println(tipo);
            for (int i = 0; i < contador; i++) {
                
                
                
                StyleContext sc = StyleContext.getDefaultStyleContext();

                //Para el color del texto
                javax.swing.text.AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.red);
                //Para el tipo de texto
                aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Arial");
            
                list.get(i).setCharacterAttributes(aset, true);
                list.get(i).setBackground(Color.BLACK);
                
                list.get(i).selectAll();
                list.get(i).setCharacterAttributes(aset, false);
                list.get(i).setCaretColor(Color.red);

                //Para el Slider

                aset = sc.addAttribute(aset, StyleConstants.FontSize, tam);
                list.get(i).setCharacterAttributes(aset, false);
            
            }






        } 

    }

    //Button

    public static JButton addButton(URL url,Object objectContenedor, String rotulo){

        System.out.println(url);
        JButton button = new JButton(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        button.setToolTipText(rotulo);

        ((Container) objectContenedor).add(button);
        return button;
    }

    //------------------------------------ Tamaño de texto ---------------------------------------

    public static void tamañoTexto(int tam, int contador, ArrayList<JTextPane> list){

        for (int i = 0; i < contador; i++) {
            
            list.get(i).selectAll();

            StyleContext sc = StyleContext.getDefaultStyleContext();

            //Para cambiar el tamaño del texto
            javax.swing.text.AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.FontSize, tam);

            //Aplica el tamaño del texto en el area de texto
            list.get(i).setCharacterAttributes(aset, false);

        }

    }


    //--------------------------------------------------------------------------------------------

    //--------------------------- Metodo bloqueo y desbloqueo de items -----------------------

    public static void activaItems(JMenuItem items[]){

        for(JMenuItem item : items){

            item.setEnabled(true);

        }

    }

    public static void desactivaItem(JMenuItem items[]){

        for(JMenuItem item : items){

            item.setEnabled(false);

        }


    }



    //-------------------------------------------------------------------------------------------
}
