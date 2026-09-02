package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import models.Person;
import services.PersonService;
import services.PersonServiceImpl;
import java.utils.PersonValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonController {

    @FXML private TextField txtFirstName, txtLastName, txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbJobTitle;
    @FXML private ListView<String> lstDepartment;
    @FXML private DatePicker dpHireDate;
    @FXML private ToggleGroup tgContractType;
    @FXML private CheckBox chkInsurance, chkBonus, chkTraining;

    @FXML private TableView<Person> tblPerson;
    @FXML private TableColumn<Person, String> colFullName, colJobTitle, colDepartment, colContractType, colBenefits;
    @FXML private TableColumn<Person, LocalDate> colHireDate;

    private final PersonService personService = new PersonServiceImpl();
    private Person selectedPerson;

    @FXML
    public void initialize() {
        cmbJobTitle.getItems().addAll("Gerente", "Analista", "Desarrollador", "Soporte");
        lstDepartment.getItems().addAll("Sistemas", "Recursos Humanos", "Ventas", "Finanzas");

        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colJobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colHireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        colContractType.setCellValueFactory(new PropertyValueFactory<>("contractType"));
        colBenefits.setCellValueFactory(new PropertyValueFactory<>("benefits"));

        tblPerson.setItems(personService.getPersons());
        setupContextMenu();
    }

    // --- ACCIONES CRUD ---

    @FXML
    public void handleSave(ActionEvent event) {
        String error = validateForm();
        if (error != null) { showAlert(Alert.AlertType.ERROR, "Error", error); return; }

        personService.save(buildPersonFromInputs());
        clearForm();
        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Colaborador guardado correctamente.");
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        if (selectedPerson == null) {
            showAlert(Alert.AlertType.WARNING, "Atención", "Seleccione un registro de la tabla.");
            return;
        }
        String error = validateForm();
        if (error != null) { showAlert(Alert.AlertType.ERROR, "Error", error); return; }

        personService.update(selectedPerson, buildPersonFromInputs());
        clearForm();
        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Registro actualizado correctamente.");
    }

    @FXML
    public void handleDelete(ActionEvent event) {
        Person target = tblPerson.getSelectionModel().getSelectedItem();
        if (target != null) {
            personService.delete(target);
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Registro eliminado correctamente.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Atención", "Seleccione un registro para eliminar.");
        }
    }

    @FXML public void handleClear(ActionEvent event) { clearForm(); }
    @FXML public void handleExitMenu(ActionEvent event) { System.exit(0); }
    @FXML public void handleAboutMenu(ActionEvent event) { showAlert(Alert.AlertType.INFORMATION, "Acerca de", "Distribuidora El Güegüense v1.0"); }



    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (selectedPerson == null) handleSave(null); else handleUpdate(null);
        } else if (event.getCode() == KeyCode.ESCAPE) {
            clearForm();
        }
    }

    @FXML
    public void handleTableDoubleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            populateFormFields();
        }
    }



    private String validateForm() {
        boolean hasBenefits = chkInsurance.isSelected() || chkBonus.isSelected() || chkTraining.isSelected();
        RadioButton selectedRb = (RadioButton) tgContractType.getSelectedToggle();

        return PersonValidator.validate(
                txtFirstName.getText(), txtLastName.getText(), txtUsername.getText(), txtPassword.getText(),
                cmbJobTitle.getValue(), lstDepartment.getSelectionModel().getSelectedItem(),
                dpHireDate.getValue(), selectedRb != null ? selectedRb.getText() : null, hasBenefits
        );
    }

    private Person buildPersonFromInputs() {
        RadioButton selectedRb = (RadioButton) tgContractType.getSelectedToggle();
        List<String> benefits = new ArrayList<>();
        if (chkInsurance.isSelected()) benefits.add("Seguro");
        if (chkBonus.isSelected()) benefits.add("Bono");
        if (chkTraining.isSelected()) benefits.add("Capacitación");

        return new Person(
                txtFirstName.getText().trim(),
                txtLastName.getText().trim(),
                txtUsername.getText().trim(),
                txtPassword.getText(),
                cmbJobTitle.getValue(),
                lstDepartment.getSelectionModel().getSelectedItem(),
                dpHireDate.getValue(),
                selectedRb.getText(),
                String.join(", ", benefits)
        );
    }

    private void populateFormFields() {
        selectedPerson = tblPerson.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) return;

        txtFirstName.setText(selectedPerson.getFirstName());
        txtLastName.setText(selectedPerson.getLastName());
        txtUsername.setText(selectedPerson.getUsername());
        txtPassword.setText(selectedPerson.getPassword());
        cmbJobTitle.setValue(selectedPerson.getJobTitle());
        lstDepartment.getSelectionModel().select(selectedPerson.getDepartment());
        dpHireDate.setValue(selectedPerson.getHireDate());

        tgContractType.getToggles().forEach(toggle -> {
            RadioButton rb = (RadioButton) toggle;
            if (rb.getText().equalsIgnoreCase(selectedPerson.getContractType())) rb.setSelected(true);
        });

        String b = selectedPerson.getBenefits();
        chkInsurance.setSelected(b.contains("Seguro"));
        chkBonus.setSelected(b.contains("Bono"));
        chkTraining.setSelected(b.contains("Capacitación"));
    }

    private void clearForm() {
        txtFirstName.clear(); txtLastName.clear(); txtUsername.clear(); txtPassword.clear();
        cmbJobTitle.getSelectionModel().clearSelection();
        lstDepartment.getSelectionModel().clearSelection();
        dpHireDate.setValue(null);
        if (tgContractType.getSelectedToggle() != null) tgContractType.getSelectedToggle().setSelected(false);
        chkInsurance.setSelected(false); chkBonus.setSelected(false); chkTraining.setSelected(false);
        selectedPerson = null;
        tblPerson.getSelectionModel().clearSelection();
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Editar");
        MenuItem deleteItem = new MenuItem("Eliminar");

        editItem.setOnAction(e -> populateFormFields());
        deleteItem.setOnAction(e -> handleDelete(e));

        contextMenu.getItems().addAll(editItem, deleteItem);
        tblPerson.setContextMenu(contextMenu);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}