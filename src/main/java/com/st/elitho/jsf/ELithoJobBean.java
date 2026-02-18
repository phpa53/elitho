package com.st.elitho.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.st.elitho.dto.ELithoJobDTO;
import com.st.elitho.ejb.ELithoJobEJB;

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
public final class ELithoJobBean extends AbstractTableBean<ELithoJobDTO, ELithoJobEJB> implements Serializable {

	private static final long serialVersionUID = -3389167920821616173L;
	private List<ELithoJobDTO> selectedItems;
	@EJB
	private transient ELithoJobEJB alertJobEJB;

	@Override
	public List<ELithoJobDTO> getConcreteSelectedItems() {
		return this.selectedItems;
	}

	@Override
	public void apply() {

		apply(this.alertJobEJB);
		Collections.sort(getItems(), Comparator.comparing(ELithoJobDTO::getMachineId));

	}

	public void add() {
		super.add(ELithoJobDTO.builder().build().getDefault());
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
		super.save(this.alertJobEJB);
	}

	public void resetTable() {
		super.resetTable("fsmTabView:alertjobForm:alertjobDT");
	}

}
