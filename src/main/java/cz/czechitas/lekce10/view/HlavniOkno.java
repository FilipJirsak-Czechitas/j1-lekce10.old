package cz.czechitas.lekce10.view;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import cz.czechitas.lekce10.Aplikace;
import cz.czechitas.lekce10.controller.KontaktyController;
import cz.czechitas.lekce10.formbuilder.ActionBuilder;
import cz.czechitas.lekce10.formbuilder.FormBuilder;
import cz.czechitas.lekce10.formbuilder.FormBuilderWithContainer;
import cz.czechitas.lekce10.model.OsobaBean;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

/**
 * Formulář – hlavní okno aplikace.
 */
public class HlavniOkno extends JFrame {
    private final KontaktyController controller;

    public HlavniOkno(KontaktyController controller) throws HeadlessException {
        super("Kontakty 1.0");
        this.controller = controller;
        this.init();
    }

    public void start() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(Aplikace.class.getResource("czechitas-icon.png")).getImage());
        setLayout(new MigLayout("wrap 4", "[right]rel[50:75:250,grow,fill]unrel[right]rel[50:75:250,grow,fill]"));
        setMinimumSize(new Dimension(400, 200));

        createMenu();

        FormBuilderWithContainer<OsobaBean> formBuilder = FormBuilder.create(controller.getModel())
                .container(this);

        formBuilder
                .label("&Jméno")
                .textField("jmeno")
                .add();
        formBuilder
                .label("&Příjmení")
                .textField("prijmeni")
                .add();

        formBuilder
                .label("Titul pře&d")
                .textField("titulPred")
                .add();
        formBuilder
                .label("Titul &za")
                .textField("titulZa")
                .add();

        formBuilder
                .label("Celé jméno")
                .textField("celeJmeno", tf -> tf.setEditable(false))
                .add("span");

        formBuilder
                .label("&Adresa")
                .textField("adresa")
                .add("span");

        formBuilder
                .label("&Kraj")
                .comboBox("kraj", KontaktyController.KRAJE)
                .add("span");

        formBuilder
                .label("&Datum narození")
                .dateField("datumNarozeniDate")
                .add();

        formBuilder
                .label("Věk")
                .numberField("vek", ftf -> ftf.setEditable(false))
                .add();

        formBuilder
                .label("Po&hlaví")
                .comboBox("pohlavi", KontaktyController.POHLAVI)
                .add();

        formBuilder
                .checkbox("Dospělý", "dospely", c -> c.setEnabled(false))
                .add("span 2");

        formBuilder
                .panel(panel -> {
                    JButton novyButton = new JButton(controller.getNovyAction());
                    JButton ulozitButton = new JButton(controller.getUlozitAction());

                    getRootPane().setDefaultButton(ulozitButton);

                    panel.add(novyButton);
                    panel.add(ulozitButton);
                })
                .add("right, span");

        pack();
    }

    private void createMenu() {
        JMenu menuKontakty = new JMenu("Kontakty");
        menuKontakty.add(controller.getNovyAction());
        menuKontakty.add(controller.getUlozitAction());

        JMenu menuNapoveda = new JMenu("Nápověda");
        menuNapoveda.add(
                ActionBuilder.create(
                        "&O aplikaci",
                        () -> JOptionPane.showMessageDialog(this, "Aplikace Kontakty 1.0", "O aplikaci", JOptionPane.INFORMATION_MESSAGE)
                )
        );

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuKontakty);
        menuBar.add(menuNapoveda);

        setJMenuBar(menuBar);
    }

}
