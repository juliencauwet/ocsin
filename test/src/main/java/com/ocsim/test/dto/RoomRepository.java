package com.ocsim.test.dto;

import com.ocsim.test.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository <Room, Integer> {

    Room findByName(String name);

}
