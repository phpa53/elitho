package com.st.elitho.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.st.elitho.dto.AbstractTableDTO;
import com.st.elitho.dto.ELithoRecipeDefectClassDTO;
import com.st.elitho.dto.ELithoRecipeDefectDTO;
import com.st.elitho.ejb.ELithoRecipeDefectEJB;

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
public final class ELithoRecipeDefectBean
	extends AbstractTableBean<ELithoRecipeDefectDTO, ELithoRecipeDefectEJB> implements Serializable {

	private static final long serialVersionUID = 7872755669645300443L;
	private List<ELithoRecipeDefectDTO> selectedItems;
	private ELithoRecipeDefectDTO selectedItem;
	@EJB
	private transient ELithoRecipeDefectEJB eLithoRecipeDefectEJB;

	@Override
	public List<ELithoRecipeDefectDTO> getConcreteSelectedItems() {
		return this.selectedItems;
	}

	@Override
	public void apply() {

		apply(this.eLithoRecipeDefectEJB);
		Collections.sort(getItems(), Comparator.comparing(ELithoRecipeDefectDTO::getRecipeName));

	}

	public void add() {
		super.add(ELithoRecipeDefectDTO.builder().build().getDefault());
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
		super.save(this.eLithoRecipeDefectEJB);
	}

	public void resetTable() {
		super.resetTable("elithoTabView:elithorecipedefectForm:elithorecipedefectDT");
	}

	@SuppressWarnings("static-method")
	public void addDefectClass(final ELithoRecipeDefectDTO item) {

	    if (item.getDefectClasses() == null) {
	        item.setDefectClasses(new ArrayList<>());
	    }

	    final ELithoRecipeDefectClassDTO newClass = ELithoRecipeDefectClassDTO.builder()
	        .name(AbstractTableDTO.VALUE_DEFAULT)
	        .minHeight(0)
	        .minSize(0)
	        .minDensity(0)
	        .build();

	    item.getDefectClasses().add(newClass);

	}

}
