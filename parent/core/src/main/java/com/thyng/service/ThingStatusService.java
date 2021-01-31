package com.thyng.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Source;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingInfo;
import com.thyng.domain.model.ThingStatusChange;
import com.thyng.domain.model.ThingStatusChangeInfo;
import com.thyng.repository.MetricRepository;
import com.thyng.repository.ObjectRepository;
import com.thyng.repository.Repository;
import com.thyng.util.Constant;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Builder
@RequiredArgsConstructor
public class ThingStatusService implements Lifecycle {
	
	@Value
	@Builder
	public static class Data {
		private final Long now;
		private final List<Thing> things;
		private final Map<String, Long> lastMetricTimestamps;
		private final Map<String, ThingStatusChange> thingStatusChanges;
	}

	@NonNull private final Source<Thing> thingSource;
	@NonNull private final MetricRepository metricRepository;
	@NonNull private final Repository<ThingInfo> thingInfoRepository;
	@NonNull private final ObjectRepository<ThingStatusChangeInfo> thingStatusChangeInfoRepository;
	
	public Data updateAll() {
		final Long now = System.currentTimeMillis();
		final Map<String, Long> lastMetricTimestamps = metricRepository.findAllLatestTimestamps();
		final ThingStatusChangeInfo statusChangeInfo = thingStatusChangeInfoRepository.get(Constant.THING_STATUS_CHANGE_INFO);
		
		final List<Thing> things = thingSource.findAll();
		final Map<String, ThingStatusChange> delta = new HashMap<>();
		for(Thing thing : things) {
			final Long lastMetricTimestamp = lastMetricTimestamps.getOrDefault(thing.getId(), 0l);
			final Boolean online = lastMetricTimestamp > now - thing.getInactivityPeriod() * 1000 * 60;
			final ThingStatusChange change = statusChangeInfo.getChanges().getOrDefault(thing.getId(), ThingStatusChange.builder()
					.id(thing.getId())
					.online(Boolean.FALSE)
					.timestamp(0l)
					.build());
			if(!Objects.equals(online, change.getOnline())) {
				delta.put(thing.getId(), ThingStatusChange.builder()
						.id(thing.getId())
						.online(online)
						.timestamp(lastMetricTimestamp)
						.build());
			}
		}
		persistThingInfo(delta);
		persistThingStatusChangeInfo(delta, statusChangeInfo);
		return Data.builder()
				.now(now)
				.things(things)
				.thingStatusChanges(delta)
				.lastMetricTimestamps(lastMetricTimestamps)
				.build();
	}
	
	private void persistThingInfo(final Map<String, ThingStatusChange> delta) {
		final List<ThingInfo> results = new ArrayList<>(delta.size());
		for(ThingStatusChange change : delta.values()) {
			results.add(ThingInfo.builder()
					.id(change.getId())
					.online(change.getOnline())
					.lastStatusChangeTimestamp(change.getTimestamp())
					.build());
		}
		thingInfoRepository.saveAll(results);
	}
	
	private void persistThingStatusChangeInfo(final Map<String, ThingStatusChange> delta, ThingStatusChangeInfo statusChangeInfo) {
		final Map<String, ThingStatusChange> changes = new HashMap<>(statusChangeInfo.getChanges());
		changes.putAll(delta);
		final ThingStatusChangeInfo result = statusChangeInfo.withChanges(changes); 
		thingStatusChangeInfoRepository.put(result);
	}
}
