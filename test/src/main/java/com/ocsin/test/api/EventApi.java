package com.ocsin.test.api;

import com.ocsin.test.model.Event;
import com.ocsin.test.model.RoomObject;
import com.ocsin.test.services.impl.EventService;
import com.ocsin.test.services.impl.RoomObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * point d'entrée de l'API concernant les évènements
 */
@RequestMapping("/events")
@RestController
@CrossOrigin(value = "*")
public class EventApi {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoomObjectService roomService;

    @Autowired
    private EventService eventService;

    /**
     * réception d'un évènement en provenance des salles, recherche de la salle en fonction de son nom, changement du statut de la salle en fonction de l'évènement, enregistrement de l'évènement
     * @param event
     */
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void receiveEvent(@RequestBody Event event){

        logger.info("##### receiveEvent method #####");
        logger.info("évènement reçu:" + event.toString());
        //récupération de la salle dont le nom correspond à celui de l'évènement
        RoomObject room = roomService.getRoomByName(event.getRoom());

        //si l'évènement est "motion:on", on attribue true à la disponibilité de la salle, sinon false
        room.setAvailability(event.getEvent().equals("motion:on")? true : false);

        //on enregistre la salle avec disponibilité mise à jour en BDD
        roomService.saveRoom(room);

        //on attribue la salle à l'évènement et on l'enregistre
        event.setRoomObject(room);
        eventService.saveEvent(event);
    }

    /**
     *
     * @param roomId
     * @return le dernier évènement de la salle
     */
    @GetMapping("/room/last/{roomId}")
    public Event getRoomsLastEvent(@PathVariable int roomId ){
        logger.info("##### getRoomsLastEvent method #####");
        logger.info("id de la salle:" + roomId);

        try {
            return eventService.getRoomsLastEvent(roomId);
        }catch (NullPointerException e){
            logger.warn("La salle n'est pas enregistrée");
            return null;
        }

    }

    /**
     *
     * @param roomId
     * @return l'historique des 100 derniers évènements
     */
    @GetMapping("/room/history/{roomId}")
    public List<Event> getEventsHistoryByRoom(@PathVariable("roomId") int roomId){
        logger.info("##### getEventsHistoryByRoom method #####");
        logger.info("id de la salle:" + roomId);

        return eventService.getListOfEventsperRoom(roomId);
    }
}
