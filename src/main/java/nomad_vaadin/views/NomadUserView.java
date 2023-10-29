package nomad_vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nomad_vaadin.MainLayout;
import nomad_vaadin.data.domain.NomadUser;
import nomad_vaadin.service.NomadUserService;
import org.springframework.stereotype.Component;

@PageTitle("NomadUser")
@Route(value = "", layout = MainLayout.class)
@Uses(Icon.class)
@Component
public class NomadUserView extends VerticalLayout {
    Grid<NomadUser> grid = new Grid<>();
    NomadUserForm form;
    H6 info = new H6("This view is only to show how the backend works! This is not good practice when it comes to storing passwords and logging in!");

    private final NomadUserService service;

    public NomadUserView(NomadUserService service) {
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
        form = new NomadUserForm(service.getUsers());
        form.setWidth("25em");
        form.addSaveListener(this::saveNomadUser);
        form.addDeleteListener(this::deleteNomadUser);
        form.addCloseListener(e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("song-grid");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSizeFull();
        grid.addColumn(NomadUser::getUserId).setHeader("User id").setSortable(true);
        grid.addColumn(NomadUser::getFirstName).setHeader("First Name").setSortable(true);
        grid.addColumn(NomadUser::getSurname).setHeader("Surname").setSortable(true);
        grid.addColumn(NomadUser::getHomeland).setHeader("Homeland").setSortable(true);
        grid.addColumn(NomadUser::getLogin).setHeader("Login").setSortable(true);
        grid.addColumn(NomadUser::getRole).setHeader("Role").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editNomadUser(event.getValue()));
    }

    private com.vaadin.flow.component.Component getToolbar() {
        Button addNomadUserButton = new Button("Add user");
        addNomadUserButton.addClickListener(click -> addNomadUser());
        var toolbar = new VerticalLayout(info, addNomadUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editNomadUser(NomadUser nomadUser) {
        if (nomadUser == null) {
            closeEditor();
        } else {
            form.setNomadUser(nomadUser);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        form.setNomadUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addNomadUser() {
        grid.asSingleSelect().clear();
        editNomadUser(new NomadUser());
    }
    private void updateList() {
        grid.setItems(service.getUsers());
    }

    private void saveNomadUser(NomadUserForm.SaveEvent event) {
        service.saveUser(event.getNomadUser());
        updateList();
        closeEditor();
    }
    private void deleteNomadUser(NomadUserForm.DeleteEvent event) {
        service.deleteUser(event.getNomadUser());
        updateList();
        closeEditor();
    }
}
