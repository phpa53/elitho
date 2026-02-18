package com.st.elitho.ejb;

import java.util.List;
import java.util.Optional;

import com.st.elitho.dto.AbstractTableDTO;
import com.st.elitho.jpa.AbstractTable;
import com.st.elitho.uti.LoggerWrapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public abstract class AbstractTableEJB<T extends AbstractTable<U, ?>, U extends AbstractTableDTO<?, ?, ?>> {

	/*
	@Inject
	private AlertJobRepo alertJobRepo;
	*/

	@SuppressWarnings("resource")
	@PersistenceContext(unitName = "elithoPU")
	private transient EntityManager elithoEM;

	protected abstract Class<T> getEntityClass();

	public List<U> findAll() {
		return this.elithoEM.createQuery(String.format("select obj from %s obj", getEntityClass().getSimpleName()),
			getEntityClass()).getResultList().stream().map(AbstractTable::toDTO).toList();
	}

	public void save(final List<U> items) {

		if (Optional.ofNullable(items).orElse(List.of()).isEmpty()) {

			LoggerWrapper.info(log, "No item to save");

		} else {

			LoggerWrapper.info(log, String.format("Saving %s items", items.get(0).getClass()));

			final var dtos = findAll();
			final var deleted = dtos.stream()
				.filter(dto -> items.stream().filter(item-> item.getPK().equals(dto.getPK())).toList().isEmpty())
				.toList();
			final var modified = items.stream()
				.filter(item -> !dtos.stream().filter(
					dto -> dto.getPK().equals(item.getPK()) && !dto.equals(item)).toList().isEmpty())
				.toList();
			final var created = items.stream()
				.filter(item -> dtos.stream().filter(dto -> dto.getPK().equals(item.getPK())).toList().isEmpty())
				.toList();

			deleted.forEach(dto -> {

				final var entity = this.elithoEM.find(getEntityClass(), dto.getPK());

				if (entity != null) {
					this.elithoEM.remove(entity);
				}

			});
			modified.forEach(dto -> this.elithoEM.merge(dto.toEntity()));
			created.forEach(dto -> this.elithoEM.persist(dto.toEntity()));

			LoggerWrapper.info(log, String.format("%s: %d deleted, %d modified, %d created",
				getEntityClass().getSimpleName(), deleted.size(), modified.size(), created.size()));

		}

	}

}
