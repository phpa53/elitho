package com.st.elitho.jsf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;

import com.st.elitho.dto.AbstractTableDTO;
import com.st.elitho.ejb.AbstractTableEJB;
import com.st.elitho.uti.AppContext;
import com.st.elitho.uti.LoggerUtils;
import com.st.elitho.uti.LoggerWrapper;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public abstract class AbstractTableBean<T extends AbstractTableDTO<?, ?, ?>, U extends AbstractTableEJB<?, T>> {

	private final transient List<T> items = new ArrayList<>();
	private boolean anyItemChanged;
	private int numberOfChanges;
	private int numberOfDeletes;

	@PostConstruct
	public void init() {
		apply();
	}

	public abstract List<T> getConcreteSelectedItems();
	public abstract void apply();

	public void apply(final U ejb) {

		checkKeyUniqueness();

		this.anyItemChanged = false;
		this.numberOfChanges = 0;
		this.numberOfDeletes = 0;
		this.items.clear();
		this.items.addAll(ejb.findAll());

		LoggerWrapper.info(log,
			this.items.isEmpty() ? "No data found" : String.format("Found %d rows", this.items.size()));

	}

	public void valueChanged(final ValueChangeEvent event) {

	    final boolean valueChanged;

	    if (event.getOldValue() instanceof String || event.getOldValue() instanceof Number) {
	    	valueChanged = !Objects.equals(event.getOldValue(), event.getNewValue());
	    } else {
	    	valueChanged = false;
	    }

	    if (valueChanged) {

		    @SuppressWarnings("unchecked")
			final var item = (T) event.getComponent().getAttributes().get("item");
		    final var attribute = (String) event.getComponent().getAttributes().get("attribute");

		    item.setChangedAttributeName(attribute);
		    setDates(item);
			this.anyItemChanged = true;
			this.numberOfChanges =
				(int) this.items.stream().filter(dto -> !dto.getChangedAttributes().isEmpty()).count();

	    }

	}

	public void booleanValueChanged(final ValueChangeEvent event) {

	    final var oldValue = (Boolean) event.getOldValue();
	    final var newValue = (Boolean) event.getNewValue();

	    if (!Objects.equals(oldValue, newValue)) {

		    @SuppressWarnings("unchecked")
			final var item = (T) event.getComponent().getAttributes().get("item");
		    final var attribute = (String) event.getComponent().getAttributes().get("attribute");

		    item.setChangedAttributeName(attribute);
		    setDates(item);
			this.anyItemChanged = true;
			this.numberOfChanges =
				(int) this.items.stream().filter(dto -> !dto.getChangedAttributes().isEmpty()).count();

	    }

	}

	private void setDates(final T item) {

		if (Optional.ofNullable(item.getCreatedDate()).orElse(LocalDateTime.MIN).equals(LocalDateTime.MIN)
			|| Optional.ofNullable(item.getCreatedBy()).orElse("").isEmpty()) {

			item.setCreatedDate(LocalDateTime.now());
			item.setCreatedBy(AppContext.getUserName());
			item.setLastModifiedDate(LocalDateTime.MIN);
			item.setLastModifiedBy("");

		} else {

			item.setLastModifiedDate(LocalDateTime.now());
			item.setLastModifiedBy(AppContext.getUserName());

		}

	}

	public void checkKeyUniqueness() {

		this.items.forEach(item -> item.setDuplicatedPK(false));
		for (var i = 0; i < this.items.size(); i++) {
			for (var j = i + 1; j < this.items.size(); j++) {
				if (this.items.get(i).getPK().equals(this.items.get(j).getPK())) {
					this.items.get(i).setDuplicatedPK(true);
					this.items.get(j).setDuplicatedPK(true);
				}
			}
		}

	}

	public void add(final T newItem) {

		setDates(newItem);
		this.items.add(0, newItem);

	}

	public void delete(final List<T> selectedItems) {

		if (selectedItems == null) {

			LoggerUtils.warn(log, "No selected items to delete");

		} else {

			final var numberOfSelected = selectedItems.size();
			final var newItems = getItems().stream()
				.filter(dto -> !selectedItems.stream()
					.filter(selected -> selected.getPK().equals(dto.getPK())).findFirst().isPresent())
				.toList();

			this.items.clear();
			this.items.addAll(newItems);
			selectedItems.clear();
			this.anyItemChanged = true;
			this.numberOfDeletes++;

			LoggerUtils.info(log, String.format("%d row(s) deleted", numberOfSelected));

		}

	}

	public void copy(final T item) {

		final var index = this.items.indexOf(getConcreteSelectedItems().get(0));

		if (index >= 0) {

			this.items.add(index + 1, item);
			checkKeyUniqueness();
			this.anyItemChanged = true;

			LoggerUtils.info(log, "One row added");

		}

	}

	public boolean copyDisabled() {
		return Optional.ofNullable(getConcreteSelectedItems()).orElse(new ArrayList<>()).isEmpty()
			|| getConcreteSelectedItems().size() > 1;
	}

	public boolean saveDisabled() {
		return !isAnyItemChanged()
			|| this.items.stream().filter(AbstractTableDTO::isDuplicatedPK).findFirst().isPresent();
	}

	public void discard() {

		final var numberOfDiscarded = this.numberOfChanges;

		apply();
		LoggerUtils.info(log, String.format("%d row(s) discarded", numberOfDiscarded));

	}

	public String getDeleteLabel() {
		return Optional.ofNullable(getConcreteSelectedItems()).orElse(new ArrayList<>()).isEmpty() ? "&nbsp;"
			: String.format("%d row(s)", getConcreteSelectedItems().size());
	}

	public void save(final U ejb) {

		if (Optional.ofNullable(this.items).orElse(List.of()).isEmpty()) {

			LoggerUtils.info(log, "No row to save");

		} else {

			ejb.save(this.items);
			apply();
			LoggerUtils.info(log, String.format("%d rows saved", this.items.size()));

		}

	}

	public void resetTable(final String tableId) {

		final var dataTable = (DataTable) FacesContext.getCurrentInstance()
	        .getViewRoot().findComponent(tableId);

	    if (dataTable == null) {

	    	LoggerWrapper.warn(log, String.format("%s table ID not found", tableId));

	    } else {

	        dataTable.reset();
	        apply();

	    }

	}

	@SuppressWarnings("static-method")
	public List<String> completeList(final List<String> list, final String pattern) {
        return list.stream()
        	.filter(str -> str.toLowerCase(Locale.ENGLISH).contains(pattern.toLowerCase(Locale.ENGLISH)))
        	.distinct().sorted().toList();
    }

	public static String addSelectedToList(final SelectEvent<String> event, final String name,
		final List<String> values, final boolean isChip) throws ELithoTableException {

		if (Optional.ofNullable(values).orElse(new ArrayList<>()).isEmpty()) {
			throw new ELithoTableException("");
		}

        final var object = event.getObject();
        final var label = getValidationLabel(name, event.getObject(), values, isChip);

    	if (label.isEmpty()) {
			values.add(object);
		}

        return label;

    }

	public static String validateLastItem(final String name, final List<String> values) throws ELithoTableException {

		if (Optional.ofNullable(values).orElse(new ArrayList<>()).isEmpty()) {
			throw new ELithoTableException("");
		}

		final var lastItem = values.get(values.size() - 1);
    	final var label = getValidationLabel(name, lastItem, values, true);

        if (!label.isEmpty()) {
        	values.removeLast();
        }
        if (!values.contains(lastItem)) {
        	values.add(lastItem);
        }
    	return label;

    }

	private static String getValidationLabel(final String name, final String value, final List<String> values,
		final boolean isChip) {
    	return values.contains(value)
    		&& (isChip && values.subList(0, values.size() - 1).contains(value) || !isChip)
    			? String.format("%s already exists (%s)", name, value) : "";
    }

	public static String validateLastEmail(final String name, final List<String> values)
		throws ELithoTableException {

		if (Optional.ofNullable(values).orElse(new ArrayList<>()).isEmpty()) {
			throw new ELithoTableException("");
		}

		final var lastItem = values.get(values.size() - 1);
    	final var label = getEmailValidationLabel(name, lastItem, values, true);

        if (!label.isEmpty()) {
        	values.removeLast();
        }
        if (!values.contains(lastItem)) {
        	values.add(lastItem);
        }
    	return label;

    }

	private static String getEmailValidationLabel(final String name, final String value, final List<String> values,
		final boolean isChip) {
    	return !isValidEmail(value) ? String.format("Wrong %s format (%s)", name, value) // NOPMD ternary
    		: values.contains(value) && (isChip && values.subList(0, values.size() - 1).contains(value) || !isChip)
    			? String.format("%s already exists (%s)", name, value) : "";
    }

	public static boolean isValidEmail(final String email) {

    	boolean flag;

        if (Optional.ofNullable(email).orElse("").isEmpty()) {
			flag = false;
		} else {
	        try {
	            new InternetAddress(email).validate();
	            flag = true;
	        } catch (final AddressException e) {
	            log.debug(e.getMessage());
	            flag = false;
	        }
		}

        return flag;

    }

}
