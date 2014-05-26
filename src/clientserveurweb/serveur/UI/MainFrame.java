package clientserveurweb.serveur.UI;

import clientserveurweb.serveur.Core.Connection;
import clientserveurweb.serveur.Core.Observers.ServerObserver;
import clientserveurweb.serveur.Core.Serveur;
import java.io.IOException;

/**
 *
 * @author Adrien
 */
public class MainFrame extends javax.swing.JFrame implements ServerObserver {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    public void lancerTraitementServeur() {
        Serveur srv = new Serveur(80, 6);
        while (true) {
            try {
                Connection connexion = new Connection(srv.getServeur().accept());
                connexion.addServerObserver(this);
                //connexion.fireNewClient();
                Thread processusConnection = new Thread(connexion);
                processusConnection.start();
            } catch (IOException ex) {
                System.out.println("Connexion impossible avec un client");
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelClientsNum = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonShutdown = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Interface Serveur");
        setName(""); // NOI18N

        jTextPane.setEditable(false);
        jScrollPane1.setViewportView(jTextPane);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setText("Gestion du serveur");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel2.setText("Nombre de clients connectés : ");

        jLabelClientsNum.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabelClientsNum.setText("0");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel3.setText("Requêtes en entrée ");

        jButtonShutdown.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jButtonShutdown.setText("Eteindre le serveur");
        jButtonShutdown.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonShutdownMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelClientsNum))
                    .addComponent(jLabel1))
                .addGap(220, 220, 220))
            .addGroup(layout.createSequentialGroup()
                .addGap(265, 265, 265)
                .addComponent(jLabel3)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(jButtonShutdown, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(227, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabelClientsNum))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonShutdown, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonShutdownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonShutdownMouseClicked
        System.exit(0);
    }//GEN-LAST:event_jButtonShutdownMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonShutdown;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelClientsNum;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onRequest(String str) {
        jTextPane.setText(jTextPane.getText().concat(str));
    }

    @Override
    public void onNewClient() {
        jLabelClientsNum.setText(Integer.toString(Integer.parseInt(jLabelClientsNum.getText()) + 1));
    }
}
