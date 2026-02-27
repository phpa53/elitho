package com.st.elitho.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.st.elitho.dto.ELithoMachineDTO;
import com.st.elitho.ejb.ELithoMachineEJB;

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
public final class ELithoMachineBean extends AbstractTableBean<ELithoMachineDTO, ELithoMachineEJB>
	implements Serializable {

	private static final long serialVersionUID = -9146193013252097911L;
	private List<ELithoMachineDTO> selectedItems;
	private ELithoMachineDTO selectedItem;
	private List<String> detectionsToAdd;
	private String detectionLabel;
	private List<String> defectsToAdd;
	private String defectLabel;
	private List<String> notificationsToAdd;
	private String notificationLabel;
	private String columnToExpand;
	private final transient List<String> detections = new ArrayList<>();
	private final transient List<String> previousDetections = new ArrayList<>();
	private final transient List<String> defects = new ArrayList<>();
	private final transient List<String> previousDefects = new ArrayList<>();
	private final transient List<String> notifications = new ArrayList<>();
	private final transient List<String> previousNotifications = new ArrayList<>();
	@EJB
	private transient ELithoMachineEJB eLithoJobEJB;

	@Override
	public List<ELithoMachineDTO> getConcreteSelectedItems() {
		return this.selectedItems;
	}

	@Override
	public void apply() {

		apply(this.eLithoJobEJB);
		Collections.sort(getItems(), Comparator.comparing(ELithoMachineDTO::getMachineId));

	}

	public void add() {
		super.add(ELithoMachineDTO.builder().build().getDefault());
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
		super.save(this.eLithoJobEJB);
	}

	public void resetTable() {
		super.resetTable("elithoTabView:elithomachineForm:elithomachineDT");
	}

	public List<String> completeDetection(final String str) {
        return this.detections.stream()
        	.filter(detection -> detection.toLowerCase(Locale.ENGLISH).contains(str.toLowerCase(Locale.ENGLISH)))
        	.toList();
    }

}
