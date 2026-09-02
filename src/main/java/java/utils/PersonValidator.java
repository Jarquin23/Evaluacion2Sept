package java.utils;

import java.time.LocalDate;

public class PersonValidator {

    public static String validate(String firstName, String lastName, String username,
                                  String password, String jobTitle, String department,
                                  LocalDate hireDate, String contractType, boolean hasBenefits) {
        if (firstName.isBlank() || lastName.isBlank() || username.isBlank() || password.isBlank()
                || jobTitle == null || department == null || hireDate == null || contractType == null) {
            return "Todos los campos obligatorios deben estar llenos.";
        }
        if (username.trim().length() < 5) return "El usuario debe tener al menos 5 caracteres.";
        if (password.length() < 8) return "La contraseña debe tener al menos 8 caracteres.";
        if (hireDate.isAfter(LocalDate.now())) return "La fecha de contratación no puede ser posterior a la actual.";
        if (!hasBenefits) return "Debe seleccionar al menos un beneficio.";

        return null;
    }
}
