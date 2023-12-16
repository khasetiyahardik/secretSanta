package com.secretsanta.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.secretsanta.constant.ApplicationConstant;
import com.secretsanta.model.Participants;
import com.secretsanta.model.SendReceiveList;
import com.secretsanta.repository.ParticipantsRepository;
import com.secretsanta.service.ParticipantsService;

@Service
public class ParticipantsServiceImpl implements ParticipantsService {

	@Autowired
	ParticipantsRepository participantsRepository;

	Map<String, Object> map = new HashMap<String, Object>();
	
	
    /*
     * This method is used to add participants in application.
     * 
     */
	public Map<String, Object> saveOrUpdate(Participants participants) {
		Optional<Participants> participant = participantsRepository
				.findOneByEmailIgnoreCase(participants.getEmailAddress());

		if (!participant.isPresent()) {
			Participants entity = participantsRepository.save(participants);
			if (entity != null) {
				map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
				map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.REGISTRATION_SUCCESS);
				map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
			}
		} else {
			map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
			map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.REGISTRATION_EMAIL_EXISTS);
			map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
		}
		return map;
	}

	/**
	 * This method used to delete participant from the application.
	 */
	public Map<String, Object> deleteParticipant(int id) {
		try {
			participantsRepository.deleteById(id);
			map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
			map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.PARTICIPANT_DELETE_SUCCESS);
			map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
		} catch (Exception e) {
			map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
			map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SOMETHING_WENT_WRONG);
			map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
		}
		return map;
	}

	/**
	 * This method is used to get all the participants from application.
	 */
	public Map<String, Object> getAllParticipants() {
		List<Participants> ParticipantDetails = new ArrayList<Participants>();
		ParticipantDetails = participantsRepository.findAll();
		map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
		if (ParticipantDetails.size() > 0)
			map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.PARTICIPANTS_FOUND);
		else
			map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.NO_PARTICIPANTS_FOUND);
		map.put(ApplicationConstant.RESPONSE_DATA, ParticipantDetails);
		return map;
	}

	/**
	 * This method is used to edit participant.
	 */
	public Map<String, Object> editParticipant(String id, String name) {
		Optional<Participants> participantEntity = participantsRepository.findById(Integer.valueOf(id));
		try {
			if (participantEntity != null) {
				Participants entity = participantEntity.get();
				entity.setName(name);
				participantsRepository.save(entity);
				map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
				map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_EDIT_SUCCESS);
				map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
			}
		} catch (Exception e) {
			map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
			map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SOMETHING_WENT_WRONG);
			map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
		}
		return map;
	}

	/**
	 * This method is used to suffle participants and generates sender and receiver list for gift exchange ceremony.
	 */
	public Map<String, Object> suffleParticipant() {
		List<String> ParticipantNameList = new ArrayList<String>();
		List<String> ParticipantSuffleNameList = new ArrayList<String>();
		List<SendReceiveList> sendReceiveList = new ArrayList<>();

		List<Participants> ParticipantDetails = participantsRepository.findAll();
		for (int i = 0; i < ParticipantDetails.size(); i++) {
			ParticipantNameList.add(
					ParticipantDetails.get(i).getName() + " (" + ParticipantDetails.get(i).getEmailAddress() + ")");
		}
		ParticipantSuffleNameList.addAll(ParticipantNameList);

		if (ParticipantNameList.size() != 1) {
			Collections.shuffle(ParticipantNameList, new Random(2));
			boolean shuffled = false;
			outer: while (!shuffled) {
				Collections.shuffle(ParticipantNameList);
				shuffled = true;
				for (int j = 0; j < ParticipantNameList.size(); j++) {
					if (ParticipantNameList.get(j).equals(ParticipantSuffleNameList.get(j))) {
						shuffled = false;
						continue outer;
					}
				}
			}

		}
		if (ParticipantNameList.size() == 1) {
			SendReceiveList enitity = new SendReceiveList();
			enitity.setSendUsername(ParticipantNameList.get(0));
			enitity.setReceiveUsername(ParticipantNameList.get(0));
			sendReceiveList.add(enitity);

		} else {
			for (int j = 0; j < ParticipantNameList.size(); j++) {
				SendReceiveList enitity = new SendReceiveList();
				enitity.setSendUsername(ParticipantNameList.get(j));
				enitity.setReceiveUsername(ParticipantSuffleNameList.get(j));
				sendReceiveList.add(enitity);

			}
		}
		map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
		if (sendReceiveList.size() == 0)
			map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.NO_PARTICIPANTS_FOUND);
		else if (sendReceiveList.size() > 0)
			map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SUFFLE_SUCCESS);
		else
			map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SUFFLE_FAILURE);
		map.put(ApplicationConstant.RESPONSE_DATA, sendReceiveList);
		return map;
	}

}
