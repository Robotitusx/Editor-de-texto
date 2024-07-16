
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

public class App {
    public static void main(String[] args) throws Exception {
        
        Marco marco = new Marco();
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setVisible(true);
        System.out.println("Hello, World!");
    }
}

class Marco extends JFrame{

    public Marco(){

        setBounds(300,300,300,300);
        setTitle("Programa editor de texto");
        add(new Panel(this)); //Pasar el propio marco

    }

}

class Panel extends JPanel{
    
    private JTabbedPane tPane;
    private JPanel ventana;
    private ArrayList<JTextPane> listAreaTexto;
    private ArrayList<File> listFile;
    private ArrayList<JScrollPane> listScroll;
    private ArrayList<UndoManager> listManager; 
    private int contadorPanel; //Cuenta la cantidad de paneles
    private boolean existePanel; //Dice si inicialmente existe un panel creado
    private JMenuBar menu;
    private boolean numeracion = false;
    private String tipo = "white"; //Estilo por defecto

    private JMenu archivo, editar, seleccion, ver, apariencia;
    private JMenuItem elementoItem;
    private JToolBar herramientas;
    private URL url;
    private JPanel panelExtra;
    private JLabel labelAlfiler;
    private boolean estadoAlfiler = false;
    private JSlider slider; //Util para el tamaño de la letra
    private JPopupMenu menuEmergente;
    private JMenuItem items[];

   
    
