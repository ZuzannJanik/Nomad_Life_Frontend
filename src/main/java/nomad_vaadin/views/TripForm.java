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
import nomad_vaadin.data.domain.Trip;
import nomad_vaadin.data.domain.enums.TripStatus;
import java.util.List;

public class TripForm extends FormLayout {
    TextField tripId = new TextField("Trip id");
    DatePicker dateStart = new DatePicker("Trip begin");
    DatePicker dateEnd = new DatePicker("Trip end");
    TextField destinationCountry = new TextField("Trip destination");
    ComboBox<TripStatus> tripStatus = new ComboBox<>("Trip status");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Trip> binder = new BeanValidationBinder<>(Trip.class);

    public TripForm(List<Trip> allTrips) {
        addClassName("trip-form");
        tripStatus.setItems(TripStatus.values());
        binder.bindInstanceFields(this);
        tripId.setReadOnly(true);
        add(tripId, dateStart, dateEnd, destinationCountry, tripStatus, createButtonsLayout());
    }

    public void setTrip(Trip trip) {
        binder.setBean(trip);
    }

    public static abstract class TripFormEvent extends ComponentEvent<TripForm> {
        private final Trip trip;
        protected TripFormEvent(TripForm source, Trip trip) {
            super(source, false);
            this.trip = trip;
        }

        public Trip getTrip() {
            return trip;
        }
    }

    public static class SaveEvent extends TripFormEvent {
        SaveEvent(TripForm source, Trip trip) {
            super(source, trip);
        }
    }

    public static class DeleteEvent extends TripFormEvent {
        DeleteEvent(TripForm source, Trip trip) {
            super(source, trip);
        }
    }

    public static class CloseEvent extends TripFormEvent {
        CloseEvent(TripForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(Class<DeleteEvent> deleteEventClass, ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(Class<SaveEvent> saveEventClass, ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(Class<CloseEvent> closeEventClass, ComponentEventListener<CloseEvent> listener) {
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
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }
}
