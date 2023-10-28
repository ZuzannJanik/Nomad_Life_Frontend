package nomad_vaadin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import nomad_vaadin.data.domain.NomadUser;
import nomad_vaadin.data.domain.enums.UserRole;

import java.util.List;

public class NomadUserForm extends FormLayout {

    TextField userId = new TextField("Id");
    TextField firstName = new TextField("First Name");
    TextField surname = new TextField("Surname");
    TextField homeland = new TextField("Homeland");
    TextField login = new TextField("Login");
    PasswordField password = new PasswordField("Password");
    ComboBox<UserRole> role = new ComboBox<>("Role");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<NomadUser> binder = new BeanValidationBinder<>(NomadUser.class);

    public NomadUserForm(List<NomadUser> users) {
        addClassName("user-form");
        role.setItems(UserRole.values());
        binder.bindInstanceFields(this);
        userId.setReadOnly(true);
        add(userId,firstName,surname,homeland,login,password,role, createButtonsLayout());
    }

    public void setNomadUser(NomadUser nomadUser) {
        binder.setBean(nomadUser);
    }

    public static abstract class NomadUserFormEvent extends ComponentEvent<NomadUserForm> {
        private final NomadUser nomadUser;
        protected NomadUserFormEvent(NomadUserForm source, NomadUser nomadUser) {
            super(source, false);
            this.nomadUser = nomadUser;
        }

        public NomadUser getNomadUser() {
            return nomadUser;
        }
    }

    public static class SaveEvent extends NomadUserFormEvent {
        SaveEvent(NomadUserForm source, NomadUser nomadUser) {
            super(source, nomadUser);
        }
    }

    public static class DeleteEvent extends NomadUserFormEvent {
        DeleteEvent(NomadUserForm source, NomadUser nomadUser) {
            super(source, nomadUser);
        }

    }

    public static class CloseEvent extends NomadUserFormEvent {
        CloseEvent(NomadUserForm source) {
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