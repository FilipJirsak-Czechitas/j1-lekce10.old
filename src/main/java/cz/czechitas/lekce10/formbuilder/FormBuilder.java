package cz.czechitas.lekce10.formbuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Filip Jirsák
 */
public class FormBuilder<B> implements WithModel<B>, FormBuilderWithContainer<B>, WithLabel<B>, WithInput<B> {
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

  private final PresentationModel<B> model;
  private Container container;

  private JLabel label;
  private JComponent input;

  private FormBuilder(PresentationModel<B> model) {
    this.model = model;
  }

  public static <B> WithModel<B> create(PresentationModel<B> model) {
    return new FormBuilder<>(model);
  }

  @Override
  public FormBuilderWithContainer<B> container(Container container) {
    this.container = container;
    return this;
  }

  @Override
  public WithLabel<B> label(String text) {
    this.label = new JLabel();
    int mnemonicIndex = text.indexOf("&");
    if (mnemonicIndex < 0) {
      this.label.setText(text);
    } else {
      Objects.checkIndex(mnemonicIndex, text.length() - 1);
      this.label.setText(text.substring(0, mnemonicIndex) + text.substring(mnemonicIndex + 1));
      this.label.setDisplayedMnemonicIndex(mnemonicIndex);
    }
    return this;
  }

  @Override
  public WithInput<B> textField(String property) {
    return textField(property, null);
  }

  @Override
  public WithInput<B> textField(String property, Consumer<JTextField> configuration) {
    JTextField textField = new JTextField();
    Bindings.bind(textField, model.getModel(property));

    if (configuration != null) {
      configuration.accept(textField);
    }

    Objects.requireNonNull(label);
    label.setLabelFor(textField);

    this.input = textField;
    return this;
  }

  private WithInput<B> formattedTextField(String property, JFormattedTextField.AbstractFormatter formatter, Consumer<JFormattedTextField> configuration) {
    JFormattedTextField formattedTextField = new JFormattedTextField(formatter);
    Bindings.bind(formattedTextField, model.getModel(property));

    if (configuration != null) {
      configuration.accept(formattedTextField);
    }

    Objects.requireNonNull(label);
    label.setLabelFor(formattedTextField);

    this.input = formattedTextField;
    return this;
  }

  @Override
  public WithInput<B> numberField(String property) {
    return numberField(property, null);
  }

  @Override
  public WithInput<B> numberField(String property, Consumer<JFormattedTextField> configuration) {
    return formattedTextField(property, new NumberFormatter(), configuration);
  }

  @Override
  public WithInput<B> dateField(String property) {
    return dateField(property, null);
  }

  @Override
  public WithInput<B> dateField(String property, Consumer<JFormattedTextField> configuration) {
    return formattedTextField(property, new DateFormatter(dateFormat), configuration);
  }

  @Override
  public <E> WithInput<B> comboBox(String property, List<E> items) {
    return comboBox(property, items, null);
  }

  @Override
  public <E> WithInput<B> comboBox(String property, List<E> items, Consumer<JComboBox<E>> configuration) {
    JComboBox<E> comboBox = new JComboBox<>();
    SelectionInList<E> selectionInList = new SelectionInList<>(items, model.getModel(property));
    Bindings.bind(comboBox, selectionInList);

    if (configuration != null) {
      configuration.accept(comboBox);
    }

    Objects.requireNonNull(label);
    label.setLabelFor(comboBox);

    this.input = comboBox;
    return this;
  }

  @Override
  public WithInput<B> checkbox(String property) {
    return checkbox(property, null);
  }

  @Override
  public WithInput<B> checkbox(String property, Consumer<JCheckBox> configuration) {
    JCheckBox checkBox = new JCheckBox();
    Bindings.bind(checkBox, model.getModel(property));
    checkBox.setText(label.getText());
    checkBox.setMnemonic(label.getDisplayedMnemonic());

    if (configuration != null) {
      configuration.accept(checkBox);
    }

    label = null;

    this.input = checkBox;
    return this;
  }

  @Override
  public WithInput<B> panel(Consumer<JPanel> configuration) {
    JPanel panel = new JPanel();
    configuration.accept(panel);

    this.input = panel;
    return this;
  }

  @Override
  public FormBuilderWithContainer<B> add() {
    Objects.requireNonNull(input);

    if (label != null) {
      container.add(label);
    }
    container.add(input);
    label = null;
    input = null;
    return this;
  }

  @Override
  public FormBuilderWithContainer<B> add(Object constraints) {
    Objects.requireNonNull(input);

    if (label != null) {
      container.add(label);
    }
    container.add(input, constraints);
    label = null;
    input = null;
    return this;
  }
}