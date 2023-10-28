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
        import nomad_vaadin.data.domain.Vaccination;
        import nomad_vaadin.data.domain.enums.VacType;

        import java.util.List;

public class VaccinationForm extends FormLayout {

    TextField vacId = new TextField("Id");
    TextField diseaseName = new TextField("Disease Name");
    DatePicker lastVac = new DatePicker("Last Vaccination");
    ComboBox<VacType> vacType = new ComboBox<>("Type");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Vaccination> binder = new BeanValidationBinder<>(Vaccination.class);

    public VaccinationForm(List<Vaccination> vaccinations) {
        addClassName("vaccination-form");
        binder.bindInstanceFields(this);
        vacId.setReadOnly(true);
        vacType.setItems(VacType.values());
        add(vacId, diseaseName, lastVac, vacType, createButtonsLayout());
    }

    public void setVaccination(Vaccination vaccination) {
        binder.setBean(vaccination);
    }

    public static abstract class VaccinationFormEvent extends ComponentEvent<VaccinationForm> {
        private final Vaccination vaccination;
        protected VaccinationFormEvent(VaccinationForm source, Vaccination vaccination) {
            super(source, false);
            this.vaccination = vaccination;
        }

        public Vaccination getVaccination() {
            return vaccination;
        }
    }

    public static class SaveEvent extends VaccinationFormEvent {
        SaveEvent(VaccinationForm source, Vaccination vaccination) {
            super(source, vaccination);
        }
    }

    public static class DeleteEvent extends VaccinationFormEvent {
        DeleteEvent(VaccinationForm source, Vaccination vaccination) {
            super(source, vaccination);
        }

    }

    public static class CloseEvent extends VaccinationFormEvent {
        CloseEvent(VaccinationForm source) {
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
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }
    private void validateAndSave() {
        if(binder.writeBeanIfValid(binder.getBean())) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }
}