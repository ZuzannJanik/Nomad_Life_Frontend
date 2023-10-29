package nomad_vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nomad_vaadin.MainLayout;
import nomad_vaadin.data.domain.Answer;
import nomad_vaadin.data.domain.Medicine;
import nomad_vaadin.service.AnswerService;
import nomad_vaadin.service.MedicineService;
import org.springframework.stereotype.Component;

@Route(value = "medicines", layout = MainLayout.class)
@PageTitle("Medicines")
@Uses(Icon.class)
@Component
public class MedicinesView extends VerticalLayout {
        Grid<Medicine> grid = new Grid<>();
        TextField filterText = new TextField();
        MedicinesForm form;
        H6 textSearch = new H6("You don't remember why you have the medicine?");
        Paragraph textMedium = new Paragraph("Info about medicine");
        H6 moreInfo = new H6("More information at the link");
        Anchor linkGoogle = new Anchor("link", "More Info in this link");
        private final MedicineService service;
        private final AnswerService answerService;
        private Answer answer;

        public MedicinesView(MedicineService service, AnswerService answerService) {
            this.service = service;
            this.answerService = answerService;

            addClassName("medicines-view");
            setSizeFull();
            configureGrid();
            configureForm();
            add(getToolbar(), getContent());
            updateList();
            closeEditor();
        }
        private HorizontalLayout getContent() {
            HorizontalLayout content = new HorizontalLayout(grid, form);
            content.setFlexGrow(1, form);
            content.setFlexGrow(2, grid);
            content.addClassNames("content");
            content.setSizeFull();
            return content;
        }
        private void configureForm() {
            form = new MedicinesForm(service.getMedicines());
            form.setWidth("40em");
            form.addSaveListener(this::saveMedicine);
            form.addDeleteListener(this::deleteMedicine);
            form.addCloseListener(e -> closeEditor());
        }

        private void configureGrid() {
            grid.addClassNames("medicine-grid");
            grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
            grid.setSizeFull();
            grid.addColumn(Medicine::getMedicineId).setHeader("Medicine id").setSortable(true);
            grid.addColumn(Medicine::getMedicineName).setHeader("Medicine Name").setSortable(true);
            grid.addColumn(Medicine::getDesignation).setHeader("Designation").setSortable(true);
            grid.addColumn(Medicine::getMedType).setHeader("Type").setSortable(true);
            grid.addColumn(Medicine::getExpiryDate).setHeader("Expiry date").setSortable(true);
            grid.getColumns().forEach(col -> col.setAutoWidth(true));
            grid.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() != null) { editMedicine(event.getValue()); } });
        }

        private com.vaadin.flow.component.Component getToolbar() {
            filterText.setPlaceholder("Search in google...");
            filterText.setClearButtonVisible(true);
            filterText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
            filterText.setPattern("^[a-zA-Z\\s]+");
            filterText.setErrorMessage("Letter only. Min 2 chars");

            textMedium.setVisible(false);
            moreInfo.setVisible(false);
            linkGoogle.setVisible(false);
            Button addMedicineButton = new Button("Add medicine");
            addMedicineButton.addClickListener(click -> addMedicine());

            Button searchMedicineButton = new Button("Search");
            searchMedicineButton.addClickListener(click -> searchMedicine());

            var toolbar = new VerticalLayout(addMedicineButton, textSearch, filterText, searchMedicineButton, textMedium, moreInfo, linkGoogle);
            toolbar.addClassName("toolbar");
            return toolbar;
        }
        public void searchMedicine() {
            if (filterText.isInvalid()) {
                textMedium.setText("Invalid question. Ask in English");
            } else {
                Answer newAnswer = new Answer();
                newAnswer = answerService.getNewSnippet(filterText.getValue());
                textMedium.setVisible(true);
                textMedium.setText(newAnswer.getSnippet());
                moreInfo.setVisible(true);
                linkGoogle.setVisible(true);
                linkGoogle.setTarget("_blank");
                linkGoogle.setHref(newAnswer.getLink());
            }
        }

        public void editMedicine(Medicine medicine) {
            if (medicine == null) {
                closeEditor();
            } else {
                form.setMedicine(medicine);
                form.setVisible(true);
                addClassName("editing");
            }
        }

        private void closeEditor() {
            form.setMedicine(null);
            form.setVisible(false);
            removeClassName("editing");
        }

        private void updateList() {
            grid.setItems(service.getMedicines());
        }
        private void addMedicine() {
            grid.asSingleSelect().clear();
            editMedicine(new Medicine());
        }

        private void saveMedicine(MedicinesForm.SaveEvent event) {
            service.saveMedicine(event.getMedicine());
            updateList();
            closeEditor();
        }

        private void deleteMedicine(MedicinesForm.DeleteEvent event) {
            service.deleteMedicine(event.getMedicine());
            updateList();
            closeEditor();
        }
    }
