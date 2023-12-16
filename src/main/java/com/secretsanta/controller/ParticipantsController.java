package com.secretsanta.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.secretsanta.model.Participants;
import com.secretsanta.service.ParticipantsService;

@RestController
public class ParticipantsController {

	public static final Logger logger = LoggerFactory.getLogger(ParticipantsController.class);

	@Autowired
	ParticipantsService participantsService;

	@PostMapping("/addParticipant")
	private ResponseEntity<Map<String, Object>> addParticipant(@RequestBody Participants participants) {
		try {
			logger.info("Inside addParticipant :: ");
			return new ResponseEntity<>(participantsService.saveOrUpdate(participants), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occured while addParticipant {} :Reason :{}", participants.getEmailAddress(),
					e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getAllParticipants")
	public ResponseEntity<Map<String, Object>> getAllParticipants() {
		try {
			logger.info("Inside getAllParticipants :: ");
			return new ResponseEntity<>(participantsService.getAllParticipants(), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error occured while getAllParticipants {} :Reason :{}", "", e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/deleteParticipant")
	public ResponseEntity<Map<String, Object>> deleteParticipant(@RequestParam(value = "id") String id) {
		try {
			logger.info("Inside deleteParticipant : id : " + id);
			return new ResponseEntity<>(participantsService.deleteParticipant(Integer.valueOf(id)), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error occured while deleteParticipant {} :Reason :{}", id, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/editParticipant")
	public ResponseEntity<Map<String, Object>> editParticipant(@RequestParam("id") String id,
			@RequestParam("name") String name) {
		try {
			logger.info("Inside editParticipant : id : " + id);
			return new ResponseEntity<>(participantsService.editParticipant(id, name), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error occured while editParticipant {} :Reason :{}", name, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("suffleParticipant")
	public ResponseEntity<Map<String, Object>> suffleParticipant() {
		try {
			logger.info("Inside suffleParticipant :: ");
			return new ResponseEntity<>(participantsService.suffleParticipant(), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error occured while suffleParticipant {} :Reason :{}", "", e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
