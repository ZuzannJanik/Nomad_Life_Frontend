package nomad_vaadin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import nomad_vaadin.data.domain.Medicine;
import nomad_vaadin.data.domain.enums.MedType;

import java.util.List;


public class MedicinesForm extends FormLayout {
    TextField medicineId = new TextField("Medicine id");
    TextField medicineName = new TextField("Medicine Name");
    TextField designation = new TextField("Designation");
    ComboBox<MedType> medType = new ComboBox<>("Type");
    DatePicker expiryDate = new DatePicker("Expire Date");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Medicine> binder = new BeanValidationBinder<>(Medicine.class);

    public MedicinesForm(List<Medicine> medicines) {
        addClassName("medicine-form");
        medType.setItems(MedType.values());
        binder.bindInstanceFields(this);
        medicineId.setReadOnly(true);
        add(medicineId, medicineName, designation, medType, expiryDate, createButtonsLayout());
    }

    public void setMedicine(Medicine medicine) {
        binder.setBean(medicine);
    }

    public static abstract class MedicinesFormEvent extends ComponentEvent<MedicinesForm> {
        private final Medicine medicine;
        protected MedicinesFormEvent(MedicinesForm source, Medicine medicine) {
            super(source, false);
            this.medicine = medicine;
        }

        public Medicine getMedicine() {
            return medicine;
        }
    }

    public static class SaveEvent extends MedicinesFormEvent {
        SaveEvent(MedicinesForm source, Medicine medicine) {
            super(source, medicine);
        }
    }

    public static class DeleteEvent extends MedicinesFormEvent {
        DeleteEvent(MedicinesForm source, Medicine medicine) {
            super(source, medicine);
        }
    }

    public static class CloseEvent extends MedicinesFormEvent {
        CloseEvent(MedicinesForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    private Component createButtonsLayout() {
        save.setEnabled(true);
        delete.setEnabled(true);
        close.setEnabled(true);
        save.addClickShortcut(Key.ENTER);
        delete.addClickShortcut(Key.DELETE);
        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }
    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }
}
