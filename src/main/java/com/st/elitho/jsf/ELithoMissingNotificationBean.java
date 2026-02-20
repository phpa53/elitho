package com.st.elitho.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import org.primefaces.event.SelectEvent;

import com.st.elitho.dto.ELithoMissingNotificationDTO;
import com.st.elitho.ejb.ELithoMissingNotificationEJB;
import com.st.elitho.uti.LoggerWrapper;

import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Named
@ViewScoped
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public final class ELithoMissingNotificationBean
	extends AbstractTableBean<ELithoMissingNotificationDTO, ELithoMissingNotificationEJB> implements Serializable {

	private static final long serialVersionUID = -4586666283583917350L;
	private static final String EMAIL_LBL = "email";
	private List<ELithoMissingNotificationDTO> selectedItems;
	private ELithoMissingNotificationDTO selectedItem;
	private List<String> emailsToAdd;
	private String emailMessage;
	private final transient List<String> emails = new ArrayList<>();
	private final transient List<String> previousEmails = new ArrayList<>();
	@EJB
	private transient ELithoMissingNotificationEJB eLithoMissNotifEJB;

	@Override
	public List<ELithoMissingNotificationDTO> getConcreteSelectedItems() {
		return this.selectedItems;
	}

	@Override
	public void apply() {

		apply(this.eLithoMissNotifEJB);
		Collections.sort(getItems(), Comparator.comparing(ELithoMissingNotificationDTO::getRecipeName));
		this.emails.clear();
		this.emails.addAll(getItems().stream().map(ELithoMissingNotificationDTO::getEmails).flatMap(List::stream)
			.distinct().sorted().toList());

	}

	public void add() {
		super.add(ELithoMissingNotificationDTO.builder().build().getDefault());
	}

	public void copy() {

		if (Optional.ofNullable(this.selectedItems).orElse(new ArrayList<>()).size() == 1) {
			super.copy(this.selectedItems.get(0).getCopy());
		}

	}

	public void delete() {
		super.delete(this.selectedItems);
	}

	public void save() {
		super.save(this.eLithoMissNotifEJB);
	}

	public void resetTable() {
		super.resetTable("elithoTabView:elithojobForm:elithojobDT");
	}

	public List<String> completeEmail(final String pattern) {
        return completeList(this.emails, pattern);
    }

	public void addSelectedEmailToList(final SelectEvent<String> event) {
		try {
			this.emailMessage = addSelectedToList(event, EMAIL_LBL,
				this.selectedItem == null ? new ArrayList<>() : this.selectedItem.getEmails(), false);
			this.emailsToAdd = new ArrayList<>();
		} catch (final ELithoTableException e) {
			LoggerWrapper.debug(log, e.getMessage());
		}
    }

	public void validateLastEmail() {
		try {
			this.emailMessage = validateLastEmail(EMAIL_LBL,
				this.selectedItem == null ? new ArrayList<>() : this.selectedItem.getEmails());
		} catch (final ELithoTableException e) {
			LoggerWrapper.debug(log, e.getMessage());
		}
    }

	public void resetEmailsChanges() {

    	this.previousEmails.clear();
    	if (this.selectedItem != null) {
			this.previousEmails.addAll(Optional.ofNullable(this.selectedItem.getEmails()).orElse(new ArrayList<>()));
		}

    }

	public void checkEmailsChanges() {

    	if (this.selectedItem != null
    		&& !Optional.ofNullable(this.selectedItem.getEmails()).orElse(new ArrayList<>()).isEmpty()
			&& !this.previousEmails.isEmpty()
			&& !new TreeSet<>(this.selectedItem.getEmails())
				.equals(new TreeSet<>(this.previousEmails))) {

			this.selectedItem.setChangedAttributeName("Emails");
			setAnyItemChanged(true);
			setNumberOfChanges(getNumberOfChanges() + 1);

		}

    }

}
