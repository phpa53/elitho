package com.st.elitho.ejb;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.jpa.ExposureTwinscanLot;
import com.st.elitho.uti.LoggerWrapper;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Startup
@Data
@Slf4j
public class LotRefreshEJB implements Serializable {

	private static final long serialVersionUID = 6117841041627871684L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LotRefreshEJB.class);
	private final transient List<LotDTO> lots = new ArrayList<>();
	@SuppressWarnings("resource")
	@PersistenceContext(unitName = "elithoPU")
	private transient EntityManager elithoEM;

	@PostConstruct
	public void init() {
		refresh();
	}

	@Lock(LockType.READ)
	@Schedule(hour = "*", minute = "*/10", second = "0", persistent = false)
	public void refresh() {

		this.lots.clear();
		this.lots.addAll(this.elithoEM.createQuery("select etl from ExposureTwinscanLot etl", ExposureTwinscanLot.class)
			.getResultList().stream().map(ExposureTwinscanLot::toDTO).toList());
		LoggerWrapper.info(LOGGER, String.format("%d lots loaded", this.lots.size()));

	}

	public List<LotDTO> getAllLots() {
		return this.lots;
	}

}
