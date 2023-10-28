package nomad_vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nomad_vaadin.MainLayout;
import nomad_vaadin.data.domain.Vaccination;
import nomad_vaadin.service.VaccinationService;
import org.springframework.stereotype.Component;

@PageTitle("Vaccinations")
@Route(value = "vaccinations", layout = MainLayout.class)
@Uses(Icon.class)
@Component
public class VaccinationsView extends VerticalLayout {
    Grid<Vaccination> grid = new Grid<>();
    H6 multiple = new H6("*If the vaccination was 5 in 1 or 6 in 1, please add each vaccination separately");
    Anchor linkTravelVac = new Anchor("https://szczepieniadlapodrozujacych.pl/szczepienia", "More Info in this link");
    VaccinationForm form;
    Hr hr = new Hr();
    private final VaccinationService service;

    public VaccinationsView(VaccinationService service) {
        this.service = service;
        addClassName("list-view");
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
        form = new VaccinationForm(service.getVaccinations());
        form.setWidth("25em");
        form.addSaveListener(this::saveVaccination);
        form.addDeleteListener(this::deleteVaccination);
        form.addCloseListener(e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("song-grid");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSizeFull();
        grid.addColumn(Vaccination::getVacId).setHeader("Vaccination id").setSortable(true);
        grid.addColumn(Vaccination::getDiseaseName).setHeader("Disease Name").setSortable(true);
        grid.addColumn(Vaccination::getVacType).setHeader("Type").setSortable(true);
        grid.addColumn(Vaccination::getLastVac).setHeader("Last vaccination").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editVaccination(event.getValue()));
    }

    private com.vaadin.flow.component.Component getToolbar() {
        Button addVaccinationButton = new Button("Add vaccination");
        addVaccinationButton.addClickListener(click -> addVaccination());
        linkTravelVac.setTarget("_blank");
        var toolbar = new VerticalLayout(addVaccinationButton, multiple, hr, linkTravelVac);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editVaccination(Vaccination vaccination) {
        if (vaccination == null) {
            closeEditor();
        } else {
            form.setVaccination(vaccination);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        form.setVaccination(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addVaccination() {
        grid.asSingleSelect().clear();
        editVaccination(new Vaccination());
    }
    private void updateList() {
        grid.setItems(service.getVaccinations());
    }

    private void saveVaccination(VaccinationForm.SaveEvent event) {
        service.saveVaccination(event.getVaccination());
        updateList();
        closeEditor();
    }
    private void deleteVaccination(VaccinationForm.DeleteEvent event) {
        service.deleteVaccination(event.getVaccination());
        updateList();
        closeEditor();
    }
}
