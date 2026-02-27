package com.st.elitho.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.st.elitho.dto.ELithoRecipeDetectionDTO;
import com.st.elitho.ejb.ELithoRecipeDetectionEJB;

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
public final class ELithoRecipeDetectionBean
	extends AbstractTableBean<ELithoRecipeDetectionDTO, ELithoRecipeDetectionEJB> implements Serializable {

	private static final long serialVersionUID = -6883957043525238825L;
	private List<ELithoRecipeDetectionDTO> selectedItems;
	private ELithoRecipeDetectionDTO selectedItem;
	@EJB
	private transient ELithoRecipeDetectionEJB eLithoRecipeDetectionEJB;

	@Override
	public List<ELithoRecipeDetectionDTO> getConcreteSelectedItems() {
		return this.selectedItems;
	}

	@Override
	public void apply() {

		apply(this.eLithoRecipeDetectionEJB);
		Collections.sort(getItems(), Comparator.comparing(ELithoRecipeDetectionDTO::getRecipeName));

	}

	public void add() {
		super.add(ELithoRecipeDetectionDTO.builder().build().getDefault());
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
		super.save(this.eLithoRecipeDetectionEJB);
	}

	public void resetTable() {
		super.resetTable("elithoTabView:elithorecipedetectionForm:elithorecipedetectionDT");
	}

}
