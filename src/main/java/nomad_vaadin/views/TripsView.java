package nomad_vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nomad_vaadin.MainLayout;
import nomad_vaadin.data.domain.Trip;
import nomad_vaadin.service.TripService;
import org.springframework.stereotype.Component;

@Route(value = "trips", layout = MainLayout.class)
@PageTitle("Trips")
@Uses(Icon.class)
@Component
public class TripsView extends VerticalLayout {
    Grid<Trip> grid = new Grid<>();
    TripForm form;
    private final TripService service;

    public TripsView(TripService service) {
        this.service = service;
        addClassName("trips-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void configureForm() {
        form = new TripForm(service.getTrips());
        form.setWidth("40em");
        form.addSaveListener(TripForm.SaveEvent.class, this::saveTrip);
        form.addDeleteListener(TripForm.DeleteEvent.class, this::deleteTrip);
        form.addCloseListener(TripForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("trips-grid");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSizeFull();
        grid.addColumn(Trip::getTripId).setHeader("Trip id").setSortable(true);
        grid.addColumn(Trip::getDateStart).setHeader("Begin").setSortable(true);
        grid.addColumn(Trip::getDateEnd).setHeader("End").setSortable(true);
        grid.addColumn(Trip::getDestinationCountry).setHeader("Designation").setSortable(true);
        grid.addColumn(Trip::getTripStatus).setHeader("Status").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editTrip(event.getValue()));
    }

    private com.vaadin.flow.component.Component getToolbar() {
        Button addButton = new Button("New trip");
        addButton.addClickListener(click -> addTrip());
        var toolbar = new HorizontalLayout(addButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editTrip(Trip trip) {
        if (trip == null) {
            closeEditor();
        } else {
            form.setTrip(trip);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        form.setTrip(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.getTrips());
    }
    private void addTrip() {
        grid.asSingleSelect().clear();
        editTrip(new Trip());
    }
    private void saveTrip(TripForm.SaveEvent event) {
        service.saveTrip(event.getTrip());
        updateList();
        closeEditor();
    }
    private void deleteTrip(TripForm.DeleteEvent event) {
        service.deleteTrip(event.getTrip());
        updateList();
        closeEditor();
    }
}

