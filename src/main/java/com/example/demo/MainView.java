package com.example.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    private final CustomerRepository repo;
    final Grid<Customer> grid;
    final TextField filter;
    private final Button addNewBtn;

    public MainView(CustomerRepository repo) {
        this.repo = repo;
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Add your people");

        // Настройка поля поиска
        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        // Настройка кнопки добавления тестового клиента
        addNewBtn.addClickListener(e -> {
            repo.save(new Customer("Alex", "Novak"));
            listCustomers(filter.getValue());
        });

        // Компоновка интерфейса
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid);

        // Настройка колонок таблицы
        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "lastName");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        // Первичная загрузка данных
        listCustomers("");
    }

    private void listCustomers(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            grid.setItems(repo.findAll());
        } else {
            grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }
}
