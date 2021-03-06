package com.ocsin.test.services.impl;

import com.ocsin.test.dao.RoomRepository;
import com.ocsin.test.model.RoomObject;
import com.ocsin.test.services.IRoomObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomObjectService implements IRoomObjectService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoomRepository roomRepository;

    /**
     * récupère l'objet en base de données correspondant à l'id entré en paramètre
     * @param roomId
     * @return l'objet RoomObject correspondant au paramètre roomId
     */
    @Override
    public RoomObject getRoomById(int roomId){
        logger.info("##### getRoomById method #####");
        return roomRepository.findById(roomId);
    }

    /**
     * récupère l'objet en base de données correspondant au nom de salle entré en paramètre, ou un nouveau RoomObject avec le nom en paramètre
     * @param roomName
     * @return l'objet RoomObject correspondant au paramètre roomName ou un nouvel objet RoomObject
     */
    @Override
    public RoomObject getRoomByName(String roomName){
        logger.info("##### getRoomObjectByName method #####");
        RoomObject room;
        room = roomRepository.findByName(roomName);
        return room == null ? roomRepository.save(new RoomObject(roomName)) : room;

    }

    /**
     * persiste un objet RoomObject entré en paramètre
     * @param room
     */
    @Override
    public RoomObject saveRoom(RoomObject room){
        logger.info("##### saveRoomObject method #####");
        return roomRepository.save(room);
    }


    /**
     * @return l'ensemble des salles ayant chacune au moins un évènement
     */
    @Override
    public List<RoomObject> findRoomsWithAtLeastOneEvent() {
        logger.info("##### findRoomsWithAtLeastOneEvent method #####");
        return roomRepository.findDistinctRoomsByEventsIsNotNull();
    }

    /**
     *
     * @param key
     * @return une liste de salles dont le nom contient l'expression entrée en paramètre
     */
    @Override
    public List<RoomObject> searchRoomsContainingCharacters(String key) {
        return roomRepository.findByNameContainingIgnoreCase(key);
    }
}
