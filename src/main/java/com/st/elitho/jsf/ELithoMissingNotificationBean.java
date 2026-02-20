package com.st.elitho.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.st.elitho.dto.ELithoMissingNotificationDTO;
import com.st.elitho.ejb.ELithoMissingNotificationEJB;

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
	private List<ELithoMissingNotificationDTO> selectedItems;
	private ELithoMissingNotificationDTO selectedItem;
	private List<String> emailsToAdd;
	private String emailListLabel;
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

	public List<String> completeDetection(final String str) {
        return this.emails.stream()
        	.filter(email -> email.toLowerCase(Locale.ENGLISH).contains(str.toLowerCase(Locale.ENGLISH)))
        	.toList();
    }

}