    public Panel(JFrame marco){
        setLayout(new BorderLayout());

        //Menu
        
        JPanel panelMenu = new JPanel();
        items = new JMenuItem[8];
        panelMenu.setLayout(new BorderLayout());
        
        menu = new JMenuBar();
        archivo = new JMenu("Archivo");
        editar = new JMenu("Editar");
        seleccion = new JMenu("Seleccion");
        ver = new JMenu("Ver");
        apariencia = new JMenu("Apariencia");

        menu.add(archivo);
        menu.add(editar);
        menu.add(seleccion);
        menu.add(ver);


        //Elementos del Menu Archivo
        creaItem("Nuevo Archivo", "Archivo", "Nuevo");
        creaItem("Abrir Archivo", "Archivo", "Abrir");
        creaItem("Guardar", "Archivo", "Guardar");
        creaItem("Guardar Como", "Archivo", "GuardarComo");
        
        //Elementos del Menu Editar

        creaItem("Deshacer", "Editar", "Deshacer");
        creaItem("Rehacer", "Editar", "Rehacer");
        editar.addSeparator();
        creaItem("Cortar", "Editar", "Cortar");
        creaItem("Copiar", "Editar", "Copiar");
        creaItem("Pegar", "Editar", "Pegar");
        
        //Elementos del Menu Seleccion

        creaItem("Selecionar Todo", "Seleccion", "Seleccion");

        //Elementos del Menu Ver

        creaItem("Numeracion", "Ver", "Numeracion");
        ver.add(apariencia);


        creaItem("Normal", "Apariencia", "Normal");
        creaItem("Dark", "Apariencia", "Dark");
        
        
      
        
        
        panelMenu.add(menu, BorderLayout.NORTH);

        //Area de Texto
        tPane = new JTabbedPane(); //Colocar las ventanas en forma de pestañas
        listFile = new ArrayList<File>();
        listAreaTexto = new ArrayList<JTextPane>();
        listScroll = new ArrayList<JScrollPane>();
        listManager = new ArrayList<UndoManager>();

        
        Utilidades.desactivaItem(items);
        //Barra de herramientas

        herramientas = new JToolBar(JToolBar.VERTICAL);
        url = App.class.getResource("images/imagenx.png");
        Utilidades.addButton(url, herramientas, "Cerrar Pestaña Actual").addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                int seleccion = tPane.getSelectedIndex();

                if (seleccion != -1) { //Si existen pestañas abiertas eliminamos la seleccionada
                    
                    listScroll.get(tPane.getSelectedIndex()).setRowHeaderView(null);
                    tPane.remove(seleccion);
                    listAreaTexto.remove(seleccion);
                    listScroll.remove(seleccion);                    
                    listManager.remove(seleccion);
                    listFile.remove(seleccion);

                    contadorPanel--;

                    if(contadorPanel == 0){

                        Utilidades.desactivaItem(items);

                    }
                    
                }
                if(seleccion == -1){

                    existePanel = false; //Si tpane retorna -1 no existen paneles creados
                    
                    
                }
            }






        });

        url = App.class.getResource("images/2.png");
        Utilidades.addButton(url, herramientas, "Nueva Pestaña").addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                crearPanel();

                if(existePanel){

                    Utilidades.activaItems(items);
                }
            }


        });

        //----------------------------- Panel Extra -----------------------------
        panelExtra = new JPanel();
        panelExtra.setLayout(new BorderLayout());

        JPanel panelIzquierdo = new JPanel();

        labelAlfiler = new JLabel();
        url = App.class.getResource("images/alfiler.png");
        labelAlfiler.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        panelIzquierdo.add(labelAlfiler);
        labelAlfiler.addMouseListener(new MouseAdapter(){
            
            //Al pasar el boton sobre el alquiler cambia la imagen
           @Override
           public void mouseEntered(java.awt.event.MouseEvent e) {
               
               url = App.class.getResource("images/alfiler2.png");
               labelAlfiler.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
           }

           @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                
                if (estadoAlfiler) {

                    url = App.class.getResource("images/alfiler2.png");
                    labelAlfiler.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                    
                }
                else{

                    url = App.class.getResource("images/alfiler.png");
                    labelAlfiler.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                }
                
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                    
                estadoAlfiler = !estadoAlfiler;
                marco.setAlwaysOnTop(estadoAlfiler);
                
            }


        });
        
        JPanel panelCentral = new JPanel();
        slider = new JSlider(8,38,14);
        slider.setMajorTickSpacing(6); //Separacion entre barritas grandes sera de 12 en 12
        slider.setMinorTickSpacing(2); //Indica que la separacion entre las barras pequeñas es de 2
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(new ChangeListener() { //Escuchador de movimiento

            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                Utilidades.tamañoTexto(slider.getValue(), contadorPanel, listAreaTexto);
            }
           
            

        });
        
        panelCentral.add(slider);

        panelExtra.add(panelIzquierdo,BorderLayout.WEST);
        panelExtra.add(panelCentral,BorderLayout.CENTER);

        //----------------------------------------------------------------------
        //-------------------------- Menu Emergente -----------------------------------

        menuEmergente = new JPopupMenu();


        JMenuItem cortar = new JMenuItem("Cortar");
        JMenuItem copiar = new JMenuItem("Copiar");
        JMenuItem pegar = new JMenuItem("Pegar");

        cortar.addActionListener(new DefaultEditorKit.CutAction());
        copiar.addActionListener(new DefaultEditorKit.CopyAction());
        pegar.addActionListener(new DefaultEditorKit.PasteAction());

        menuEmergente.add(cortar);
        menuEmergente.add(copiar);
        menuEmergente.add(pegar);
        


        //-----------------------------------------------------------------------------
        //crearPanel();
        add(panelMenu,BorderLayout.NORTH);
        add(tPane,BorderLayout.CENTER);
        add(herramientas, BorderLayout.WEST);
        add(panelExtra,BorderLayout.SOUTH); 
    }

    public void crearPanel(){

        ventana = new JPanel();
        ventana.setLayout(new BorderLayout());
        listFile.add(new File(""));
        listAreaTexto.add(new JTextPane());
        listScroll.add(new JScrollPane(listAreaTexto.get(contadorPanel)));
        listManager.add(new UndoManager()); //Rastrear los cambios del area de texto

        listAreaTexto.get(contadorPanel).getDocument().addUndoableEditListener(listManager.get(contadorPanel));
        listAreaTexto.get(contadorPanel).setComponentPopupMenu(menuEmergente); //Añadir el menu Emergente

        ventana.add(listScroll.get(contadorPanel),BorderLayout.CENTER);
        tPane.addTab("Title", ventana);


        Utilidades.viewNumeracionInicio(numeracion, listAreaTexto.get(contadorPanel), listScroll.get(contadorPanel));
        tPane.setSelectedIndex(contadorPanel);
        contadorPanel++;
        Utilidades.aplicarFondo(contadorPanel, tipo,slider.getValue(), listAreaTexto);
        existePanel = true;
    }

    public void creaItem(String rotulo, String menu, String accion){

        elementoItem = new JMenuItem(rotulo);

        if(menu.equals("Archivo")){

            archivo.add(elementoItem);
            if(accion.equals("Nuevo")){

                elementoItem.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO Auto-generated method stub
                        crearPanel();
                        if (existePanel) {
                            Utilidades.activaItems(items);
                        }
                    }


                });
            }
            else if(accion.equals("Abrir")){

                elementoItem.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO Auto-generated method stub
                        crearPanel();

                        JFileChooser selectorArchivos = new JFileChooser();
                        selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        int selector = selectorArchivos.showOpenDialog(listAreaTexto.get(tPane.getSelectedIndex()));
                    
                        if(selector == JFileChooser.APPROVE_OPTION){

                            
                            if(existePanel){
                                Utilidades.activaItems(items);

                            }
                            try {
                                boolean existePath = false;
                                for(int i= 0;i < tPane.getTabCount() ;i++){

                                    File f = selectorArchivos.getSelectedFile();

                                    if(listFile.get(i).getPath().equals(f.getPath())){

                                        existePath = true;
                                    }
                                }
                                    if(!existePath){

                                        File archivo = selectorArchivos.getSelectedFile();
                                        listFile.set(tPane.getSelectedIndex(), archivo);

                                        
                                        FileReader entrada = new FileReader(listFile.get(tPane.getSelectedIndex()).getPath());
                                       
                                        BufferedReader myBuffer = new BufferedReader(entrada);
                                        String line = "";

                                        String titulo = listFile.get(tPane.getSelectedIndex()).getName();
                                        tPane.setTitleAt(tPane.getSelectedIndex(), titulo);
                                        
                                        while(line != null){

                                            line = myBuffer.readLine(); //Lee linea a linea lo del archivo y lo almacena en el string

                                            if(line != null){

                                               Utilidades.append(line + "\n", listAreaTexto.get(tPane.getSelectedIndex()));
                                            }
                                        }
                                        Utilidades.aplicarFondo(contadorPanel, tipo,slider.getValue(), listAreaTexto);
                                    }

                                    else{

                                        
                                        for(int j = 0; j < tPane.getTabCount();j++){

                                            File l = selectorArchivos.getSelectedFile();

                                            //Comprobando directorios igualados
                                            if(listFile.get(j).getPath().equals(l.getPath())){

                                                tPane.setSelectedIndex(j);
                                                tPane.remove(tPane.getTabCount() - 1);
                                                listAreaTexto.remove(tPane.getTabCount() - 1);
                                                listScroll.remove(tPane.getTabCount()  - 1);
                                                listFile.remove(tPane.getTabCount() - 1);
                                                contadorPanel--;
                                                break;
                                            }

                                        }

                                    }

                                
                                
                                

                            } catch (IOException el) {
                                //TODO: handle exception
                            }
                        }
                        else{


                            int seleccion = tPane.getSelectedIndex();

                            if(seleccion != -1){

                                
                                tPane.remove(tPane.getTabCount() - 1);
                                listAreaTexto.remove(tPane.getTabCount() - 1);
                                listScroll.remove(tPane.getTabCount()  - 1);
                                listFile.remove(tPane.getTabCount() - 1);
                                contadorPanel--;
                            }
                        }
                       
                    
                    }






                });

            }
            else if(accion.equals("Guardar")){

                items[0] = elementoItem;

                elementoItem.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Guardar como si el archivo no existe

                        if(listFile.get(tPane.getSelectedIndex()).getPath().equals("")){ //Path obtiene la direccion del archivo

                            JFileChooser guardarArchivos = new JFileChooser();
                            int select = guardarArchivos.showSaveDialog(null);

                            if(select == JFileChooser.APPROVE_OPTION){

                                File f = guardarArchivos.getSelectedFile();
                                listFile.set(tPane.getSelectedIndex(), f);
                                tPane.setTitleAt(tPane.getSelectedIndex(), f.getName());

                                try {
                                    FileWriter fw = new FileWriter(listFile.get(tPane.getSelectedIndex()).getPath());
                                    String texto = listAreaTexto.get(tPane.getSelectedIndex()).getText();

                                    for(int i = 0; i < texto.length();i++){

                                        fw.write(texto.charAt(i));
                                    }
                                    fw.close();

                                } catch (IOException el) {
                                    //TODO: handle exception
                                }
                            }
                           
                        }
                        else{

                            try {
                                FileWriter fw = new FileWriter(listFile.get(tPane.getSelectedIndex()).getPath());
                                String texto = listAreaTexto.get(tPane.getSelectedIndex()).getText();

                                for(int i = 0; i < texto.length();i++){

                                    fw.write(texto.charAt(i));
                                }
                                fw.close();

                            } catch (IOException el) {
                                //TODO: handle exception
                            }
                        }

                        
                    }





                });

            }

            else if(accion.equals("GuardarComo")){

                items[1] = elementoItem;
                elementoItem.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser guardarArchivos = new JFileChooser();
                            int select = guardarArchivos.showSaveDialog(null);

                            if(select == JFileChooser.APPROVE_OPTION){

                                File f = guardarArchivos.getSelectedFile();
                                listFile.set(tPane.getSelectedIndex(), f);
                                tPane.setTitleAt(tPane.getSelectedIndex(), f.getName());

                                try {
                                    FileWriter fw = new FileWriter(listFile.get(tPane.getSelectedIndex()).getPath());
                                    String texto = listAreaTexto.get(tPane.getSelectedIndex()).getText();

                                    for(int i = 0; i < texto.length();i++){

                                        fw.write(texto.charAt(i));
                                    }
                                    fw.close();

                                } catch (IOException el) {
                                    //TODO: handle exception
                                }
                            }
                        
                    }




                });
            }
        }
        else if(menu.equals("Editar")){

            editar.add(elementoItem);

            if (accion.equals("Deshacer")) {

                items[2] = elementoItem;
                elementoItem.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                        if(listManager.get(tPane.getSelectedIndex()).canUndo()){

                            listManager.get(tPane.getSelectedIndex()).undo();
                        }
                        
                    }


                });

            }
            else if (accion.equals("Rehacer")) {

                items[3] = elementoItem;
                elementoItem.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(listManager.get(tPane.getSelectedIndex()).canRedo()){

                            listManager.get(tPane.getSelectedIndex()).redo();
                        }
                        
                    }



                });
            } 
            else if (accion.equals("Cortar")) {
                items[4] = elementoItem;
                elementoItem.addActionListener(new DefaultEditorKit.CutAction());
            } 
            else if (accion.equals("Copiar")) {
                items[5] = elementoItem;
                elementoItem.addActionListener(new DefaultEditorKit.CopyAction());
            } 
            else if (accion.equals("Pegar")) {
                items[6] = elementoItem;
                elementoItem.addActionListener(new DefaultEditorKit.PasteAction());
            } 

        }
        else if(menu.equals("Seleccion")){

            items[7] = elementoItem;
            seleccion.add(elementoItem);
            elementoItem.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {



                    listAreaTexto.get(tPane.getSelectedIndex()).selectAll();

                };

            });

        }
        else if(menu.equals("Ver")){

            ver.add(elementoItem);

            if(accion.equals("Numeracion")){

                elementoItem.addActionListener(new ActionListener(){


                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                        numeracion = !numeracion;
                        Utilidades.viewNumeracion(contadorPanel, numeracion, listAreaTexto, listScroll);


                    }


                });


            }
        }
        else{

            apariencia.add(elementoItem);

            if (accion.equals("Normal")) {
                elementoItem.addActionListener(new ActionListener(){


                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                        tipo = "white";

                        if(tPane.getTabCount() > 0){

                            Utilidades.aplicarFondo(contadorPanel, tipo,slider.getValue(), listAreaTexto);

                        }
                       
                    }






                });
            }
            if(accion.equals("Dark")){

                elementoItem.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO Auto-generated method stub

                        tipo = "dark";
                        if(tPane.getTabCount() > 0) {

                            Utilidades.aplicarFondo(contadorPanel, tipo,slider.getValue(), listAreaTexto);
                            
                           
                        } 
                        
                    }






                });


            }

        }

    }

}

