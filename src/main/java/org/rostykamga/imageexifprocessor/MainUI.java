package org.rostykamga.imageexifprocessor;

import org.rostykamga.imageexifprocessor.model.ExifData;
import org.rostykamga.imageexifprocessor.model.ImageProperty;
import org.rostykamga.imageexifprocessor.ui.ImagePropertyTableModel;
import org.rostykamga.imageexifprocessor.ui.PictureTile;
import org.rostykamga.imageexifprocessor.utils.Parser;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The main UI driver class used to test the developed features
 */
public class MainUI extends JFrame {

    private static final int GRID_COLS = 5;

    private final ExifProcessor processor = new ExifProcessor();

    private final ImagePropertyTableModel tableModel = new ImagePropertyTableModel();
    /*
   JTable used to display the selected picture properties.
    */
    private final JTable propertiesTable = new JTable(tableModel);

    private final JPanel gridpaneContainer = new JPanel(new BorderLayout());
    private final  JLabel thLabel = new JLabel();

    public MainUI(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initLayout();
        buildMenu();
    }

    /**
     * Build the Main UI layout
     */
    private void initLayout(){

        //getContentPane().setLayout(new BorderLayout(5,5));
        getContentPane().setLayout(new GridBagLayout());
        setSize(new Dimension(1000, 700));
        gridpaneContainer.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));

        GridBagConstraints constraints = new GridBagConstraints(0,0,1,1,2.5,0.5,
                GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,5,5,0),1,1);

        getContentPane().add(new JScrollPane(gridpaneContainer), constraints);

        constraints = new GridBagConstraints(2,0,1,1,0.5,0.5,
                GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,5,5,0),1,1);

        getContentPane().add(buildPropertiesJPanel(),constraints);

        setLocationRelativeTo(null);
    }

    /**
     * Builds the properties panel
     * @return
     */
    private JPanel buildPropertiesJPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setMinimumSize(new Dimension(300, 500));

        Border border = BorderFactory.createTitledBorder("Extracted Properties");
        panel.setBorder(border);

        JLabel exifLabel = new JLabel("Exif Metadata");
        GridBagConstraints constraints = new GridBagConstraints(0,0,1,1,0.5,0.5,
                GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,0),1,1);
        panel.add(exifLabel, constraints);

        constraints = new GridBagConstraints(0,1,1,1,0.5,1.0,
                GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),1,1);
        propertiesTable.setMinimumSize(new Dimension(300, 500));

        panel.add(propertiesTable, constraints);

        JLabel thDescription = new JLabel("Thumbnails");
        constraints = new GridBagConstraints(0,2,1,1,0.5,0.5,
                GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,0,5,0),1,1);
        panel.add(thDescription, constraints);

        constraints = new GridBagConstraints(0,3,1,1,0.5,0.5,
                GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,0,5,0),1,1);
        panel.add(thLabel,constraints);

        return panel;
    }

    /**
     * Builds the menu bar and its actions.
     */
    private void buildMenu(){

        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        exitMenuItem.addActionListener(l-> System.exit(0));
        openMenuItem.addActionListener(l->{
            JFileChooser  chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Open images folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            //
            if (chooser.showOpenDialog(MainUI.this) == JFileChooser.APPROVE_OPTION) {
                File seelctedDir = chooser.getSelectedFile();
                loadPicturesInBackground(seelctedDir);
            }
        });

        JMenu menu = new JMenu("File");
        menu.add(openMenuItem);
        menu.add(new JSeparator());
        menu.add(exitMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);

        this.setJMenuBar(menuBar);
    }

    /**
     * Loads all picture (PNG, TIFF, and JPEG) of the folder and display them into the grid.
     * @param folder the root folder to scan.
     */
    private void loadPicturesInBackground(File folder){

        clearMainPanel();
        JProgressBar progressBar = new JProgressBar();
//        Dimension dim = new Dimension(350, 20);
//        progressBar.setPreferredSize(dim);
//        progressBar.setMinimumSize(dim);
//        progressBar.setMaximumSize(dim);

        progressBar.setStringPainted(true);
        progressBar.setString("Loading images...");
        gridpaneContainer.add(progressBar, BorderLayout.CENTER);
        gridpaneContainer.revalidate();
        gridpaneContainer.repaint();

        new ImageLoaderTask(folder, gridpaneContainer, progressBar).execute();
    }

    /**
     * This method is called after the user has selected a picture into the grid and exif and thunbnails has been
     * extracted.
     *
     * @param exifData The exif data of the selected picture, or null if no picture selected or
     *                if exif couldn't be generated
     * @param thunbnail the extracted thunbnail
     */
    private void updatePropertiesPanel(ExifData exifData, BufferedImage thunbnail){

        List<ImageProperty> props = new ArrayList<>();

        if(exifData != null) {
            props.add(new ImageProperty("Image", ""));
            props.add(new ImageProperty("Copyright", exifData.getCopyright() == null? "N/A" : exifData.getCopyright()));

            props.add(new ImageProperty("Date", exifData.getDatetime() == null ? "N/A" : Parser.DATE_FORMAT.format(exifData.getDatetime())));
            props.add(new ImageProperty("Size", exifData.getImageLength()== 0 ? "N/A" : exifData.getImageLength() + " Bytes"));
            props.add(new ImageProperty("Height",  exifData.getImageHeight() ==0? "N/A": exifData.getImageHeight() + " Pixels"));
            props.add(new ImageProperty("Width", exifData.getImageWidth() == 0 ? "N/A" : exifData.getImageWidth() + " Pixels"));
            props.add(new ImageProperty("Resolution Unit", exifData.getResolutionUnit() == null ? "N/A" : exifData.getResolutionUnit()));
            props.add(new ImageProperty("X - Resolution", exifData.getxResolution()==0.0? "N/A" : exifData.getxResolution() + " " + exifData.getResolutionUnit()));
            props.add(new ImageProperty("Y - Resolution", exifData.getyResolution()  == 0.0 ? "N/A": exifData.getyResolution() + " " + exifData.getResolutionUnit()));

            props.add(new ImageProperty("Camera", ""));
            props.add(new ImageProperty("Make", exifData.getMake()== null ? "N/A": exifData.getMake()));
            props.add(new ImageProperty("Model", exifData.getModel() == null ? "N/A": exifData.getModel()));
        }
        tableModel.updateProperties(props);

        //displays the thumbnail
        if(thunbnail != null){
            thLabel.setIcon(new ImageIcon(thunbnail));
        }
        else{
            thLabel.setIcon(null);
        }
        thLabel.revalidate();


    }

    /**
     * Removes all the existing children of the main panel and refresh it
     */
    private void clearMainPanel(){
        try {
            gridpaneContainer.remove(0);
        }
        catch (Exception ex){}
        gridpaneContainer.revalidate();
        gridpaneContainer.repaint();
    }

    class ImageLoaderTask extends SwingWorker<JPanel, Integer>{

        private final File rootFolder;
        private final JProgressBar progressBar;
        private boolean firstChunk = true;
        private final JPanel mainPanel;
        private JPanel grid;

        /**
         * The loader task will loads images in background, notifies the progressbar, and once done, put
         * a new JPanel into the main panel. That JPanel will contains all the images loaded.
         * @param rootFolder the root folder to scan
         * @param mainPanel the main panel into which to display the images
         * @param progressBar the progress observer
         */
        public ImageLoaderTask(File rootFolder, JPanel mainPanel, JProgressBar progressBar) {
            this.rootFolder = rootFolder;
            this.progressBar = progressBar;
            this.mainPanel = mainPanel;
        }

        /**
         * Update the progress bar
         * @param chunks partial progress
         */
        @Override
        protected void process(List<Integer> chunks) {
            super.process(chunks);
            if(firstChunk){
                progressBar.setMaximum(chunks.get(0));
                firstChunk = false;
            }
            else{
                chunks.forEach(progressBar::setValue);
            }
        }

        @Override
        protected void done() {
            super.done();
            clearMainPanel();
            mainPanel.add(grid, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }

        private JPanel emptyPanel(){
            JLabel notFoundLabel = new JLabel("No image found into the selected folder");
            grid = new JPanel();
            grid.setLayout(new BorderLayout());
            grid.add(notFoundLabel, BorderLayout.CENTER);
            return grid;
        }

        @Override
        protected JPanel doInBackground() {

            try {
                List<String> extensions = Arrays.asList(".tiff", ".jpg", ".png", ".jpeg");
                File[] pictures = rootFolder.listFiles(file -> file.isFile()
                        && file.getName().contains(".")
                        && extensions.contains(file.getName().substring(file.getName().lastIndexOf(".")).toLowerCase()));

                if (pictures == null || pictures.length == 0) {
                    return emptyPanel();
                }

                int nbRows = pictures.length % GRID_COLS == 0 ? pictures.length / GRID_COLS : (pictures.length / GRID_COLS) + 1;

                //Publish the amount of work to perform.
                publish(pictures.length);

                grid = new JPanel(new GridLayout(nbRows, GRID_COLS, 10, 10));

                ButtonGroup buttonGroup = new ButtonGroup();
                int count = 0;
                while (count < pictures.length) {

                    PictureTile tile = new PictureTile(pictures[count++], buttonGroup);
                    tile.addActionListener(l -> {
                        ExifData data = processor.extractExif(tile.getPictureFile().getAbsolutePath());
                        BufferedImage thumbnail = processor.extractThumbnail(tile.getPictureFile().getAbsolutePath());
                        updatePropertiesPanel(data, thumbnail);
                    });

                    buttonGroup.add(tile);

                    grid.add(tile);
                    publish(count);
                }
                return grid;
            }
            catch (Exception ex){
                ex.printStackTrace();
                return emptyPanel();
            }
        }
    }
}
