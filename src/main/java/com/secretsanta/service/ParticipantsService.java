package com.secretsanta.service;

import java.util.Map;

import com.secretsanta.model.Participants;

public interface ParticipantsService {

	public Map<String, Object> saveOrUpdate(Participants participants);

	public Map<String, Object> deleteParticipant(int id);

	public Map<String, Object> getAllParticipants();

	public Map<String, Object> editParticipant(String id, String name);

	public Map<String, Object> suffleParticipant();
}
